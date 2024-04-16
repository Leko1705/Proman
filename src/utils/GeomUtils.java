package utils;

import java.awt.*;
import java.awt.geom.Line2D;

public final class GeomUtils {

    private GeomUtils(){
    }

    public static Point closestPointOnRectTo(Rectangle r1, Rectangle r2){
        Point m1 = GeomUtils.getCenter(r1);
        Point m2 = GeomUtils.getCenter(r2);
        return GeomUtils.lineIntersectionOnRect(r1, new Line2D.Double(m1, m2));
    }

    public static Point lineIntersectionOnRect(Rectangle rectangle, Line2D line) {

        double w = rectangle.getWidth() / 2;
        double h = rectangle.getHeight() / 2;

        double xB = line.getP1().getX();
        double yB = line.getP1().getY();

        double xA = line.getP2().getX();
        double yA = line.getP2().getY();

        double dx = xA - xB;
        double dy = yA - yB;

        //if A=B return B itself
        if (dx == 0 && dy == 0) return new Point((int) xB, (int) yB);

        double tan_phi = h / w;
        double tan_theta = Math.abs(dy / dx);

        //tell me in which quadrant the A point is
        double qx = (int) Math.signum(dx);
        double qy = (int) Math.signum(dy);


        double xI, yI;

        if (tan_theta > tan_phi) {
            xI = xB + (h / tan_theta) * qx;
            yI = yB + h * qy;
        } else {
            xI = xB + w * qx;
            yI = yB + w * tan_theta * qy;
        }

        return new Point((int) xI, (int) yI);
    }

    public static Point getCenter(Rectangle rectangle){
        int x = (int) (rectangle.getX() + rectangle.getWidth()/2);
        int y = (int) (rectangle.getY() + rectangle.getHeight()/2);
        return new Point(x, y);
    }

    public static Point getCenter(Point p, Point q){
        int x = p.x + ((q.x - p.x)/2);
        int y = p.y + ((q.y - p.y)/2);
        return new Point(x, y);
    }

    public static Point calculatePointOnCircle(Point center, double angle, double r){
        double rad = Math.toRadians(angle);
        double dx = Math.cos(rad);
        double dy = Math.sin(rad);
        double nx = (center.x + dx * r);
        double ny = (center.y + dy * r);
        return new Point((int) nx, (int) ny);
    }

    public static void paintStringInBetween(Graphics g, Point p, Point q, String s){
        int halfX = p.x + ((q.x - p.x) / 2);
        halfX -= g.getFontMetrics().stringWidth(s)/2;
        int halfY = p.y + ((q.y - p.y) / 2);
        g.drawString(s, halfX, halfY);
    }

    public static double euclideanDistance(Point p, Point q){
        return Math.sqrt(Math.pow(q.x - p.x, 2) + Math.pow(p.y - q.y, 2));
    }

    public static double calcAngle(Point p, Point q){
        return Math.toDegrees(Math.atan2(q.y - p.y, q.x - p.x));
    }

}
