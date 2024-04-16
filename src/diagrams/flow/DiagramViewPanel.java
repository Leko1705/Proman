package diagrams.flow;

import diagrams.flow.graph.node.*;
import diagrams.utils.*;
import graph.Edge;
import graph.Node;

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
        return new Transition((BaseNode<?, ?>) from, (BaseNode<?, ?>) to);
    }

    private void addComponent(BaseNode<?, ?> node, MouseEvent e) {
        node.getView().setLocation(e.getX(), e.getY());
        addNode(node);
    }


    private class RightClickPopUpMenu extends JPopupMenu {

        public RightClickPopUpMenu(MouseEvent mouseEvent) {
            JMenu addMenu = new JMenu("add");

            JMenuItem startItem = new JMenuItem("start/end");
            startItem.addActionListener(e -> addComponent(new StartNode(), mouseEvent));
            addMenu.add(startItem);

            JMenuItem processItem = new JMenuItem("process");
            processItem.addActionListener(e -> addComponent(new ProcessNode(), mouseEvent));
            addMenu.add(processItem);

            JMenuItem subProcessItem = new JMenuItem("sub process");
            subProcessItem.addActionListener(e -> addComponent(new SubProcessNode(), mouseEvent));
            addMenu.add(subProcessItem);

            JMenuItem branchItem = new JMenuItem("branch");
            branchItem.addActionListener(e -> {
                BranchNode branchNode = new BranchNode();
                addComponent(branchNode, mouseEvent);
                addNode(branchNode.getConnector(SwingConstants.WEST));
                addNode(branchNode.getConnector(SwingConstants.EAST));
                addNode(branchNode.getConnector(SwingConstants.SOUTH));
                addNode(branchNode.getConnector(SwingConstants.NORTH));
                branchNode.onMove();
            });
            addMenu.add(branchItem);

            JMenuItem inputItem = new JMenuItem("input/output");
            inputItem.addActionListener(e -> addComponent(new InputNode(), mouseEvent));
            addMenu.add(inputItem);

            add(addMenu);
        }
    }


    private class NodePopUpMenu extends JPopupMenu {

        public NodePopUpMenu(BaseNode<?, ?> node) {

            if (!(node instanceof BranchNode branchNode)) {
                JMenuItem connectItem = new JMenuItem("connect");
                connectItem.addActionListener(e -> startConnectionProcess(new Transition(node, MOUSE_POINT_END)));
                add(connectItem);
            }
            else {
                JMenu connectMenu = new JMenu("connect");

                JMenuItem westItem = new JMenuItem("west");
                westItem.addActionListener(e -> startConnectionProcess(new Transition(branchNode.getConnector(BranchNode.WEST), MOUSE_POINT_END)));
                connectMenu.add(westItem);

                JMenuItem eastItem = new JMenuItem("east");
                eastItem.addActionListener(e -> startConnectionProcess(new Transition(branchNode.getConnector(BranchNode.EAST), MOUSE_POINT_END)));
                connectMenu.add(eastItem);

                JMenuItem northItem = new JMenuItem("north");
                northItem.addActionListener(e -> startConnectionProcess(new Transition(branchNode.getConnector(BranchNode.NORTH), MOUSE_POINT_END)));
                connectMenu.add(northItem);

                JMenuItem southItem = new JMenuItem("south");
                southItem.addActionListener(e -> startConnectionProcess(new Transition(branchNode.getConnector(BranchNode.SOUTH), MOUSE_POINT_END)));
                connectMenu.add(southItem);

                add(connectMenu);
            }

            JMenuItem removeItem = new JMenuItem("remove");
            removeItem.addActionListener(e -> {
                removeNode(node);
                if (node instanceof BranchNode branchNode){
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
            JMenuItem setTextItem = new JMenuItem("set Text");
            setTextItem.addActionListener(e -> {
                String newText = JOptionPane.showInputDialog(SwingUtilities.getWindowAncestor(this), edge.getText(), "Set Transition Text", JOptionPane.PLAIN_MESSAGE);
                if (newText != null) {
                    edge.setText(newText);
                    DiagramViewPanel.this.repaint();
                }
            });
            add(setTextItem);

            JMenuItem removeItem = new JMenuItem("remove");
            removeItem.addActionListener(e -> removeEdge(edge));
            add(removeItem);
        }
    }

}
