package mylib.view.struct;

import java.util.HashMap;
import java.util.Iterator;

public class MappedDataSet<K, V> extends AbstractDataSet<K, V> {

    private final HashMap<K, DataValue<V>> map = new HashMap<>();

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void put(K key, DataValue<V> dataValue) {
        map.put(key, dataValue);
    }

    @Override
    public DataValue<V> getDataValue(K key) {
        return map.get(key);
    }

    @Override
    public Iterator<DataValue<V>> iterator() {
        return map.values().iterator();
    }
}
