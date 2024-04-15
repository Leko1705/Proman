package mylib.view.diagram;

import mylib.view.struct.DataSet;
import mylib.view.struct.DataValue;

import java.awt.*;

public class BarChart extends Diagram {

    private final DataSet<Integer, Double> dataSet;

    private double max = 0.0;

    private final Double threshold;

    public BarChart(DataSet<Integer, Double> dataSet, Double threshold){
        this.dataSet = dataSet;
        this.threshold = threshold;
        dataSet.addPropertyChangeListener(this);
    }

    public BarChart(DataSet<Integer, Double> dataSet, double threshold){
        this.dataSet = dataSet;
        this.threshold = threshold;
        dataSet.addPropertyChangeListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.clearRect(0, 0, getWidth(), getHeight());

        g2.setStroke(new BasicStroke((float) (getHeight()/100)));

        paintBars(g2);
        paintSkeleton(g2);
    }

    private void paintBars(Graphics2D g2){

        final int xAxisLength = getWidth()-getWidth()/40;

        int barWidth = xAxisLength / (dataSet.size()+2);
        int space = barWidth;
        barWidth /= 2;
        if (space == 0) space = 1;
        if (barWidth == 0) barWidth = 1;

        int pos = getWidth()/20 + space;

        for (DataValue<Double> value : dataSet){
            if (value.getElement() > max) max = value.getElement();

            paintBar(g2, value, pos, barWidth);
            pos += space;
        }

    }

    private void paintSkeleton(Graphics2D g2){
        g2.drawLine(getWidth()/20, getHeight()-getHeight()/10, getWidth()-getWidth()/40, getHeight()-getHeight()/10);


        if (threshold != null){
            if (max < threshold)
                max = threshold;
        }

        double trimmedScreenHeight = (double) getHeight() / 100 * 80;
        double axisHeight = trimmedScreenHeight / 100 * max;

        g2.drawLine(
                getWidth()/20,
                (int) (getHeight() - getHeight() / 10 - axisHeight),
                getWidth()/20,
                getHeight()-getHeight()/10
        );

        String maxStr = Double.toString(max);
        g2.drawString(maxStr,
                getWidth()/20-g2.getFontMetrics().stringWidth(maxStr)-10,
                (int) (getHeight() - getHeight() / 10 - axisHeight));
    }

    private void paintBar(Graphics2D g2, DataValue<Double> value, int pos, int barWidth){
        double trimmedScreenHeight = (double) getHeight() / 100 * 80;
        double barHeight = trimmedScreenHeight / 100 * value.getElement();

        Color sekeletonColor = g2.getColor();
        g2.setColor(value.getColor());

        g2.fillRect(pos,
                (int) (getHeight() - getHeight() / 10 - barHeight),
                barWidth,
                (int) barHeight);

        g2.setColor(sekeletonColor);

        if (value.getDesc() != null){
            g2.drawString(
                    value.getDesc(),
                    pos,
                    getHeight() - getHeight() / 10 + 20
            );
        }
    }

}
