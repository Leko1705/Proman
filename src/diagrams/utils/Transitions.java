package diagrams.utils;

import utils.GeomUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public final class Transitions {

    private static final int LINE_CLICK_BOUNDS = 12;

    private Transitions(){
    }


    public static Shape createClickableLine(Point p, Point q) {

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

}
