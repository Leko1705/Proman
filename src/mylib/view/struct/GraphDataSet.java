package mylib.view.struct;

import mylib.structs.Graph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class GraphDataSet<E> extends AbstractDataSet<E, E> {

    private final HashMap<E, DataValue<E>> styleMap = new HashMap<>();
    private final Graph<E> graph;

    public GraphDataSet(Graph<E> graph){
        Objects.requireNonNull(graph);
        this.graph = graph;
        for (E e : graph)
            styleMap.put(e, new DataValue<>(e, null, null));
    }

    @Override
    public int size() {
        return graph.size();
    }

    @Override
    public void put(E key, DataValue<E> dataValue) {
        graph.add(key);
        styleMap.put(key, dataValue);
        firePropertyChange();
    }

    public boolean add(E e){
        boolean success = graph.add(e);
        if (success) {
            styleMap.put(e, new DataValue<>(e, e.toString(), null));
            firePropertyChange();
        }
        return success;
    }

    public void connect(E e1, E e2, double w){
        graph.connect(e1, e2, w);
        firePropertyChange();
    }

    public void disconnect(E e1, E e2){
        graph.disconnect(e1, e2);
        firePropertyChange();
    }

    public boolean contains(Object o){
        return graph.contains(o);
    }

    public boolean remove(Object o){
        return graph.remove(o);
    }

    public HashMap<E, Double> getConnections(Object e){
        return graph.getConnections(e);
    }

    @Override
    public DataValue<E> getDataValue(E key) {
        return styleMap.get(key);
    }

    public DataValue<E> getDataValueObj(Object o){
        return getDataValue((E) o);
    }

    @Override
    public Iterator<DataValue<E>> iterator() {
        return styleMap.values().iterator();
    }
}
