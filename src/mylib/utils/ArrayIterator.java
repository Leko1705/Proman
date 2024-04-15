package mylib.utils;

import java.util.Arrays;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ArrayIterator<T> implements ListIterator<T> {

    private T[] content;

    private int index = 0;

    private int lastIndex = -1;

    public ArrayIterator(T[] content) {
        this.content = content;
    }

    @Override
    public boolean hasNext() {
        return index != content.length;
    }

    @Override
    public T next() {
        if (!hasNext())
            throw new NoSuchElementException();
        lastIndex = index;
        return content[index++];
    }

    @Override
    public boolean hasPrevious() {
        return index != 0;
    }

    @Override
    public T previous() {
        if (!hasPrevious())
            throw new NoSuchElementException();
        lastIndex = index;
        return content[index--];
    }

    @Override
    public int nextIndex() {
        return index+1;
    }

    @Override
    public int previousIndex() {
        return index-1;
    }

    @Override
    public void remove() {
        if (lastIndex == -1)
            throw new IllegalStateException();
        System.arraycopy(content, lastIndex + 1, content, lastIndex, content.length - 1);
    }

    @Override
    public void set(T t) {
        if (lastIndex == -1)
            throw new IllegalStateException();
        content[lastIndex] = t;
    }

    @Override
    public void add(T t) {
        content = Arrays.copyOf(content, content.length + 1);
        content[content.length-1] = t;
    }

    public Object[] getArray() {
        return content;
    }
}
