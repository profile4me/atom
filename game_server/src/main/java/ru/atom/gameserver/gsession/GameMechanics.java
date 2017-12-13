package ru.atom.gameserver.gsession;

import ru.atom.gameserver.model.GameObject;
import ru.atom.gameserver.model.Pawn;
import ru.atom.gameserver.tick.Tickable;

import java.util.ArrayList;
import java.util.List;

public class GameMechanics implements Tickable {

    private List<Pawn> pawns = new ArrayList<>();
    private List<GameObject> gameObjects = new ArrayList<>();

    void addPlayer(String login) {

    }

    @Override
    public void tick(long elapsed) {

    }

}
