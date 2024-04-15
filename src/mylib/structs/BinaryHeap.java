package mylib.structs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unused")
public class BinaryHeap<E extends Comparable<E>> extends AbstractHeap<E> {

    private final ArrayList<E> values;

    public BinaryHeap(){
        values = new ArrayList<>();
    }

    public BinaryHeap(E e){
        values = new ArrayList<>(List.of(e));
    }

    public BinaryHeap(Collection<? extends E> collection){
        values = new ArrayList<>(collection);
        // build heap backwards
        for (int i = values.size()/2; i >= 0; i--)
            siftDown(i);
    }

    private void buildHeapBackwards(){

    }

    @Override
    public boolean insert(E e) {
        values.add(e);
        siftUp(values.size()-1);
        return true;
    }

    @Override
    public E search(E e) {
        for (E v : values)
            if (v.compareTo(e) == 0)
                return v;
        return null;
    }

    @Override
    public int balance() {
        return height() - size() == 0
                ? 0
                : 1;
    }

    @Override
    public boolean remove(Object o) {
        int index = values.indexOf(o);
        if (index == -1) return false;
        removeNodeAt(index);
        return true;
    }

    private void removeNodeAt(int idx){
        values.set(idx, values.remove(values.size()-1));
        siftDown(idx);
    }

    private void siftDown(int i){
        if (2*i < values.size()) {
            int m;
            if (2*i+1 > values.size()-1 || values.get(2*i).compareTo(values.get(2*i+1)) <= 0)
                m = 2*i;
            else
                m = 2*i+1;
            if (values.get(i).compareTo(values.get(m)) > 0){
                swap(i, m);
                siftDown(m);
            }
        }

    }

    private void siftUp(int i){
        if (i == 0 || values.get(i/2).compareTo(values.get(i)) < 0)
            return;
        swap(i, i/2);
        siftUp(i/2);
    }

    private void swap(int i, int j){
        E dummy = values.get(i);
        values.set(i, values.get(j));
        values.set(j, dummy);
    }

    @Override
    public int height() {
        if (values.isEmpty()) return 0;
        return (int) (Math.log(values.size()) / Math.log(2)) + 1;
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public E poll() {
        E res = values.get(0);
        removeNodeAt(0);
        return res;
    }

    @Override
    public E peek() {
        if (values.isEmpty()) return null;
        return values.get(0);
    }

    public E get(int index){
        return values.get(index);
    }

    @Override
    public Object[] toArray() {
        return values.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return values.toArray(a);
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public Iterator<E> iterator() {
        return values.iterator();
    }

    @Override
    public String toString() {
        return values.toString();
    }

}
