package graph;

import mvc.Controller;
import mvc.IView;
import mylib.swingx.JDragPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Path2D;
import java.util.*;

public class GraphPanel
        extends JDragPanel
        implements IView {

    private static long nextFreeID = Long.MIN_VALUE;


    private final Map<Long, Node<?, ?>> nodes = new HashMap<>();

    private final Set<Edge<?>> edges = new HashSet<>();

    private final Set<EdgeClickListener> edgeClickListeners = new HashSet<>();


    public GraphPanel(){
        super(true);
        setOpaque(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Edge<?> edge : edges)
                    handlePotentialLineClick(edge, e);
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
        NodeModel model = node.getModel();
        model.setID(nextFreeID++);
        nodes.put(model.getID(), node);
        Component view = node.getView();

        view.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                repaint();
            }
        });

        add(view);
        repaint();
    }

    public void removeNode(Node<?, ?> node){
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

    public Collection<Node<?, ?>> getNodes(){
        return nodes.values();
    }

    public void addEdge(Edge<?> edge){
        edges.add(edge);
        repaint();
    }

    public void removeEdge(Edge<?> edge){
        edges.remove(edge);
        repaint();
    }

    public Collection<Edge<?>> getEdges(){
        return edges;
    }

}
