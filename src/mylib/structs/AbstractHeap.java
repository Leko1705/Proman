package mylib.structs;

import java.util.NoSuchElementException;

public abstract class AbstractHeap<E> extends AbstractTree<E> implements Heap<E> {

    @Override
    public boolean offer(E e) {
        return insert(e);
    }

    @Override
    public E remove() {
        if (isEmpty()) throw new NoSuchElementException();
        return poll();
    }

    @Override
    public abstract E poll();

    @Override
    public E element() {
        if (isEmpty()) throw new NoSuchElementException();
        return peek();
    }

    @Override
    public abstract E peek();

}
