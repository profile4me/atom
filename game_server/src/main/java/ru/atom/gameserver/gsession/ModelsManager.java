package ru.atom.gameserver.gsession;

import ru.atom.gameserver.geometry.Bar;
import ru.atom.gameserver.geometry.Point;
import ru.atom.gameserver.model.Buff;
import ru.atom.gameserver.model.GameObject;
import ru.atom.gameserver.model.Pawn;

import java.util.List;

public interface ModelsManager {

    void putBomb(Point point, long lifetime, int power);

    void putFire(Point point, long lifetime, int power);

    void putBonus(Point point, Buff.BuffType buffType);

    List<Pawn> getIntersectPawns(Bar bar);

    List<GameObject> getIntersectStatic(Bar bar);

}
