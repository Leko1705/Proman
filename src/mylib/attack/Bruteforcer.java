package mylib.attack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Bruteforcer<T> implements Iterator<T[]> {

    private final T[] candidates;
    private final Integer size;
    private final ArrayList<T> current;
    private final ArrayList<Integer> indices;


    @SuppressWarnings("unchecked")
    public Bruteforcer(Collection<T> candidates, Integer size){
        this.candidates = (T[]) candidates.toArray();
        this.size = size;
        this.current = new ArrayList<>();
        this.indices = new ArrayList<>();

        if (this.candidates.length > 0)
            for (int i = 0; i < size; i++)
                expand();
    }

    @SuppressWarnings("unchecked")
    public Bruteforcer(Collection<T> candidates){
        this.candidates = (T[]) candidates.toArray();
        this.size = null;
        this.current = new ArrayList<>();
        this.indices = new ArrayList<>();

        if (this.candidates.length > 0)
            expand();
    }

    @Override
    public boolean hasNext() {
        return (size == null || current.size() <= size && size != 0) && candidates.length != 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] next() {
        T[] next = (T[]) current.toArray();
        permute(current.size() - 1);
        return next;
    }

    private void permute(int idx){
        int nextIndex = indices.get(idx) + 1;
        if (nextIndex < candidates.length){
            indices.set(idx, nextIndex);
            current.set(idx, candidates[nextIndex]);
        }
        else {
            indices.set(idx, 0);
            current.set(idx, candidates[0]);
            if (idx == 0)
                expand();
            else
                permute(idx - 1);

        }
    }

    private void expand(){
        current.add(candidates[0]);
        indices.add(0);
    }
}
