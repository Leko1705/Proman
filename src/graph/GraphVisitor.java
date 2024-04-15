package graph;

import java.util.Collection;

public interface GraphVisitor<P, R> {

    R visitNode(Node<?, ?> node, Collection<Edge<?>> edges, P p);

}
