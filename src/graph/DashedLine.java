package graph;

import utils.GeomUtils;

import java.awt.*;

public class DashedLine implements EdgeStyle {

    @Override
    public void paintEdge(Graphics g, Shape shape) {

        Graphics2D g2d = (Graphics2D) g.create();
        // Set the stroke of the copy, not the original
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{9}, 0);
        g2d.setStroke(dashed);

        g2d.draw(shape);
    }

}
