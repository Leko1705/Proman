package mylib.view.diagram;

import java.awt.*;

public class CircularProgressBar extends Diagram {

    private double progress;
    private double reference;

    public CircularProgressBar(double progress){
        this.progress = progress;
        this.reference = 100;
    }

    public CircularProgressBar(double progress, double reference){
        this.progress = progress;
        this.reference = reference;
    }

    public void setProgress(double progress) {
        this.progress = progress;
        repaint();
    }

    public void setReference(double reference) {
        this.reference = reference;
        repaint();
    }

    public double getProgress() {
        return progress;
    }

    public double getReference() {
        return reference;
    }

    public void increaseProgress(double d){
        progress += d;
        repaint();
    }

    public void increaseReference(double d){
        reference += d;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        paintProgressArea(g);
        paintUnused(g);
        paintProgressValue(g);
    }

    private void paintProgressArea(Graphics g){
        int radius = relativeRadius(20);

        int centerX = centerCycle(getWidth() / 2, radius);
        int centerY = centerCycle(getHeight() / 2, radius);

        int startAngle = 90;
        int arcAngle = calculateProgressAngle(progress);

        g.setColor(new Color(150, 0, 0));
        g.fillOval(centerX, centerY, radius, radius);
        g.setColor(Color.RED);
        g.fillArc(centerX, centerY, radius, radius, startAngle, arcAngle);
    }

    private void paintUnused(Graphics g){
        int radius = relativeRadius(16);

        int centerX = centerCycle(getWidth() / 2, radius);
        int centerY = centerCycle(getHeight() / 2, radius);

        g.setColor(getBackground());
        g.fillOval(centerX, centerY, radius, radius);
    }

    private void paintProgressValue(Graphics g){
        g.setColor(Color.BLACK);
        g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, getWidth()/40));
        String progressStr = Double.toString(progress);
        g.drawString(progressStr,
                centerText(getWidth() / 2, progressStr, g),
                getHeight() / 2 + (getFontMetrics(g.getFont()).getHeight() / 3));
    }

    private int centerCycle(int p, int r){
        return p - r / 2;
    }

    private int relativeRadius(int percent){
        return getWidth() / 100 * percent;
    }

    private int calculateProgressAngle(double progress){
        double unit = 360 / reference;
        int angle = (int) (progress * unit);
        return -angle;
    }

    private int centerText(int p, String text, Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        int width = g2.getFontMetrics().stringWidth(text);
        return p - width / 2;
    }

}
