package diagrams.flow.graph.node;

import diagrams.utils.BaseNode;
import graph.TextNode;
import utils.GeomUtils;

import java.awt.*;
import java.awt.geom.Line2D;

public class InputNode extends TextNode implements BaseNode<TextNode, TextNode> {

    private static final int SHIFT = 20;

    public InputNode(){
        setSize(150, 100);
        setOpaque(false);
    }

    @Override
    public Point getNearestPointOnOutline(Point q) {
        Rectangle r = getBounds();
        Point p = GeomUtils.getCenter(r);
        return GeomUtils.lineIntersectionOnRect(r, new Line2D.Double(p, q));
    }

    @Override
    public void paintComponent(Graphics g) {
        Polygon polygon = new Polygon();
        polygon.addPoint(SHIFT, 0);
        polygon.addPoint(getWidth(), 0);
        polygon.addPoint(getWidth() - SHIFT, getHeight() - 1);
        polygon.addPoint(0, getHeight() - 1);
        polygon.addPoint(SHIFT, 0);

        Graphics2D g2d = (Graphics2D) g;
        Color c = g2d.getColor();

        g2d.setColor(new Color(209, 225, 255));
        g2d.fillPolygon(polygon);

        g2d.setColor(c);
        g2d.draw(polygon);

        super.paintComponent(g);
    }
}
