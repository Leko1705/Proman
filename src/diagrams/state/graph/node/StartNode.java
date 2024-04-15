package diagrams.state.graph.node;

import java.awt.*;

public class StartNode extends RoundStateNode {

    @Override
    public void paintComponent(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.BLACK);
        g.fillOval(0, 0, getWidth(), getHeight());
        g.setColor(c);
        super.paintComponent(g);
    }
}
