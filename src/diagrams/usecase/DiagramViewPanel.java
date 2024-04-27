package diagrams.usecase;

import diagrams.utils.BaseNode;
import diagrams.utils.GraphEditorPanel;
import diagrams.utils.ImageNode;
import diagrams.utils.Transition;
import graph.Edge;
import graph.Node;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.nio.file.Path;

public class DiagramViewPanel extends GraphEditorPanel {

    @Override
    public void onDiagramRightClick(MouseEvent e) {
        new DiagramViewPanel.RightClickPopUpMenu(e).show(e.getComponent(), e.getX(), e.getY());
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

            JMenuItem usecaseItem = new JMenuItem("usecase");
            usecaseItem.addActionListener(e -> addComponent(new UseCaseNode(), mouseEvent));
            addMenu.add(usecaseItem);

            JMenuItem actorItem = new JMenuItem("actor");
            actorItem.addActionListener(e -> {
                BaseNode<?, ?> node = new ImageNode(Path.of("images/usecase_diagram_icon.png"), 80, 100, "actor");
                addComponent(node, mouseEvent);
                node.getView().revalidate();
            });
            addMenu.add(actorItem);

            add(addMenu);
        }
    }

    private class NodePopUpMenu extends JPopupMenu {

        public NodePopUpMenu(BaseNode<?, ?> node) {

            JMenuItem connectItem = new JMenuItem("connect");
            connectItem.addActionListener(e -> startConnectionProcess(new Transition(node, MOUSE_POINT_END)));
            add(connectItem);

            JMenuItem removeItem = new JMenuItem("remove");
            removeItem.addActionListener(e -> removeNode(node));
            add(removeItem);
        }
    }

    private class EdgePopUpMenu extends JPopupMenu {

        public EdgePopUpMenu(Transition edge) {

            JMenu textMenu = new JMenu("text");

            JMenuItem includeItem = new JMenuItem("include");
            includeItem.addActionListener(e -> edge.setText("<<includes>>"));
            textMenu.add(includeItem);

            JMenuItem extendItem = new JMenuItem("extend");
            extendItem.addActionListener(e -> edge.setText("<<extends>>"));
            textMenu.add(extendItem);

            JMenuItem customItem = new JMenuItem("custom");
            customItem.addActionListener(e -> {
                String newText = JOptionPane.showInputDialog(SwingUtilities.getWindowAncestor(this), edge.getText(), "Set Transition Text", JOptionPane.PLAIN_MESSAGE);
                if (newText != null) {
                    edge.setText(newText);
                    DiagramViewPanel.this.repaint();
                }
            });
            textMenu.add(customItem);

            if (!edge.getText().isEmpty()) {
                JMenuItem removeTextItem = new JMenuItem("remove");
                removeTextItem.addActionListener(e -> edge.setText(""));
                textMenu.add(removeTextItem);
            }

            add(textMenu);

            JMenuItem removeItem = new JMenuItem("remove");
            removeItem.addActionListener(e -> removeEdge(edge));
            add(removeItem);
        }
    }

}
