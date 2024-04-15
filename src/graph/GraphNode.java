package graph;

import mvc.IView;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;

public class GraphNode
        extends JInternalFrame
        implements Node<GraphNode, GraphNode>, NodeModel, IView {

    private long id;

    private final GraphPanel graphPanel;

    public GraphNode(String title, GraphPanel graphPanel){
        super(title, true, false, true, false);
        this.graphPanel = graphPanel;
        setVisible(true);
        setContentPane(graphPanel);
    }

    public GraphPanel getGraphPanel() {
        return graphPanel;
    }

    @Override
    public GraphNode getModel() {
        return this;
    }

    @Override
    public GraphNode getView(){
        return this;
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public void setID(long id) {
        this.id = id;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
        if (getNorthPane() != null)
            getNorthPane().addMouseListener(l);
    }

    @Override
    public synchronized void addMouseMotionListener(MouseMotionListener l) {
        super.addMouseMotionListener(l);
        if (getNorthPane() != null)
            getNorthPane().addMouseMotionListener(l);
    }

    @Override
    public synchronized void addMouseWheelListener(MouseWheelListener l) {
        super.addMouseWheelListener(l);
        if (getNorthPane() != null)
            getNorthPane().addMouseWheelListener(l);
    }

    @Override
    public synchronized void addComponentListener(ComponentListener l) {
        super.addComponentListener(l);
        if (getNorthPane() != null)
            getNorthPane().addComponentListener(l);
    }

    @Override
    public synchronized void addFocusListener(FocusListener l) {
        super.addFocusListener(l);
        if (getNorthPane() != null)
            getNorthPane().addFocusListener(l);
    }

    @Override
    public synchronized void addKeyListener(KeyListener l) {
        super.addKeyListener(l);
        if (getNorthPane() != null)
            getNorthPane().addKeyListener(l);
    }

    private JComponent getNorthPane(){
        return ((BasicInternalFrameUI) getUI()).getNorthPane();
    }


    public void addNode(Node<?, ?> node){
        graphPanel.addNode(node);
    }

    public void removeNode(Node<?, ?> node){
        graphPanel.removeNode(node);
    }

    public void addEdge(Edge<?> edge){
        graphPanel.addEdge(edge);
    }

    public void removeEdge(Edge<?> edge){
        graphPanel.removeEdge(edge);
    }

    public Collection<Node<?, ?>> getNodes(){
        return graphPanel.getNodes();
    }

    public Collection<Edge<?>> getEdges(){
        return graphPanel.getEdges();
    }

}
