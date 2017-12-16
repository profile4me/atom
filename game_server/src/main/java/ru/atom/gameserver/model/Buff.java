package ru.atom.gameserver.model;

import ru.atom.gameserver.geometry.Point;
import ru.atom.gameserver.tick.Tickable;

public class Buff extends SaneGameObject implements Tickable {

    private final BuffType buffType;

    public Buff(int id, Point position, BuffType type) {
        super(id, position);
        this.buffType = type;
    }

    public BuffType getType() {
        return buffType;
    }

    @Override
    public void tick(long elapsed) {

    }

    public enum BuffType {
        VELOCITY,    // speed of player
        POWER,    // max size of explosion line
        CAPACITY; // max numbers of bombs

        public void affect() {
            switch (this) {
                case VELOCITY:
                    break;
                case POWER:
                    break;
                case CAPACITY:
                    break;
                default:
                    break;
            }
        }
    }
}
