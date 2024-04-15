package mylib.structs.abTree;

import java.util.Collections;
import java.util.List;

public class LeafNode<E extends Comparable<E>> extends ABNode<E> {

    public LeafNode(int a, int b) {
        super(a, b);
    }

    @Override
    public OperationResult<E> insert(List<E> e) {
        if (!Collections.disjoint(values, e)) return new OperationResult<>(false);
        E leader = e.get(0);

        for (int i = 0; i < values.size(); i++){
            E comp = values.get(i);
            if (leader.compareTo(comp) < 0){
                values.addAll(i, e);
                return handleOverflow();
            }
        }
        values.addAll(e);
        return handleOverflow();
    }

    private OperationResult<E> handleOverflow(){
        return hasOverflow()
                ? split()
                : new OperationResult<>(true);
    }

    @Override
    public OperationResult<E> remove(E e) {
        boolean success = values.remove(e);
        if (!success) return new OperationResult<>(false);
        return handleUnderflow();
    }

    private OperationResult<E> handleUnderflow(){
        return hasUnderflow()
                ? new OperationResult<>(null, this, true)
                : new OperationResult<>(true);
    }

    @Override
    public E search(E e) {
        for (E v : values){
            if (v.compareTo(e) == 0)
                return v;
        }
        return null;
    }

    @Override
    public String print(String prefix) {
        return !prefix.isEmpty() ?
                prefix.substring(0, prefix.length()-1) + values
                : values.toString();
    }

}
