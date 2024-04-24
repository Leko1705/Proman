package diagrams.entityRel;

import diagrams.entityRel.graph.node.AttributeNode;
import diagrams.entityRel.graph.node.EntityNode;
import diagrams.utils.BaseNode;
import diagrams.utils.GraphEditorPanel;
import diagrams.utils.RhombusNode;
import diagrams.utils.Transition;
import graph.Edge;
import graph.Node;
import graph.PlainLine;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class DiagramViewPanel extends GraphEditorPanel {

    @Override
    public void onDiagramRightClick(MouseEvent e) {
        new RightClickPopUpMenu(e).show(e.getComponent(), e.getX(), e.getY());
    }

    @Override
    public void onNodeRightClicked(Node<?, ?> node, MouseEvent e) {
        new NodePopUpMenu((BaseNode<?, ?>) node).show(e.getComponent(), e.getX(), e.getY());
    }

    @Override
    public void onEdgeRightClicked(Edge<?> edge, MouseEvent e) {
        new EdgePopUpMenu((Transition) edge).show(e.getComponent(), e.getX(), e.getY());
    }

    @Override
    public Edge<?> onNewConnectionEstablished(Node<?, ?> from, Node<?, ?> to) {
        return new Transition((BaseNode<?, ?>) from, (BaseNode<?, ?>) to, new PlainLine());
    }


    private class RightClickPopUpMenu extends JPopupMenu {

        public RightClickPopUpMenu(MouseEvent mouseEvent) {
            JMenu addMenu = new JMenu("add");

            JMenuItem entityItem = new JMenuItem("entity");
            entityItem.addActionListener(e -> addComponent(new EntityNode(), mouseEvent));
            addMenu.add(entityItem);

            JMenuItem attributeItem = new JMenuItem("attribute");
            attributeItem.addActionListener(e -> addComponent(new AttributeNode(), mouseEvent));
            addMenu.add(attributeItem);

            JMenuItem relationshipItem = new JMenuItem("relationship");
            relationshipItem.addActionListener(e -> {
                RhombusNode branchNode = new RhombusNode();
                addComponent(branchNode, mouseEvent);
                addNode(branchNode.getConnector(SwingConstants.WEST));
                addNode(branchNode.getConnector(SwingConstants.EAST));
                addNode(branchNode.getConnector(SwingConstants.SOUTH));
                addNode(branchNode.getConnector(SwingConstants.NORTH));
                branchNode.onMove();
            });
            addMenu.add(relationshipItem);

            add(addMenu);
        }
    }


    private void addComponent(BaseNode<?, ?> node, MouseEvent e) {
        node.getView().setLocation(e.getX(), e.getY());
        addNode(node);
    }


    private class NodePopUpMenu extends JPopupMenu {

        public NodePopUpMenu(BaseNode<?, ?> node) {

                JMenuItem connectItem = new JMenuItem("connect");
                connectItem.addActionListener(e -> startConnectionProcess(new Transition(node, MOUSE_POINT_END, new PlainLine())));
                add(connectItem);

            JMenuItem removeItem = new JMenuItem("remove");
            removeItem.addActionListener(e -> {
                removeNode(node);
                if (node instanceof RhombusNode branchNode){
                    removeNode(branchNode.getConnector(SwingConstants.WEST));
                    removeNode(branchNode.getConnector(SwingConstants.EAST));
                    removeNode(branchNode.getConnector(SwingConstants.SOUTH));
                    removeNode(branchNode.getConnector(SwingConstants.NORTH));
                }
            });
            add(removeItem);
        }
    }


    private class EdgePopUpMenu extends JPopupMenu {

        public EdgePopUpMenu(Transition edge) {
            JMenuItem removeItem = new JMenuItem("remove");
            removeItem.addActionListener(e -> removeEdge(edge));
            add(removeItem);
        }
    }

}
