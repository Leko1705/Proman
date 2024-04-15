package mylib.structs;

import java.util.Queue;

public interface Heap<E> extends Tree<E>, Queue<E> {

    int size();

    boolean isEmpty();

    boolean insert(E e);

    E poll();

    E peek();

}
