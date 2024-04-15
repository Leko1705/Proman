package mylib.structs;

import java.util.*;

public class ObservedList<T> implements List<T> {

    public interface ListChangeListener {
        void onListChanged(ObservedList<?> list);
    }




    private final List<T> list;

    private final Set<ListChangeListener> changeListeners = new LinkedHashSet<>();



    public ObservedList(List<T> list) {
        this.list = list;
    }



    public void addListChangeListener(ListChangeListener listener) {
        changeListeners.add(listener);
    }

    public boolean removeListChangeListener(ListChangeListener listener) {
        return changeListeners.remove(listener);
    }


    private void fireContentChanged(){
        for (ListChangeListener listener : changeListeners) {
            listener.onListChanged(this);
        }
    }



    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(T t) {
        list.add(t);
        fireContentChanged();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        boolean removed = list.remove(o);
        if (removed) fireContentChanged();
        return removed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(list).containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean removed = list.addAll(c);
        if (removed) fireContentChanged();
        return removed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean removed = list.addAll(index, c);
        if (removed) fireContentChanged();
        return removed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removed = list.removeAll(c);
        if (removed) fireContentChanged();
        return removed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean removed = list.retainAll(c);
        if (removed) fireContentChanged();
        return removed;
    }

    @Override
    public void clear() {
        if (list.isEmpty()) return;
        list.clear();
        fireContentChanged();
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public T set(int index, T element) {
        T old = list.set(index, element);
        fireContentChanged();
        return old;
    }

    @Override
    public void add(int index, T element) {
        list.add(index, element);
        fireContentChanged();
    }

    @Override
    public T remove(int index) {
        T old = list.remove(index);
        fireContentChanged();
        return old;
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

}
