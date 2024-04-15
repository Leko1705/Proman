package graph;

import utils.GeomUtils;

import java.awt.*;

public class OpenArrow extends EdgeStyleDecorator {

    public OpenArrow(EdgeStyle style) {
        super(style);
    }

    @Override
    public void paintEdge(Graphics g,Point from, Point to) {
        super.paintEdge(g, from, to);

        double alpha = GeomUtils.calcAngle(from, to);

        Point s = GeomUtils.calculatePointOnCircle(to, alpha + 180 + ANGULATION, 20);
        g.drawLine(to.x, to.y, s.x, s.y);
        s = GeomUtils.calculatePointOnCircle(to, alpha + 180 - ANGULATION, 20);
        g.drawLine(to.x, to.y, s.x, s.y);
    }

}
