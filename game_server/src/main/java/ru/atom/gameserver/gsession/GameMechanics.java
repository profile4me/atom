package ru.atom.gameserver.gsession;

import ru.atom.gameserver.geometry.Point;
import ru.atom.gameserver.model.GameObject;
import ru.atom.gameserver.model.Pawn;
import ru.atom.gameserver.model.Wood;
import ru.atom.gameserver.tick.Tickable;
import ru.atom.gameserver.tick.Ticker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameMechanics implements Tickable {

    private int id;
    private final Ticker ticker;
    private final Replicator replicator;

    private final List<Pawn> pawns = new ArrayList<>();
    private final List<GameObject> gameObjects = new CopyOnWriteArrayList<>();

    GameMechanics(Ticker ticker, Replicator replicator) {
        id = 0;
        this.ticker = ticker;
        this.replicator = replicator;
        //init walls and boxes here
        //bombs, explosion and paws must be added to ticker
        gameObjects.add(new Wood(id++, new Point(32.0f, 32.0f)));
    }

    int addPlayer() {
        //add to ticker!
        return pawns.size() - 1;
    }

    @Override
    public void tick(long elapsed) {
        boolean gameOverFlag = false;

        //send replica via replicator
        replicator.writeReplica(gameObjects, gameOverFlag);
    }

}
