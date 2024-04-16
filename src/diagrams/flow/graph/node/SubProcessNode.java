package diagrams.flow.graph.node;

import java.awt.*;

public class SubProcessNode extends ProcessNode {

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(10, 0, 10, getHeight());
        g.drawLine(getWidth() - 10, 0, getWidth() - 10, getHeight());
    }
}
