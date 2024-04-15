package mylib.view.diagram;

import mylib.functional.Evaluable;
import mylib.functional.Functional;
import mylib.view.struct.DataSet;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;

@SuppressWarnings("unused")
public class CoordinateSystem2D extends Diagram {
    private final HashMap<String, EvaluableAdapter> functions = new HashMap<>();

    private DataSet<Object, Point> points;

    private final double[][] base;

    private double zoomX = 100, zoomY = 100;

    private double[] cursorPosition = {(double) getWidth() /2, (double) getHeight() /2};
    private final double[] bias = {0, 0};

    private final double SENSITIVITY = 5;

    public CoordinateSystem2D(){
        base = new double[][]{
                {1, 0},
                {0, 1}
        };
    }

    public CoordinateSystem2D(double[][] base){
        if (base.length != 2 || base[0].length != 2)
            throw new IllegalArgumentException("2x2 matrix expected");
        this.base = base;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.clearRect(0, 0, getWidth(), getHeight());

        int[] origin = {(int) (getWidth()/2+bias[0]*zoomX), (int) (getHeight()/2+bias[1]*zoomY)};
        int ot = 3;

        g2.fillRect(origin[0], 0, ot, getHeight());
        g2.fillRect(0, origin[1], getWidth(), ot);

        g2.drawString("zoomX: " + zoomX, 50, 50);
        g2.drawString("zoomY: " + zoomY, 50, 70);
        g2.drawString("0", origin[0]-15, origin[1]+20);
        g2.drawString("1", (int) (origin[0]+zoomX-ot), origin[1]+25);
        g2.drawString("-1", (int) (origin[0]-zoomX-ot), origin[1]+25);

        g2.drawString("-1", origin[0]-25, (int) (origin[1]+zoomY+ot));
        g2.drawString("1", origin[0]-25, (int) (origin[1]-zoomY+ot));
        g2.setStroke(new BasicStroke(3));

        for (int i = 0; i < getHeight()/2; i++){
            if (origin[1]+i*zoomY != origin[1])
                g2.drawLine(origin[0]-5, (int) (origin[1]+i*zoomY), origin[0]+5, (int) (origin[1]+i*zoomY));
        }

        // y axis
        for (int i = 0; i < getHeight()/2; i++){
            if (origin[1]+i*zoomY != origin[1])
                g2.drawLine(origin[0]-5, (int) (origin[1]-i*zoomY), origin[0]+5, (int) (origin[1]-i*zoomY));
        }

        // x axis
        for (int i = 0; i < getWidth()/2; i++){
            if (origin[0]+i*zoomX != origin[0])
                g2.drawLine((int) (origin[0]+i*zoomX), origin[1]-5, (int) (origin[0]+i*zoomX), origin[1]+5);
        }

        for (int i = 0; i < getWidth()/2; i++){
            if (origin[0]+i*zoomX != origin[0])
                g2.drawLine((int) (origin[0]-i*zoomX), origin[1]-5, (int) (origin[0]-i*zoomX), origin[1]+5);
        }

        for (EvaluableAdapter function : functions.values()){
            Polygon polygon = new Polygon();
            g2.setColor(function.color);

            for (int i = -getWidth(); i < getWidth(); i += 1) {
                int x = origin[0] + i + (ot / 2);
                double value = function.eval((double) i);
                if (!Double.isNaN(value)) {
                    double y = Math.round(origin[1] - (function.eval(1 / zoomX * i) * zoomY));
                    polygon.addPoint(x, (int) y);
                }
            }

            g2.drawPolyline(polygon.xpoints, polygon.ypoints, polygon.npoints);
        }

        /*
        for (Point point : points){
            g2.setColor(point.color);
            g2.fillOval((int) (origin[0] + point.x*zoomX-(point.thickness/2)), (int) (origin[1] - point.y*zoomY-(point.thickness/2)), point.thickness, point.thickness);
            if (coordinatesVisible)
                g2.drawString("("+point.x+", "+point.y+")", (int) (origin[0] + point.x*zoomX)+point.thickness, (int) (origin[1] - point.y*zoomY)-point.thickness/2);
        }
         */
    }
    
    public void setFunction(String name, Evaluable<Double, Double> function){
        setFunction(name, function, Color.BLACK);
    }

    public void setFunction(String name, Evaluable<Double, Double> function, Color c){
        if (c == null) c = Color.BLACK;
        functions.put(name, new EvaluableAdapter(function, c));
        onChange();
    }

    public Evaluable<Double, Double> getFunction(String name){
        return functions.get(name);
    }

    public Evaluable<Double, Double> removeFunction(String name){
        Evaluable<Double, Double> f = functions.remove(name);
        onChange();
        return f;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoomX += (double) e.getUnitsToScroll()/2*-10;
        zoomY += (double) e.getUnitsToScroll()/2*-10;

        if (!(zoomX <= 0) && !(zoomY <= 0) && !(zoomX >= 1000) && !(zoomY >= 1000)) {
            getGraphics().clearRect(0, 0, getWidth(), getHeight());
            repaint();
        }
        else{
            zoomX += (double) e.getUnitsToScroll()/2*10;
            zoomY += (double) e.getUnitsToScroll()/2*10;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        double max = Math.max(getWidth(), getHeight());
        double min = Math.min(getWidth(), getHeight());
        double unitScreenWidth = min + ((max - min) / 2);

        if (e.getX()-cursorPosition[0] > 0) {
            double dx = unitScreenWidth / zoomX / 1000;
            bias[0] += dx*SENSITIVITY;
        }
        else if (e.getX()-cursorPosition[0] < 0) {
            double dx = unitScreenWidth / zoomX / 1000;
            bias[0] -= dx*SENSITIVITY;
        }

        if (e.getY()-cursorPosition[1] > 0) {
            double dy = unitScreenWidth / zoomY / 1000;
            bias[1] += dy*SENSITIVITY;
        }
        else if (e.getY()-cursorPosition[1] < 0) {
            double dy = unitScreenWidth / zoomY / 1000;
            bias[1] -= dy*SENSITIVITY;
        }

        cursorPosition = new double[]{e.getX(), e.getY()};

        onChange();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        cursorPosition[0] = e.getX();
        cursorPosition[1] = e.getY();
    }

    private double[] transformBase(double[] p){
        return new double[]{
                base[0][0] * p[0] + base[1][0] * p[1],
                base[0][1] * p[0] + base[1][1] * p[1]
        };
    }

    private double[] transformBase(double x, double y){
        return transformBase(new double[]{x, y});
    }


    private static class EvaluableAdapter implements Functional {
        private final Evaluable<Double, Double> original;
        private Color color;
        public EvaluableAdapter(Evaluable<Double, Double> f, Color c) {
            this.original = f;
            this.color = c;
        }
        public void setColor(Color color) {
            this.color = color;
        }
        public Color getColor() {
            return color;
        }
        public Evaluable<Double, Double> getOriginal() {
            return original;
        }
        public Double eval(Double... input) {
            return original.eval(input);
        }
    }

}
