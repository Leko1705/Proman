package diagrams.utils;

import graph.OpenArrow;
import graph.PlainLine;
import graph.*;
import utils.GeomUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Transition
        extends Edge<Transition>
        implements EdgeModel, EdgeView<Transition>, EdgeModelPainter<Transition> {

    private static final int LINE_CLICK_BOUNDS = 12;

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

        double angle = GeomUtils.calcAngle(p, q);

        int distance = (int) Math.sqrt(Math.pow(q.x - p.x, 2) + Math.pow(q.y - p.y, 2));

        int fromX = Math.min(p.x, q.x);
        int fromY = Math.min(p.y, q.y);

        Rectangle r = new Rectangle(fromX - LINE_CLICK_BOUNDS, fromY - LINE_CLICK_BOUNDS, distance + LINE_CLICK_BOUNDS, LINE_CLICK_BOUNDS * 2);

        AffineTransform rotateTransform = AffineTransform.getRotateInstance((angle*2*Math.PI)/360d);
        // rotate the original shape with no regard to the final bounds
        Shape rotatedShape = rotateTransform.createTransformedShape(r);
        // get the bounds of the rotated shape
        Rectangle2D rotatedRect = rotatedShape.getBounds2D();
        // calculate the x,y offset needed to shift it to top/left bounds of original rectangle
        double xOff = r.getX()-rotatedRect.getX();
        double yOff = r.getY()-rotatedRect.getY();
        AffineTransform translateTransform = AffineTransform.getTranslateInstance(xOff, yOff);
        // shift the new shape to the top left of original rectangle
        return translateTransform.createTransformedShape(rotatedShape);
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

        new OpenArrow(new PlainLine()).paintEdge(g, p, q);

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
