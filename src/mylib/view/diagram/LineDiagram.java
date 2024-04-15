package mylib.view.diagram;

import mylib.view.struct.DataSet;
import mylib.view.struct.DataValue;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class LineDiagram extends Diagram {

    private final DataSet<Integer, Double> dataSet;
    private double zoom = 120;

    private double bias = 0;

    private int lastMouseXPosition = 0;

    public LineDiagram(DataSet<Integer, Double> dataSet){
        dataSet.addPropertyChangeListener(this);
        this.dataSet = dataSet;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.clearRect(0, 0, getWidth(), getHeight());

        g2.setStroke(new BasicStroke((float) (zoom/20)));
        paintLine(g2);
        paintStamps(g2);
    }

    private void paintLine(Graphics2D g2){
        g2.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
    }

    private void paintStamps(Graphics2D g2){
        paintSingleStamp(g2, new DataValue<>(0., "0", g2.getColor()), true); // paint the zero stamp

        g2.setColor(Color.RED);
        boolean descUpperPlaces = false;

        for (DataValue<Double> value : dataSet){
            paintSingleStamp(g2, value, descUpperPlaces);
            descUpperPlaces = !descUpperPlaces;
        }
    }

    private void paintSingleStamp(Graphics2D g2, DataValue<Double> value, boolean upper){
        int xPos = (int) calculatePosition(value.getElement());
        int plainPositionY = getHeight()/2;
        g2.drawLine(xPos,  plainPositionY - 10, xPos, plainPositionY + 10);

        if (value.getDesc() != null) {
            int stringWidth = g2.getFontMetrics().stringWidth(value.getDesc());

            g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
            if (upper)
                g2.drawString(value.getDesc(), xPos - (float) stringWidth / 2, plainPositionY - 25);
            else
                g2.drawString(value.getDesc(), xPos - (float) stringWidth / 2, plainPositionY + 30);

        }
    }

    private double calculatePosition(double val){
        val *= 20;
        double center = (double) getWidth() / 2;
        double offset = ((zoom*val)+(bias/40*zoom)) / 20;
        return center + offset;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoom += e.getWheelRotation();
        if (zoom > 200) zoom = 200;
        else if (zoom < 1) zoom = 1;
        onChange();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if (lastMouseXPosition > e.getX()) {
            bias += 130;
        }
        else if (lastMouseXPosition < e.getX()) {
            bias -= 130;
        }

        lastMouseXPosition = e.getX();
        onChange();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        lastMouseXPosition = e.getXOnScreen();
    }

}
