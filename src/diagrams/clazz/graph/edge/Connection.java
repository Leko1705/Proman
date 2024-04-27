package diagrams.clazz.graph.edge;

import diagrams.utils.Transitions;
import graph.*;
import graph.Edge;
import utils.GeomUtils;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Connection
        extends Edge<Connection>
        implements EdgeModel, EdgeView<Connection>, EdgeModelPainter<Connection> {

    private static final int CYCLE_OFFSET = 35;


    private Node<?, ?> from, to;

    private final Set<EdgeChangeListener> changeListeners = new HashSet<>();

    private EdgeStyle style;

    private int styleIndex;

    private String centerText = "";

    private String leftText = "";

    private String rightText = "";

    private boolean straightLine = true;


    // are two nodes connected to each other?
    private boolean circularDependence = false;


    public Connection(Node<?, ?> from, Node<?, ?> to, EdgeStyle style) {
        super(null, null);
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        this.style = Objects.requireNonNull(style);
    }

    public void setCircularDependence(boolean circularDependence) {
        this.circularDependence = circularDependence;
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
        if (straightLine){

            Point p = GeomUtils.closestPointOnRectTo(from.getView().getBounds(), to.getView().getBounds());
            Point q = GeomUtils.closestPointOnRectTo(to.getView().getBounds(), from.getView().getBounds());

            if (circularDependence){
               Point m = GeomUtils.getCenter(p, q);
               double angle = GeomUtils.calcAngle(p, m);
               Point c = GeomUtils.calculatePointOnCircle(m, angle + 90, 20);
               Path2D path = new Path2D.Double();
               path.moveTo(p.x, p.y);
               path.lineTo(c.x, c.y);
               path.lineTo(q.x, q.y);
               style.paintEdge(g, path);
               // ((Graphics2D) g).draw(path.getBounds());
            }
            else {
                style.paintEdge(g, new Line2D.Double(p, q));
            }
        }
        else {
            style.paintEdge(g, shape);
        }
    }

    public void changeDirection() {
        Node<?, ?> temp = from;
        from = to;
        to = temp;
        notifyListeners();
    }

    public Shape getEdgePath(Node<?, ?> from, Node<?, ?> to){
        if (from == to){
            straightLine = false;
            return getCyclicShape(from);
        }
        else {
            straightLine = true;
            return getDirectConnectionShape(from, to);
        }
    }

    private Shape getCyclicShape(Node<?, ?> from){
        Path2D path = new Path2D.Double();

        Component node = from.getView();

        Point p1 = node.getLocation();
        p1.translate(node.getWidth(), node.getHeight()/2);

        Point p2 = p1.getLocation();
        p2.translate(CYCLE_OFFSET, 0);

        Point p3 = p2.getLocation();
        p3.translate(0, -(node.getHeight()/2 + CYCLE_OFFSET + 10));

        Point p4 = node.getLocation();
        p4.translate(node.getWidth()/2, -(CYCLE_OFFSET + 10));

        Point p5 = node.getLocation();
        p5.translate(node.getWidth()/2, 0);

        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.lineTo(p4.x, p4.y);
        path.lineTo(p5.x, p5.y);
        return path;
    }

    private Shape getDirectConnectionShape(Node<?, ?> from, Node<?, ?> to){
        Point p = GeomUtils.closestPointOnRectTo(from.getView().getBounds(), to.getView().getBounds());
        Point q = GeomUtils.closestPointOnRectTo(to.getView().getBounds(), from.getView().getBounds());

        return Transitions.createClickableLine(p, q);
    }

    public void paintModel(Graphics g, Connection model){

        if (from == to) {
            Component v = from.getView();
            Point p = v.getLocation();
            p.translate(v.getWidth(), v.getHeight()/2);
            g.drawString(leftText, p.x + 5, p.y + 15);

            p.translate(CYCLE_OFFSET, 0);
            p.translate(0, -(v.getHeight()/2 + CYCLE_OFFSET + 10));
            g.drawString(centerText, p.x + 5, p.y);

            p = v.getLocation();
            p.translate(v.getWidth()/2, 0);
            g.drawString(rightText, p.x + 10, p.y - 10);
        }
        else {
            Point p = GeomUtils.closestPointOnRectTo(from.getView().getBounds(), to.getView().getBounds());
            Point q = GeomUtils.closestPointOnRectTo(to.getView().getBounds(), from.getView().getBounds());
            GeomUtils.paintStringInBetween(g, p, q, getCenterText());

            putEdgeString(g, p, q, leftText);
            putEdgeString(g, q, p, rightText);
        }
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
