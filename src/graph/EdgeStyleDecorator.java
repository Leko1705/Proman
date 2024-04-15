package graph;

import java.awt.*;

public class EdgeStyleDecorator implements EdgeStyle {

    private final EdgeStyle style;

    public EdgeStyleDecorator(EdgeStyle style) {
        this.style = style;
    }

    @Override
    public void paintEdge(Graphics g, Point from, Point to) {
        style.paintEdge(g, from, to);
    }
}
