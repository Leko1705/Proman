package diagrams.state.graph.node;

import graph.TextNode;
import utils.GeomUtils;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;

public class SimpleState extends TextNode implements StateNode<TextNode, TextNode> {

    public SimpleState(){
        setSize(150, 100);
        setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.BLACK);

        Graphics2D g2 = (Graphics2D) g;
        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints( qualityHints);

        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, 50, 50));

        g.setColor(c);
        super.paintComponent(g);
    }

    @Override
    public Point getNearestPointOnOutline(Point q) {
        Rectangle r = getBounds();
        Point p = GeomUtils.getCenter(r);
        return GeomUtils.lineIntersectionOnRect(r, new Line2D.Double(p, q));
    }

}
