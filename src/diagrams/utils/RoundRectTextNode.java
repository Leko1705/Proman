package diagrams.utils;

import graph.TextNode;
import utils.GeomUtils;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;

public class RoundRectTextNode extends TextNode implements BaseNode<TextNode, TextNode> {

    private int arcw, arch;

    Color outline;

    Color fillColor;

    public RoundRectTextNode(int arcw, int arch){
        this.arcw = arcw;
        this.arch = arch;
        setOpaque(false);
    }

    public void setArch(int arch) {
        this.arch = arch;
    }

    public void setArcw(int arcw) {
        this.arcw = arcw;
    }

    public int getArch() {
        return arch;
    }

    public int getArcw() {
        return arcw;
    }

    public Color getOutline() {
        return outline;
    }

    public void setOutline(Color outline) {
        this.outline = outline;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints( qualityHints);

        RoundRectangle2D rect = new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, arcw, arch);
        Color c = g.getColor();

        if (fillColor != null) {
            g.setColor(fillColor);
            g2.fill(rect);
        }

        if (outline != null) {
            g.setColor(outline);
            g2.draw(rect);
        }

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
