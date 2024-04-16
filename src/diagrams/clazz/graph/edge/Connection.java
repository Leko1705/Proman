package diagrams.clazz.graph.edge;

import graph.*;
import graph.Edge;
import utils.GeomUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class Connection
        extends Edge<Connection>
        implements EdgeModel, EdgeView<Connection>, EdgeModelPainter<Connection> {

    private static final int LINE_CLICK_BOUNDS = 12;


    private Node<?, ?> from, to;

    private final Set<EdgeChangeListener> changeListeners = new HashSet<>();

    private EdgeStyle style;

    private int styleIndex;

    private String centerText = "";

    private String leftText = "";

    private String rightText = "";


    public Connection(Node<?, ?> from, Node<?, ?> to, EdgeStyle style) {
        super(null, null);
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        this.style = Objects.requireNonNull(style);
    }

    public void setStyle(EdgeStyle style) {
        this.style = Objects.requireNonNull(style);
        notifyListeners();
    }

    public void setStyleIndex(int styleIndex) {
        this.styleIndex = styleIndex;
    }

    public int getStyleIndex() {
        return styleIndex;
    }

    public EdgeStyle getStyle() {
        return style;
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = Objects.requireNonNull(leftText);
        notifyListeners();
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = Objects.requireNonNull(rightText);
        notifyListeners();
    }

    public String getCenterText() {
        return centerText;
    }

    public void setCenterText(String centerText) {
        this.centerText = Objects.requireNonNull(centerText);
        notifyListeners();
    }

    public void addEdgeChangeListener(EdgeChangeListener listener) {
        changeListeners.add(listener);
    }

    public void removeEdgeChangeListener(EdgeChangeListener listener) {
        changeListeners.remove(listener);
    }

    private void notifyListeners() {
        for (EdgeChangeListener listener : changeListeners)
            listener.onChange();
    }

    @Override
    public long getFromID() {
        return from.getModel().getID();
    }

    @Override
    public long getToID() {
        return to.getModel().getID();
    }

    public Node<?, ?> getFrom() {
        return from;
    }

    public Node<?, ?> getTo() {
        return to;
    }

    @Override
    public Connection getModel() {
        return this;
    }

    @Override
    public EdgeView<Connection> getView() {
        return this;
    }

    @Override
    public EdgeModelPainter<Connection> getModelPainter(Node<?, ?> from, Node<?, ?> to) {
        return this;
    }

    @Override
    public void paintEdge(Graphics g, Shape shape) {
        Point p = GeomUtils.closestPointOnRectTo(from.getView().getBounds(), to.getView().getBounds());
        Point q = GeomUtils.closestPointOnRectTo(to.getView().getBounds(), from.getView().getBounds());
        style.paintEdge(g, p, q);
    }

    public void changeDirection() {
        Node<?, ?> temp = from;
        from = to;
        to = temp;
        notifyListeners();
    }

    public Shape getEdgePath(Node<?, ?> from, Node<?, ?> to){
        Point p = GeomUtils.closestPointOnRectTo(from.getView().getBounds(), to.getView().getBounds());
        Point q = GeomUtils.closestPointOnRectTo(to.getView().getBounds(), from.getView().getBounds());

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

    public void paintModel(Graphics g, Connection model){
        Point p = GeomUtils.closestPointOnRectTo(from.getView().getBounds(), to.getView().getBounds());
        Point q = GeomUtils.closestPointOnRectTo(to.getView().getBounds(), from.getView().getBounds());
        GeomUtils.paintStringInBetween(g, p, q, getCenterText());

        putEdgeString(g, p, q, leftText);
        putEdgeString(g, q, p, rightText);
    }

    private void putEdgeString(Graphics g, Point p, Point q, String text){
        double angle = GeomUtils.calcAngle(p, q);

        Point m = GeomUtils.calculatePointOnCircle(p, angle, 15);
        int offs = Math.max(0, (p.x - m.x)/2 + g.getFontMetrics().stringWidth(text));

        if (p.x < m.x) {
            m.x += charWidth(g);
        }
        else {
            m.x -= offs;
        }
        g.drawString(text, m.x, m.y);
    }

    private int charWidth(Graphics g){
        return g.getFontMetrics().stringWidth("m");
    }

}
