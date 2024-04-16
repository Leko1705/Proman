package diagrams.utils;

import graph.Edge;
import graph.GraphPanel;
import graph.Node;
import graph.PointNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class GraphEditorPanel extends GraphPanel {

    protected final MousePointNode MOUSE_POINT_END = new MousePointNode();

    private Edge<?> processedConnection = null;


    private boolean lineClicked = false;


    public GraphEditorPanel() {
        addNode(MOUSE_POINT_END);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (SwingUtilities.isRightMouseButton(e) && !lineClicked) {
                    onDiagramRightClick(e);
                }

                if (processedConnection != null)
                    removeEdge(processedConnection);
                processedConnection = null;
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner();
                lineClicked = false;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                MOUSE_POINT_END.setLocation(e.getPoint());
                repaint();
            }
        });

        addEdgeClickListener((edge, e) -> {
            if (SwingUtilities.isRightMouseButton(e)){
                lineClicked = true;
                onEdgeRightClicked(edge, e);
            }
        });
    }

    public abstract void onDiagramRightClick(MouseEvent e);

    public abstract void onNodeRightClicked(Node<?, ?> node, MouseEvent e);

    public abstract void onEdgeRightClicked(Edge<?> edge, MouseEvent e);

    public abstract Edge<?> onNewConnectionEstablished(Node<?, ?> from, Node<?, ?> to);


    public void startConnectionProcess(Edge<?> edge){
        processedConnection = edge;
        addEdge(edge);
    }

    @Override
    public void addNode(Node<?, ?> node) {
        applyComponentSpecs(node);
        super.addNode(node);
    }

    private void applyComponentSpecs(Node<?, ?> node) {
        node.getView().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    onNodeRightClicked(node, e);
                }
                else {
                    if (processedConnection != null){
                        removeEdge(processedConnection);
                        Node<?, ?> from = getNode(processedConnection.getModel().getFromID());
                        Edge<?> edge = onNewConnectionEstablished(from, node);
                        addEdge(edge);
                        processedConnection = null;
                    }
                }
            }
        });
    }


    public static class MousePointNode
            extends PointNode
            implements BaseNode<PointNode, PointNode> {
        @Override
        public Point getNearestPointOnOutline(Point to) {
            return getLocation();
        }
    }

}
