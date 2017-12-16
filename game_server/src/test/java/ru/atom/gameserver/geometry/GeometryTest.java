package ru.atom.gameserver.json.geometry;

import org.junit.Test;
import ru.atom.gameserver.geometry.Bar;
import ru.atom.gameserver.geometry.Point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class GeometryTest {

    @Test
    public void barTest() {
        Bar bar = new Bar(0, 0, 10, 10);
        Point expectedOriginCorner = new Point(0,0);
        Point expectedEndCorner = new Point(10, 10);

        //проверка getOriginCorner
        assertTrue(Math.abs(expectedOriginCorner.getX() - bar.getOriginCorner().getX()) < 0.001f
                && Math.abs(expectedOriginCorner.getY() - bar.getOriginCorner().getY()) < 0.001f);

        //проверка getEndCorner
        assertTrue(Math.abs(expectedEndCorner.getX() - bar.getEndCorner().getX()) < 0.001f
                && Math.abs(expectedEndCorner.getY() - bar.getEndCorner().getY()) < 0.001f);

        //проверка getWidth, getHeight
        assertTrue(Math.abs(bar.getHeight() - 10.0f) < 0.001);
        assertTrue(Math.abs(bar.getWidth() - 10.0f) < 0.001);

        //проверка isIntersecting
        Bar otherBar = new Bar(15, 15, 25, 25);
        assertTrue(!bar.isIntersecting(otherBar));
        otherBar = new Bar(5, 5, 15, 15);
        assertTrue(bar.isIntersecting(otherBar));

        //проверка isIncluding(Point)
        Point point = new Point(15, 15);
        assertTrue(!bar.isIncluding(point));
        point = new Point(5, 5);
        assertTrue(bar.isIncluding(point));

        //проверка isIncluding(Bar)
        otherBar = new Bar(5, 5, 15, 15);
        assertTrue(!bar.isIncluding(otherBar));
        otherBar = new Bar(2,2,8,8);
        assertTrue(bar.isIncluding(otherBar));

        //проверка isColliding
        otherBar = new Bar(9,9,15,15);
        assertTrue(bar.isColliding(otherBar));
        point = new Point(9,9);
        assertTrue(bar.isColliding(point));

        //проверка equals
        otherBar = new Bar(new Point(0,0), 10, 10);
        assertTrue(bar.equals(otherBar));
    }

    @Test
    public void pointTest() {
        Point point = new Point(0,0);

        //проверка getX, getY
        assertTrue(Math.abs(point.getX()) < 0.001 && Math.abs(point.getY()) < 0.001);

        //проверка equals
        assertTrue(point.equals(new Point(0,0)));
    }
}
