package graph;

import java.awt.*;

public interface EdgeStyle {

    double ANGULATION = 25;

    void paintEdge(Graphics g, Point from, Point to);
}
