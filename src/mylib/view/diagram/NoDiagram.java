package mylib.view.diagram;

import java.awt.*;

public final class NoDiagram extends Diagram {

    private static NoDiagram instance;

    private NoDiagram(){
    }

    public static NoDiagram getInstance() {
        if (instance == null)
            instance = new NoDiagram();
        return instance;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawString("no diagram given", getWidth()/2, getHeight()/2);
    }
}
