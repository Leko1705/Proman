package graph;

import utils.GeomUtils;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class OutlineArrow extends EdgeStyleDecorator {

    public OutlineArrow(EdgeStyle style) {
        super(style);
    }

    @Override
    public void paintEdge(Graphics g, Shape shape) {
        super.paintEdge(g, shape);
        Line2D segment = getLastSegment(shape);
        Point2D from = segment.getP1();
        Point2D to = segment.getP2();

        double alpha = GeomUtils.calcAngle(from, to);

        Point s1 = GeomUtils.calculatePointOnCircle(to, alpha + 180 + ANGULATION, 20);
        Point s2 = GeomUtils.calculatePointOnCircle(to, alpha + 180 - ANGULATION, 20);

        int[] xpoints = {(int) to.getX(), s1.x, s2.x, (int) to.getX()};
        int[] ypoints = {(int) to.getY(), s1.y, s2.y, (int) to.getY()};
        int npoints = xpoints.length;

        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.fillPolygon(xpoints, ypoints, npoints);
        g.setColor(c);
        g.drawPolyline(xpoints, ypoints, npoints);
    }

}
