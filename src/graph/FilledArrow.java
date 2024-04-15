package graph;

import utils.GeomUtils;

import java.awt.*;

public class FilledArrow extends EdgeStyleDecorator {

    public FilledArrow(EdgeStyle style) {
        super(style);
    }

    @Override
    public void paintEdge(Graphics g, Point from, Point to) {
        super.paintEdge(g, from, to);

        double alpha = GeomUtils.calcAngle(from, to);

        Point s1 = GeomUtils.calculatePointOnCircle(to, alpha + 180 + ANGULATION, 20);
        Point s2 = GeomUtils.calculatePointOnCircle(to, alpha + 180 - ANGULATION, 20);

        int[] xpoints = {to.x, s1.x, s2.x, to.x};
        int[] ypoints = {to.y, s1.y, s2.y, to.y};
        int npoints = xpoints.length;

        g.fillPolygon(xpoints, ypoints, npoints);
    }

}
