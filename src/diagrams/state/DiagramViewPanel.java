package diagrams.state;

import diagrams.state.graph.edge.Transition;
import diagrams.state.graph.node.*;
import graph.Edge;
import graph.GraphPanel;
import graph.PointNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DiagramViewPanel extends GraphPanel {

    private final MousePointNode MOUSE_POINT_END = new MousePointNode();

    private Transition processedConnection = null;

    private boolean lineClicked = false;

    public DiagramViewPanel() {
        addNode(MOUSE_POINT_END);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (SwingUtilities.isRightMouseButton(e) && !lineClicked) {
                    new RightClickPopUpMenu(e).show(e.getComponent(), e.getX(), e.getY());
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
                new EdgePopUpMenu((Transition) edge).show(e.getComponent(), e.getX(), e.getY());
            }
        });
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

        private void addComponent(StateNode<?, ?> node, MouseEvent e) {
            node.getView().setLocation(e.getX(), e.getY());
            if (node instanceof JInternalFrame frame){
                frame.setSize(250, 200);
            }
            applyComponentSpecs(node);
            addNode(node);
        }

        private void applyComponentSpecs(StateNode<?, ?> node) {
            node.getView().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        new NodePopUpMenu(node).show(e.getComponent(), e.getX(), e.getY());
                    }
                    else {
                        if (processedConnection != null){
                            removeEdge(processedConnection);
                            Transition transition = new Transition(processedConnection.getFrom(), node);
                            addEdge(transition);
                            processedConnection = null;
                        }
                    }
                }
            });
        }
    }



    private class NodePopUpMenu extends JPopupMenu {

        public NodePopUpMenu(StateNode<?, ?> node) {
            JMenuItem connectItem = new JMenuItem("connect");
            connectItem.addActionListener(e -> {
                processedConnection = new Transition(node, MOUSE_POINT_END);
                addEdge(processedConnection);
            });
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


    private static class MousePointNode
            extends PointNode
            implements StateNode<PointNode, PointNode> {
        @Override
        public Point getNearestPointOnOutline(Point to) {
            return getLocation();
        }
    }

}
