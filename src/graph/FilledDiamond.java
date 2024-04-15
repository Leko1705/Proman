package graph;

import utils.GeomUtils;

import java.awt.*;

public class FilledDiamond extends EdgeStyleDecorator {

    public FilledDiamond(EdgeStyle style) {
        super(style);
    }

    @Override
    public void paintEdge(Graphics g, Point from, Point to) {
        super.paintEdge(g, from, to);

        double alpha = GeomUtils.calcAngle(from, to);

        Point s1 = GeomUtils.calculatePointOnCircle(from, alpha + ANGULATION, 20);
        Point s2 = GeomUtils.calculatePointOnCircle(from, alpha - ANGULATION, 20);
        Point s3 = GeomUtils.calculatePointOnCircle(from, alpha, 37);

        int[] xpoints = {from.x, s1.x, s3.x, s2.x, from.x};
        int[] ypoints = {from.y, s1.y, s3.y, s2.y, from.y};
        int npoints = xpoints.length;

        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.fillPolygon(xpoints, ypoints, npoints);
        g.setColor(c);
        g.fillPolygon(xpoints, ypoints, npoints);
    }
}
