package mylib.view.struct;

import mylib.view.PropertyChangeListener;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class AbstractDataSet<K, V> implements DataSet<K, V> {


    private final LinkedList<PropertyChangeListener> listeners = new LinkedList<>();


    public abstract int size();

    public abstract void put(K key, DataValue<V> dataValue);

    public abstract DataValue<V> getDataValue(K key);

    public abstract Iterator<DataValue<V>> iterator();


    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void put(K key, V value, String description, Color color) {
        if (containsKey(key)) {
            DataValue<V> dataValue = getDataValue(key);
            dataValue.setValue(value);
            dataValue.setDesc(description);
            dataValue.setColor(color);
        }
        else
            put(key, new DataValue<>(value, description, color));
    }

    @Override
    public void put(K key, V value, String description) {
        if (containsKey(key)) {
            DataValue<V> dataValue = getDataValue(key);
            dataValue.setValue(value);
            dataValue.setDesc(description);
        }
        else
            put(key, value, description, null);
    }

    @Override
    public void put(K key, V value, Color color) {
        if (containsKey(key)) {
            DataValue<V> dataValue = getDataValue(key);
            dataValue.setValue(value);
            dataValue.setColor(color);
        }
        else
            put(key, value, null, color);
    }

    @Override
    public void put(K key, V value) {
        DataValue<V> dataValue = getDataValue(key);
        dataValue.setValue(value);
    }

    @Override
    public void setColor(K key, Color color) {
        DataValue<V> value = getDataValue(key);
        value.setColor(color);
    }

    @Override
    public void setDescription(K key, String desc) {
        DataValue<V> dataValue = getDataValue(key);
        dataValue.setDesc(desc);
    }

    @Override
    public V getValue(K key) {
        return getDataValue(key).getElement();
    }

    @Override
    public Color getColor(K key) {
        return getDataValue(key).getColor();
    }

    @Override
    public String getDescription(K key) {
        return getDataValue(key).getDesc();
    }

    @Override
    public boolean containsKey(K key) {
        return getDataValue(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        for (DataValue<V> elementValue : this)
            if (elementValue.getElement() == value)
                return true;
        return false;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void firePropertyChange() {
        for (PropertyChangeListener listener : listeners)
            listener.onChange();
    }

}
