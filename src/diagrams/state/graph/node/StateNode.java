package diagrams.state.graph.node;

import graph.Node;
import graph.NodeModel;
import mvc.IView;

import java.awt.*;

public interface StateNode<M extends NodeModel, V extends Component & IView>
        extends Node<M, V> {

    Point getNearestPointOnOutline(Point to);
}
