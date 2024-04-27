package graph;

import utils.GeomUtils;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class OpenArrow extends EdgeStyleDecorator {

    public OpenArrow(EdgeStyle style) {
        super(style);
    }

    @Override
    public void paintEdge(Graphics g, Shape shape) {
        super.paintEdge(g, shape);
        Line2D segment = getLastSegment(shape);

        Point2D from = segment.getP1();
        Point2D to = segment.getP2();

        double alpha = GeomUtils.calcAngle(from, to);

        Point s = GeomUtils.calculatePointOnCircle(to, alpha + 180 + ANGULATION, 20);
        g.drawLine((int) to.getX(), (int) to.getY(), s.x, s.y);
        s = GeomUtils.calculatePointOnCircle(to, alpha + 180 - ANGULATION, 20);
        g.drawLine((int) to.getX(), (int) to.getY(), s.x, s.y);
    }

}
