package diagrams.entityRel.graph.node;

import diagrams.utils.BaseNode;
import graph.TextNode;
import utils.GeomUtils;

import java.awt.*;

public class AttributeNode
        extends TextNode
        implements BaseNode<TextNode, TextNode> {

    public AttributeNode(){
        setSize(150, 80);
        setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        Color c = g.getColor();
        g.setColor(new Color(255, 209, 209));
        g.fillOval(0, 0, getWidth()-1, getHeight()-1);
        g.setColor(c);
        g.drawOval(0, 0, getWidth()-1, getHeight()-1);
        super.paintComponent(g);
    }

    @Override
    public Point getNearestPointOnOutline(Point q) {
        // TODO fix this
        Point m = GeomUtils.getCenter(getBounds());
        double alpha = GeomUtils.calcAngle(m, q);
        int a = getWidth()/2;
        int b = getHeight()/2;
        int x = (int) (a * Math.cos(alpha));
        int y = (int) (b * Math.sin(alpha));
        return new Point(m.x + x, m.y + y);
    }
}
