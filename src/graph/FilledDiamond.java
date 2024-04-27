package graph;

import utils.GeomUtils;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class FilledDiamond extends EdgeStyleDecorator {

    public FilledDiamond(EdgeStyle style) {
        super(style);
    }

    @Override
    public void paintEdge(Graphics g, Shape shape) {
        super.paintEdge(g, shape);
        Line2D segment = getFirstSegment(shape);
        Point2D from = segment.getP1();
        Point2D to = segment.getP2();

        double alpha = GeomUtils.calcAngle(from, to);

        Point s1 = GeomUtils.calculatePointOnCircle(from, alpha + ANGULATION, 20);
        Point s2 = GeomUtils.calculatePointOnCircle(from, alpha - ANGULATION, 20);
        Point s3 = GeomUtils.calculatePointOnCircle(from, alpha, 37);

        int[] xpoints = {(int) from.getX(), s1.x, s3.x, s2.x, (int) from.getX()};
        int[] ypoints = {(int) from.getY(), s1.y, s3.y, s2.y, (int) from.getY()};
        int npoints = xpoints.length;

        Color c = g.getColor();
        g.setColor(Color.BLACK);
        g.fillPolygon(xpoints, ypoints, npoints);
        g.setColor(c);
    }
}
