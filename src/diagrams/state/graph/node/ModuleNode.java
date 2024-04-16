package diagrams.state.graph.node;

import diagrams.state.DiagramViewPanel;
import diagrams.utils.BaseNode;
import graph.GraphNode;
import utils.GeomUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.*;
import java.awt.geom.Line2D;

public class ModuleNode
        extends GraphNode
        implements BaseNode<GraphNode, GraphNode> {


    public ModuleNode(String title) {
        super(title, new DiagramViewPanel());
    }

    @Override
    public ModuleNode getView() {
        return this;
    }

    @Override
    public Point getNearestPointOnOutline(Point q) {
        Rectangle r = getBounds();
        Point p = GeomUtils.getCenter(r);
        return GeomUtils.lineIntersectionOnRect(r, new Line2D.Double(p, q));
    }

    public JComponent getNorthPane(){
        return ((BasicInternalFrameUI) getUI()).getNorthPane();
    }

}
