package mylib.view.diagram;

import mylib.view.struct.DataValue;
import mylib.view.struct.GraphDataSet;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GraphDiagram extends Diagram {

    private final GraphDataSet<?> dataSet;

    public GraphDiagram(GraphDataSet<?> dataSet){
        dataSet.addPropertyChangeListener(this);
        this.dataSet = dataSet;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (dataSet.isEmpty()) return;

        int radius = relativeRadius(20);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int angleStepSize = 360 / dataSet.size();
        int currentAngle = 0;
        HashMap<Object, Integer> angleAlignment = new HashMap<>();

        for (DataValue<?> dataValue : dataSet) {
            angleAlignment.put(dataValue.getElement(), currentAngle);
            currentAngle += angleStepSize;
        }

        HashMap<Object, List<Object>> connectionPaintings = new HashMap<>();
        for (DataValue<?> dataValue : dataSet){
            Object from = dataValue.getElement();
            HashMap<?, Double> neighbours = dataSet.getConnections(from);

            for (Object to : neighbours.keySet()){

                if (connectionPaintings.containsKey(from)
                        && connectionPaintings.get(from).contains(to)) continue;

                double fromDeg = Math.toRadians(angleAlignment.get(from));
                double toDeg = Math.toRadians(angleAlignment.get(to));

                int fromX = (int) (centerX + Math.cos(fromDeg) * radius);
                int fromY = (int) (centerY + Math.sin(fromDeg) * radius);
                int toX = (int) (centerX + Math.cos(toDeg) * radius);
                int toY = (int) (centerY + Math.sin(toDeg) * radius);

                g.drawLine(fromX, fromY, toX, toY);

                int wightCenterX = rangeCenter(Math.max(fromX, toX), Math.min(fromX, toX));
                int wightCenterY = rangeCenter(Math.max(fromY, toY), Math.min(fromY, toY));

                // TODO fix painting wight
                //g.drawString("test", wightCenterX, wightCenterY);

                if (connectionPaintings.containsKey(from))
                    connectionPaintings.get(from).add(to);
                else
                    connectionPaintings.put(from, new LinkedList<>(List.of(to)));
            }

        }
        Graphics2D g2 = (Graphics2D) g;


        for (DataValue<?> dataValue : dataSet) {
            Object element = dataValue.getElement();
            double degrees = Math.toRadians(angleAlignment.get(element));

            DataValue<?> style = dataSet.getDataValueObj(element);

            Color defaultColor = g2.getColor();
            if (style.getColor() != null) {
                defaultColor = g2.getColor();
                g2.setColor(style.getColor());
            } else
                g2.setColor(getBackground());

            int x = (int) (centerX + Math.cos(degrees) * radius);
            int y = (int) (centerY + Math.sin(degrees) * radius);

            int vertexRadius = Math.max(getWidth(), getHeight()) / 100 * 3;
            g2.fillOval(x-vertexRadius/2, y-vertexRadius/2, vertexRadius, vertexRadius);
            g2.setColor(defaultColor);
            g2.drawOval(x-vertexRadius/2, y-vertexRadius/2, vertexRadius, vertexRadius);

            String desc = style.getDesc();
            if (desc == null)
                desc = element.toString();

            int width = g2.getFontMetrics().stringWidth(desc);
            g2.drawString(desc, x-width/2+1, y+5);

        }

    }

    private int relativeRadius(int percent){
        return getWidth() / 100 * percent;
    }

    private int rangeCenter(int l, int r){
        return l + (l - r) / 2;
    }

}
