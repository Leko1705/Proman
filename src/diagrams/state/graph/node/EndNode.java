package diagrams.state.graph.node;

import java.awt.*;

public class EndNode extends RoundBaseNode {

    @Override
    public void paintComponent(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.BLACK);

        final int STROKE_THICKNESS = 5;

        Graphics2D g2 = (Graphics2D) g;
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(STROKE_THICKNESS));
        g2.drawOval(STROKE_THICKNESS/2, STROKE_THICKNESS/2, getWidth()-STROKE_THICKNESS, getHeight()-STROKE_THICKNESS);
        g2.setStroke(oldStroke);

        final int offs = 2;
        final int offsSum = STROKE_THICKNESS + offs;
        g.fillOval(offsSum, offsSum, getWidth() - offsSum*2-offs, getHeight() - offsSum*2-offs);
        g.setColor(c);
        super.paintComponent(g);
    }

}
