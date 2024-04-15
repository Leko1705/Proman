package mylib.view.struct;

import mylib.view.PropertyChangeListener;

import java.awt.*;
import java.util.Iterator;

public interface DataSet<K, V> extends Iterable<DataValue<V>> {

    int size();

    boolean isEmpty();

    void put(K key, DataValue<V> dataValue);
    void put(K key, V value, String description, Color color);
    void put(K key, V value, String description);
    void put(K key, V value, Color color);
    void put(K key, V value);

    void setColor(K key, Color color);
    void setDescription(K key, String desc);

    DataValue<V> getDataValue(K key);
    V getValue(K key);
    Color getColor(K key);
    String getDescription(K key);

    boolean containsKey(K key);

    boolean containsValue(V value);

    void addPropertyChangeListener(PropertyChangeListener listener);

    void firePropertyChange();

    @Override
    Iterator<DataValue<V>> iterator();
}
