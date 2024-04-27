package diagrams.utils;

import graph.OpenArrow;
import graph.PlainLine;
import graph.*;
import utils.GeomUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class Transition
        extends Edge<Transition>
        implements EdgeModel, EdgeView<Transition>, EdgeModelPainter<Transition> {

    private final BaseNode<?, ?> from, to;

    private String text = "";

    public Transition(BaseNode<?, ?> from, BaseNode<?, ?> to) {
        super(null, null);
        this.from = from;
        this.to = to;
    }

    public BaseNode<?, ?> getFrom() {
        return from;
    }

    public BaseNode<?, ?> getTo() {
        return to;
    }

    @Override
    public long getFromID() {
        return from.getModel().getID();
    }

    @Override
    public long getToID() {
        return to.getModel().getID();
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public Transition getModel() {
        return this;
    }

    @Override
    public EdgeView<Transition> getView() {
        return this;
    }

    @Override
    public EdgeModelPainter<Transition> getModelPainter(Node<?, ?> from, Node<?, ?> to) {
        return this;
    }

    @Override
    public Shape getEdgePath(Node<?, ?> from, Node<?, ?> to) {
        if (from == to){
            return getCyclicEdgePath();
        }
        else {
            return getDirectEdgePath();
        }
    }

    private Shape getCyclicEdgePath(){
        Component v = from.getView();
        Point c = GeomUtils.getCenter(v.getBounds());
        return new Ellipse2D.Double(c.x, c.y - v.getHeight(), v.getWidth(), v.getHeight());
    }

    private Shape getDirectEdgePath(){
        Point p = from.getNearestPointOnOutline(GeomUtils.getCenter(to.getView().getBounds()));
        Point q = to.getNearestPointOnOutline(GeomUtils.getCenter(from.getView().getBounds()));

        return Transitions.createClickableLine(p, q);
    }

    @Override
    public void paintEdge(Graphics g, Shape shape) {
        if (from == to){
            paintCycle(g, shape.getBounds());
        }
        else {
            paintDirect(g);
        }
    }

    private void paintDirect(Graphics g){
        Point p = from.getNearestPointOnOutline(GeomUtils.getCenter(to.getView().getBounds()));
        Point q = to.getNearestPointOnOutline(GeomUtils.getCenter(from.getView().getBounds()));

        EdgeStyle styledPainter = new OpenArrow(new PlainLine());
        styledPainter.paintEdge(g, new Line2D.Double(p, q));

        GeomUtils.paintStringInBetween(g, p, q, text);
    }

    private void paintCycle(Graphics g, Rectangle ellipseBounds) {
        Rectangle bounds = from.getView().getBounds();

        int x = bounds.x + bounds.width;
        int y = bounds.y;

        g.drawArc(x-(bounds.width/2), y-(bounds.height/2), bounds.width, bounds.height, 270, 270);

        x = bounds.x + bounds.width/2;

        final int ARROW_ANGLE_LINE_LENGTH = 10;

        g.drawLine(x, y, x - ARROW_ANGLE_LINE_LENGTH, y - ARROW_ANGLE_LINE_LENGTH);
        g.drawLine(x, y, x + ARROW_ANGLE_LINE_LENGTH, y - ARROW_ANGLE_LINE_LENGTH);

        x = (int) (ellipseBounds.getMaxX() - ellipseBounds.getWidth() / 4);
        y = (int) (ellipseBounds.getMinY() + ellipseBounds.getHeight() / 4);
        g.drawString(text, x, y);
    }

    @Override
    public void paintModel(Graphics g, Transition model) {}
}
