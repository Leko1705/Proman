package diagrams.clazz;

import diagrams.clazz.graph.edge.*;
import diagrams.clazz.graph.node.*;
import graph.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DiagramViewPanel extends GraphPanel {

    private final PointNode MOUSE_POINT_END = new PointNode();


    public DiagramManager manager;

    private Node<?, ?> cached = null;

    private Connection processedConnection = null;

    private boolean lineClicked = false;

    private final EdgeChangeListener ON_EDGE_CHANGE = this::repaint;


    public DiagramViewPanel(){
        addNode(MOUSE_POINT_END);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (manager.getFocused() instanceof ClassNode c) c.setFocused(false);

                if (processedConnection != null)
                    removeEdge(processedConnection);
                processedConnection = null;

                if (SwingUtilities.isRightMouseButton(e)){
                    if (!lineClicked)
                        new PanelRightClickPopUpMenu(e.getPoint()).show(e.getComponent(), e.getX(), e.getY());
                }
                else if (!lineClicked){
                    manager.setFocused(null);
                    manager.foldInProperties();
                }
                lineClicked = false;
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
            if (processedConnection != null) return;
            manager.setFocused(null);
            if (SwingUtilities.isRightMouseButton(e)){
                new EdgePopUpMenu((Connection) edge).show(e.getComponent(), e.getX(), e.getY());
            }
            else {
                manager.setSideBar(new EdgePropertyPanel((Connection) edge, manager));
                manager.foldOutProperties();
            }
            lineClicked = true;
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        manager.writeDataToFile();
    }

    private class PanelRightClickPopUpMenu extends JPopupMenu {

        private final Point mousePos;

        public PanelRightClickPopUpMenu(Point mousePos){
            this.mousePos = mousePos;

            JMenu addMenu = new JMenu("add");

            JMenuItem addClass = new JMenuItem("Class");
            addClass.addActionListener( e -> {
                ClassNode classNode = createClassNode(ClassType.CLASS, "Untitled");
                classNode.setFocused(true);
                addNewComponent(classNode);
                manager.setSideBar(new ClassNodePropertyPanel(classNode));
            });
            addMenu.add(addClass);

            add(addMenu);
        }



        private void addNewComponent(Node<?, ?> node){
            Point location = mousePos.getLocation();
            location.translate(-(node.getView().getWidth()/2), -node.getView().getHeight()/2);
            node.getView().setLocation(location);
            addNode(node);
            manager.setFocused(node);
            manager.foldOutProperties();
        }

    }


    private ClassNode createClassNode(ClassType type, String name){
        ClassNode classNode = new ClassNode(type, name);

        classNode.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (processedConnection == null) {
                    if (SwingUtilities.isRightMouseButton(e)){
                        new NodePopUpMenu(classNode).show(e.getComponent(), e.getX(), e.getY());
                    }
                    else {
                        focusAndRepaint();
                    }
                }
                else {

                    if (edgeExist(processedConnection.getFrom(), classNode)) {
                        removeEdge(processedConnection);
                        processedConnection = null;
                        return;
                    }

                    Connection connection = new Connection(processedConnection.getFrom(), classNode, processedConnection.getStyle());
                    addEdge(connection);
                    SwingUtilities.invokeLater(() -> {
                        manager.setSideBar(new EdgePropertyPanel(connection, manager));
                        manager.foldOutProperties();
                    });
                    connection.addEdgeChangeListener(ON_EDGE_CHANGE);
                    removeEdge(processedConnection);
                    processedConnection = null;
                }
            }

            private boolean edgeExist(Node<?, ?> from, Node<?, ?> to){
                for (Edge<?> edge : getEdges()) {
                    if (edge.getModel().getFromID() == from.getModel().getID()
                            && edge.getModel().getToID() == to.getModel().getID()) {
                        return true;
                    }
                }
                return false;
            }

            @Override public void mouseDragged(MouseEvent e) {
                focusAndRepaint();
            }
            @Override public void mousePressed(MouseEvent e) {
                focusAndRepaint();
            }

            private void focusAndRepaint(){
                if (manager.getFocused() instanceof ClassNode c) c.setFocused(false);

                if (processedConnection == null) {
                    classNode.setFocused(true);
                    manager.setFocused(classNode);
                    manager.setSideBar(new ClassNodePropertyPanel(classNode));
                    manager.foldOutProperties();
                    classNode.repaint();
                }
            }
        });

        classNode.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control C"), "copy");
        classNode.getActionMap().put("copy", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (manager.getFocused() != null)
                    cached = manager.getFocused();
            }
        });

        classNode.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control V"), "paste");
        classNode.getActionMap().put("paste", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cached == null) return;
                if (cached instanceof ClassNode given){
                    ClassNode clonedNode = cloneNode(given);
                    clonedNode.setLocation(
                            given.getX() + given.getWidth() / 2,
                            given.getY() + given.getHeight() / 2);
                    addNode(clonedNode);
                }
            }

        });

        return classNode;
    }


    private ClassNode cloneNode(ClassNode given){
        ClassNode newNode = createClassNode(given.getClassType(), given.getClassName());

        for (Attribute givenAtt : given.getAttributes()){
            Attribute newAttribute =
                    new Attribute(givenAtt.getVisibility(), givenAtt.getType(), givenAtt.getName(), givenAtt.isStatic());
            newNode.getAttributes().add(newAttribute);
        }
        for (Method givenMethod : given.getMethods()){
            Method newMethod = new Method(givenMethod.getVisibility(), givenMethod.getName(), givenMethod.getType(), givenMethod.isStatic(), givenMethod.isAbstract());
            for (Parameter givenParam : givenMethod.getParameters()){
                Parameter newParam = new Parameter(givenParam.getType(), givenParam.getName());
                newMethod.getParameters().add(newParam);
            }
            newNode.getMethods().add(newMethod);
        }

        newNode.setLocation(given.getLocation());
        return newNode;
    }


    private class NodePopUpMenu extends JPopupMenu {

        public NodePopUpMenu(Node<?, ?> node){

            JMenuItem connectItem = new JMenuItem("connect");
            connectItem.addActionListener(e -> {
                processedConnection = new Connection(node, MOUSE_POINT_END, new PlainLine());
                addEdge(processedConnection);
                manager.foldInProperties();
            });
            add(connectItem);

            JMenuItem copyItem = new JMenuItem("copy");
            copyItem.addActionListener(e -> cached = node);
            add(copyItem);

            JMenuItem removeItem = new JMenuItem("remove");
            removeItem.addActionListener(e -> {
                removeNode(node);
                manager.foldInProperties();
            });
            add(removeItem);
        }

    }

    private class EdgePopUpMenu extends JPopupMenu {

        public EdgePopUpMenu(Connection connection){
            JMenuItem removeItem = new JMenuItem("remove");
            removeItem.addActionListener(e -> {
                removeEdge(connection);
                manager.foldInProperties();
            });
            add(removeItem);
        }

    }

}
