package diagrams.state;

import diagrams.utils.*;
import diagrams.state.graph.node.*;
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



    private class RightClickPopUpMenu extends JPopupMenu {

        public RightClickPopUpMenu(MouseEvent mouseEvent) {
            JMenu addMenu = new JMenu("add");

            JMenuItem startItem = new JMenuItem("start");
            startItem.addActionListener(e -> addComponent(new StartNode(), mouseEvent));
            addMenu.add(startItem);

            JMenuItem endItem = new JMenuItem("end");
            endItem.addActionListener(e -> addComponent(new EndNode(), mouseEvent));
            addMenu.add(endItem);

            JMenuItem stateItem = new JMenuItem("state");
            stateItem.addActionListener(e -> addComponent(new SimpleState(), mouseEvent));
            addMenu.add(stateItem);

            JMenuItem moduleItem = new JMenuItem("module");
            moduleItem.addActionListener(e -> addComponent(new ModuleNode("a Module"), mouseEvent));
            addMenu.add(moduleItem);

            JMenuItem parallelItem = new JMenuItem("parallel");
            parallelItem.addActionListener(e -> {
                JSpinner streamCountSelector = getStreamCountSelector();
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), streamCountSelector, "Select Number of Streams", JOptionPane.PLAIN_MESSAGE);
                addComponent(new ParallelState("a Parallel State", (int)streamCountSelector.getValue()), mouseEvent);
            });
            addMenu.add(parallelItem);

            add(addMenu);
        }

        private JSpinner getStreamCountSelector() {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(2, 2, 100, 1));
            JFormattedTextField textField = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
            textField.setHorizontalAlignment(JTextField.CENTER);
            return spinner;
        }

        private void addComponent(BaseNode<?, ?> node, MouseEvent e) {
            node.getView().setLocation(e.getX(), e.getY());
            if (node instanceof JInternalFrame frame){
                frame.setSize(250, 200);
            }
            addNode(node);
        }
    }



    private class NodePopUpMenu extends JPopupMenu {

        public NodePopUpMenu(BaseNode<?, ?> node) {
            JMenuItem connectItem = new JMenuItem("connect");
            connectItem.addActionListener(e -> startConnectionProcess(new Transition(node, MOUSE_POINT_END)));
            add(connectItem);

            if (node instanceof JInternalFrame frame){

                JMenuItem renameItem = new JMenuItem("rename");
                renameItem.addActionListener(e -> {
                    String newTitle = JOptionPane.showInputDialog(
                            SwingUtilities.getWindowAncestor(this),
                            null,
                            "Rename",
                            JOptionPane.PLAIN_MESSAGE);
                    if (newTitle != null)
                        frame.setTitle(newTitle);
                });
                add(renameItem);

                if (frame instanceof ParallelState parallelState){
                    JMenuItem addStreamItem = new JMenuItem("add stream");
                    addStreamItem.addActionListener(e -> parallelState.addStream());
                    add(addStreamItem);

                    JMenu alignmentMenu = new JMenu("alignment");

                    JMenuItem horizontalAlignment = new JMenuItem("horizontal");
                    horizontalAlignment.addActionListener(e -> parallelState.setAlignment(SwingConstants.HORIZONTAL));
                    alignmentMenu.add(horizontalAlignment);

                    JMenuItem verticalAlignment = new JMenuItem("vertical");
                    verticalAlignment.addActionListener(e -> parallelState.setAlignment(SwingConstants.VERTICAL));
                    alignmentMenu.add(verticalAlignment);

                    add(alignmentMenu);
                }
            }

            JMenuItem removeItem = new JMenuItem("remove");
            removeItem.addActionListener(e -> removeNode(node));
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
