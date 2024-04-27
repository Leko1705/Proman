package graph;

import java.awt.*;

public class PlainLine implements EdgeStyle {

    @Override
    public void paintEdge(Graphics g, Shape shape) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.draw(shape);
    }

}
