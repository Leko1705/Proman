package graph;

import mvc.IView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.io.Serializable;

public interface EdgeView<M extends EdgeModel> extends IView, Serializable {

    Shape getEdgePath(Node<?, ?> from, Node<?, ?> to);

    EdgeModelPainter<M> getModelPainter(Node<?, ?> from, Node<?, ?> to);
    
    void paintEdge(Graphics g, Shape shape);

    @Override
    default void paintComponent(Graphics g){
        throw new UnsupportedOperationException("paintComponent");
    }
}
