package graph;

import mvc.IView;
import mylib.swingx.JDragPanel;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.*;

public class GraphPanel
        extends JDragPanel
        implements IView {

    private static long nextFreeID = Long.MIN_VALUE;


    private final Map<Long, Node<?, ?>> nodes = new HashMap<>();

    private final Set<Edge<?>> edges = new HashSet<>();

    private final Set<EdgeClickListener> edgeClickListeners = new HashSet<>();

    public GraphPanel(){
        super(true, true);
        setOpaque(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Edge<?> edge : edges)
                    handlePotentialLineClick(edge, e);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                repaint();
            }
        });

    }

    private <M extends EdgeModel> Shape getEdgeShape(Edge<M> edge){
        EdgeView<M> view = edge.getView();
        M edgeModel = edge.getModel();
        Node<?, ?> from = nodes.get(edgeModel.getFromID());
        Node<?, ?> to = nodes.get(edgeModel.getToID());
        return view.getEdgePath(from, to);
    }

    private <M extends EdgeModel> void handlePotentialLineClick(Edge<M> edge, MouseEvent e){
        Shape path = getEdgeShape(edge);
        if (path != null && path.contains(e.getPoint()))
            onEdgeClicked(edge, e);
    }

    private <M extends EdgeModel> void onEdgeClicked(Edge<M> edge, MouseEvent e){
        for (EdgeClickListener listener : edgeClickListeners)
            listener.edgeClicked(edge, e);
    }

    public void addEdgeClickListener(EdgeClickListener listener){
        edgeClickListeners.add(listener);
    }

    public void removeEdgeClickListener(EdgeClickListener listener){
        edgeClickListeners.remove(listener);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintEdges(g);
    }

    private void paintEdges(Graphics g){
        for (Edge<?> edge : edges)
            paintEdge(g, edge);
    }

    private <M extends EdgeModel> void paintEdge(Graphics g, Edge<M> edge){
        Shape path = getEdgeShape(edge);
        EdgeView<M> view = edge.getView();
        M edgeModel = edge.getModel();
        view.paintEdge(g, path);
        Node<?, ?> from = nodes.get(edgeModel.getFromID());
        Node<?, ?> to = nodes.get(edgeModel.getToID());
        EdgeModelPainter<M> modelPainter = view.getModelPainter(from, to);
        if (modelPainter != null)
            modelPainter.paintModel(g, edgeModel);
    }


    public void addNode(Node<?, ?> node){
        addNode(node, true);
    }

    public void addNode(Node<?, ?> node, boolean generateID){
        if (node == null) return;
        NodeModel model = node.getModel();
        if (generateID) {
            while (nodes.containsKey(nextFreeID)) nextFreeID++;
            model.setID(nextFreeID++);
        }
        nodes.put(model.getID(), node);
        Component view = node.getView();

        view.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                repaint();
            }
        });

        add(view);
        repaint();
    }

    public void removeNode(Node<?, ?> node){
        if (node == null) return;
        long id = node.getModel().getID();
        nodes.remove(id);
        for (Edge<?> edge : edges.toArray(new Edge[0])){
            EdgeModel model = edge.getModel();
            if (id == model.getFromID() || id == model.getToID())
                edges.remove(edge);
        }
        remove(node.getView());
        repaint();
    }

    public void removeNode(long id){
        removeNode(nodes.get(id));
    }

    public Collection<Node<?, ?>> getNodes(){
        return nodes.values();
    }

    public void addEdge(Edge<?> edge){
        if (edge == null) return;
        edges.add(edge);
        repaint();
    }

    public void removeEdge(Edge<?> edge){
        if (edge == null) return;
        edges.remove(edge);
        repaint();
    }

    public Collection<Edge<?>> getEdges(){
        return edges;
    }

    public Node<?, ?> getNode(long from) {
        return nodes.get(from);
    }
}
