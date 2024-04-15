package graph;

import utils.GeomUtils;

import java.awt.*;

public class OutlineArrow extends EdgeStyleDecorator {

    public OutlineArrow(EdgeStyle style) {
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

        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.fillPolygon(xpoints, ypoints, npoints);
        g.setColor(c);
        g.drawPolyline(xpoints, ypoints, npoints);
    }

}
