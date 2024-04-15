package mylib.view.struct;

import java.util.Iterator;
import java.util.Objects;

public class ArrayDataSet<E> extends AbstractDataSet<Integer, E> {

    private final Object[] values;

    public ArrayDataSet(int initialCapacity){
        values = new Object[initialCapacity];
        for (int i = 0; i < initialCapacity; i++)
            values[i] = new DataValue<>(0.0, null, null);
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public void put(Integer key, DataValue<E> dataValue) {
        Objects.checkIndex(key, values.length);
        values[key] = dataValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataValue<E> getDataValue(Integer key) {
        Objects.checkIndex(key, values.length);
        return (DataValue<E>) values[key];
    }

    @Override
    public Iterator<DataValue<E>> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<DataValue<E>> {
        private int cursor = 0;
        public boolean hasNext() {
            return cursor != values.length;
        }
        public DataValue<E> next() {
            return getDataValue(cursor++);
        }
    }

}
