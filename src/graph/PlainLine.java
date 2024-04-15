package graph;

import utils.GeomUtils;

import java.awt.*;

public class PlainLine implements EdgeStyle {

    @Override
    public void paintEdge(Graphics g, Point from, Point to) {
        g.drawLine(from.x, from.y, to.x, to.y);
    }

}
