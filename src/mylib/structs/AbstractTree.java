package mylib.structs;

import java.util.AbstractSet;
import java.util.Iterator;

public abstract class AbstractTree<E>
        extends AbstractSet<E>
        implements Tree<E> {

    public abstract  boolean insert(E e);

    public abstract E search(E e);

    public abstract boolean remove(Object o);

    public abstract int size();

    public abstract Iterator<E> iterator();

    @SuppressWarnings("unchecked")
    public boolean contains(Object o){
        return search((E) o) != null;
    }

    @Override
    public boolean isBalanced() {
        return Math.abs(balance()) <= 1;
    }

    @Override
    public boolean add(E e) {
        return insert(e);
    }

}
