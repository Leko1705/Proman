package diagrams.state.graph.node;

import diagrams.state.StateDiagramConstants;
import diagrams.utils.BaseNode;
import graph.TextNode;
import utils.GeomUtils;

import java.awt.*;

public abstract class RoundBaseNode
        extends TextNode
        implements BaseNode<TextNode, TextNode> {

    public RoundBaseNode(){
        setLayout(new BorderLayout());
        setSize(StateDiagramConstants.CIRCLE_COMPONENT_SIZE, StateDiagramConstants.CIRCLE_COMPONENT_SIZE);
        setForeground(Color.WHITE);
        setDisabledTextColor(Color.LIGHT_GRAY);
        setCaretColor(Color.WHITE);
        super.setOpaque(false);
        setBorder(javax.swing.BorderFactory.createEmptyBorder());
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }

    @Override
    public Point getNearestPointOnOutline(Point q) {
        Point p = GeomUtils.getCenter(getBounds());
        double alpha = GeomUtils.calcAngle(p, q);
        return GeomUtils.calculatePointOnCircle(p, alpha, (double) getWidth() /2);
    }
}
