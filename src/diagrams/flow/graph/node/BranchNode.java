package diagrams.flow.graph.node;

import diagrams.utils.BaseNode;
import graph.TextNode;
import utils.GeomUtils;

import javax.swing.*;

import static diagrams.utils.GraphEditorPanel.MousePointNode;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BranchNode
        extends TextNode
        implements BaseNode<TextNode, TextNode>, SwingConstants {

    private final BaseNode<?, ?>[] connectors = {
            new MousePointNode(),
            new MousePointNode(),
            new MousePointNode(),
            new MousePointNode(),
    };

    public BranchNode() {
        setSize(150, 100);
        setOpaque(false);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onMove();
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                onMove();
            }
        });
    }

    public void onMove(){
        getConnector(WEST).getView().setLocation(getX(), getY() + centerY());
        getConnector(EAST).getView().setLocation(getX() + getWidth(), getY() + centerY());
        getConnector(SOUTH).getView().setLocation(getX() + centerX(), getY() + getHeight());
        getConnector(NORTH).getView().setLocation(getX() + centerX(), getY());
    }

    @Override
    public void paintComponent(Graphics g) {

        int centerX = centerX();
        int centerY = centerY();

        Polygon polygon = new Polygon();
        polygon.addPoint(centerX, 0);
        polygon.addPoint(getWidth(), centerY);
        polygon.addPoint(centerX, getHeight());
        polygon.addPoint(0, centerY);
        polygon.addPoint(centerX, 0);

        Color c = g.getColor();
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(new Color(255, 218, 209));
        g2d.fill(polygon);

        g.setColor(Color.BLACK);
        g2d.draw(polygon);

        g.setColor(c);
        super.paintComponent(g);
    }

    public BaseNode<?, ?> getConnector(int which){
        if (which == WEST)
            return connectors[0];

        else if (which == EAST)
            return connectors[1];

        else if (which == SOUTH)
            return connectors[2];

        else if (which == NORTH)
            return connectors[3];

        else
            throw new IllegalArgumentException();
    }

    private int centerX(){
        return getWidth() / 2;
    }

    private int centerY(){
        return getHeight() / 2;
    }

    @Override
    public Point getNearestPointOnOutline(Point to) {

        double minDistance = Double.POSITIVE_INFINITY;
        Point point = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);


        for (BaseNode<?, ?> connector : connectors) {

            Point from = connector.getView().getLocation();
            double distance = GeomUtils.euclideanDistance(from, to);

            if (distance < minDistance) {
                minDistance = distance;
                point = from;
            }
        }

        return point;
    }
}
