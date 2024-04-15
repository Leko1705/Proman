package graph;

import mvc.IView;

import javax.swing.*;
import java.awt.*;

public class PointNode
        extends JComponent
        implements Node<PointNode, PointNode>, NodeModel, IView {

    private long id;

    @Override
    public long getID() {
        return id;
    }

    @Override
    public void setID(long id) {
        this.id = id;
    }

    @Override
    public void paintComponent(Graphics g) { }

    @Override
    public PointNode getModel() {
        return this;
    }

    @Override
    public PointNode getView() {
        return this;
    }
}
