package mylib.view.struct;

import mylib.structs.Tree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class TreeDataSet<E> extends AbstractDataSet<E, E> {


    private final Tree<E> tree;

    private final HashMap<E, DataValue<E>> styleMap = new HashMap<>();

    public TreeDataSet(Tree<E> tree){
        Objects.requireNonNull(tree);
        this.tree = tree;
        for (E e : tree)
            styleMap.put(e, new DataValue<>(e, null, null));
    }


    @Override
    public int size() {
        return tree.size();
    }

    public boolean insert(E e){
        if (styleMap.containsKey(e)) return false;
        put(e, new DataValue<>(e, null, null));
        return true;
    }

    @Override
    public void put(E key, DataValue<E> dataValue) {
        tree.insert(key);
        styleMap.put(key, dataValue);
        firePropertyChange();
    }

    public boolean contains(E e){
        return getDataValue(e) != null;
    }

    public E search(E e){
        return tree.search(e);
    }

    public boolean remove(E e){
        boolean success = tree.remove(e);
        if (success){
            styleMap.remove(e);
            firePropertyChange();
        }
        return success;
    }

    @Override
    public DataValue<E> getDataValue(Object key) {
        return styleMap.get(key);
    }

    @Override
    public Iterator<DataValue<E>> iterator() {
        return styleMap.values().iterator();
    }

    public Tree<E> getTree() {
        return tree;
    }
}
