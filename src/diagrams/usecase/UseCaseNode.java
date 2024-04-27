package diagrams.usecase;

import diagrams.state.graph.node.RoundBaseNode;

import java.awt.*;

public class UseCaseNode extends RoundBaseNode {

    public UseCaseNode() {
        setSize(150, 70);
        setDisabledTextColor(Color.BLACK);
        setForeground(Color.BLACK);
        setCaretColor(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.BLACK);
        g.drawOval(0, 0, getWidth()-1, getHeight()-1);
        g.setColor(c);
        super.paintComponent(g);
    }

}
