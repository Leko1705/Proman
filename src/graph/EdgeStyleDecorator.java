package graph;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

public class EdgeStyleDecorator implements EdgeStyle {

    private final EdgeStyle style;

    public EdgeStyleDecorator(EdgeStyle style) {
        this.style = style;
    }

    @Override
    public void paintEdge(Graphics g, Shape shape) {
        style.paintEdge(g, shape);
    }

    public Line2D getFirstSegment(Shape shape) {
        // TODO test this

        PathIterator it = shape.getPathIterator(null);
        double[] coords = new double[6];

        Point2D from, to;
        it.currentSegment(coords);
        from = new Point2D.Double(coords[0], coords[1]);

        it.next();
        to = switch (it.currentSegment(coords)){
            case PathIterator.SEG_LINETO -> new Point2D.Double(coords[0], coords[1]);
            case PathIterator.SEG_QUADTO -> new Point2D.Double(coords[2], coords[3]);
            case PathIterator.SEG_CUBICTO -> new Point2D.Double(coords[4], coords[5]);
            default -> throw new IllegalStateException();
        };

        return new Line2D.Double(from, to);
    }

    public Line2D getLastSegment(Shape shape) {
        // TODO test this

        PathIterator it = shape.getPathIterator(null);

        Point2D from = null, to = null;
        double[] coords = new double[6];

        label:
        for (; !it.isDone(); it.next()) {
            switch (it.currentSegment(coords)) {

                case PathIterator.SEG_MOVETO:
                    to = new Point2D.Double(coords[0], coords[1]);
                    break;

                case PathIterator.SEG_LINETO:
                    from = to;
                    to = new Point2D.Double(coords[0], coords[1]);
                    break;

                case PathIterator.SEG_QUADTO:
                    from = to;
                    to = new Point2D.Double(coords[2], coords[3]);
                    break;

                case PathIterator.SEG_CUBICTO:
                    from = to;
                    to = new Point2D.Double(coords[4], coords[5]);
                    break;

                case PathIterator.SEG_CLOSE:
                    break label;
            }
        }

        return new Line2D.Double(from, to);
    }
}
