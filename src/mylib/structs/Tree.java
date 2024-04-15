package mylib.structs;

import java.util.Collection;

public interface Tree<E> extends Collection<E> {

    int size();

    boolean isEmpty();

    boolean insert(E e);

    int balance();

    boolean isBalanced();

    boolean contains(Object o);

    boolean remove(Object o);

    E search(E e);

    int height();

}
