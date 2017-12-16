package ru.atom.gameserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.atom.gameserver.geometry.Bar;
import ru.atom.gameserver.geometry.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr on 06.12.2017.
 */
public class Pawn extends SaneGameObject implements Movable{

    private float velocity;
    private int maxBombs;
    private int bombPower;
    private float speedModifier;
    @JsonIgnore
    private List<Bomb> bombs;


    public Pawn(int id, Point position, float velocity, int maxBombs) {
        super(id, position);
        this.velocity = velocity;
        this.maxBombs = maxBombs;
        this.bombPower = 1;
        this.speedModifier = 1.0f;
        bombs = new ArrayList<>();
    }

    private Bar getSpecificBar(Point position) {
        return new Bar(position, 28, 28);
    }

    @Override
    public void calculateBar() {
        setBar(getSpecificBar(getPosition()));
    }

    public float getVelocity() {
        return velocity;
    }

    public int getMaxBombs() {
        return maxBombs;
    }

    public void setMaxBombs(int maxBombs) {
        this.maxBombs = maxBombs;
    }

    public int getBombPower() {
        return bombPower;
    }

    public void setBombPower(int bombPower) {
        this.bombPower = bombPower;
    }

    public float getSpeedModifier() {
        return speedModifier;
    }

    public void setSpeedModifier(float speedModifier) {
        this.speedModifier = speedModifier;
    }

    @Override
    public void tick(long elapsed) {

    }

    @Override
    public Point move(Direction direction, long time) {
        float dist = velocity * speedModifier * (time / 1000.0f);
        Point lastPosition = getPosition();
        Point newPosition;
        switch (direction) {
            case UP: newPosition = new Point(lastPosition.getX(), lastPosition.getY() + dist); break;
            case RIGHT: newPosition = new Point(lastPosition.getX() + dist, lastPosition.getY()); break;
            case DOWN: newPosition = new Point(lastPosition.getX(), lastPosition.getY() - dist); break;
            case LEFT: newPosition = new Point(lastPosition.getX() - dist, lastPosition.getY()); break;
            default: newPosition = new Point(lastPosition.getX(), lastPosition.getY());
        }
        Bar currBar = getBar();
        Bar nextBar = getSpecificBar(newPosition);
        List<GameObject> statics = modelsManager.getIntersectStatic(nextBar);
        boolean collision = false;
        for (GameObject gameObject : statics) {
            if (nextBar.isColliding(gameObject.getBar())) {
                if (!currBar.isColliding(gameObject.getBar())) {
                    collision = true;
                    break;
                }
            }
        }
        if (!collision) {
            setPosition(newPosition);
        }

        return getPosition();
    }

    public void plainBombEvent() {
        modelsManager.putBomb(getPosition(), 2000, bombPower);
    }

}
