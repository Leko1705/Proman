package mylib.structs.abTree;

import java.util.ArrayList;
import java.util.List;

public abstract class ABNode<E extends Comparable<E>> {


    public static <T extends Comparable<T>> ABNode<T> mergeNodes(ABNode<T> a, ABNode<T> b){
        if (a.getClass() != b.getClass()) throw new AssertionError();

        ABNode<T> res = a instanceof IndexNode<T>
                ? new IndexNode<>(a.a, a.b)
                : new LeafNode<>(a.a, a.b);

        res.values.addAll(a.values);
        res.values.addAll(b.values);
        res.children.addAll(a.children);
        res.children.addAll(b.children);
        return res;
    }



    protected final int a, b;

    protected ArrayList<E> values;

    protected ArrayList<ABNode<E>> children;

    public ABNode(int a, int b) {
        this.a = a;
        this.b = b;
        values = new ArrayList<>(a);
        children = new ArrayList<>(a);
    }


    protected int indexOf(ABNode<E> node){
        return children.indexOf(node);
    }

    protected int indexOf(E e){
        return values.indexOf(e);
    }

    protected boolean containsElement(E e){
        return values.contains(e);
    }

    protected ABNode<E> chooseSubTree(E e){
        for (int i = 0; i < values.size(); i++){
            E comp = values.get(i);
            if (e.compareTo(comp) <= 0)
                return children.get(i);
        }
        return lastChild();
    }


    protected ABNode<E> lastChild(){
        return children.get(children.size()-1);
    }

    protected E lastValue(){
        return values.get(values.size()-1);
    }

    protected boolean hasOverflow(){
        return this instanceof LeafNode<E>
                ? values.size() > b
                : children.size() > b;
    }

    protected boolean hasUnderflow(){
        return this instanceof LeafNode<E>
                ? values.size() < a
                : children.size() < a;
    }

    public OperationResult<E> split(){
        return this instanceof LeafNode<E>
                ? splitLeafNode()
                : splitIndexNode();
    }

    private OperationResult<E> splitLeafNode(){
        int half = values.size()/2;
        E pivot = values.get(half-1);
        LeafNode<E> newLeaf = new LeafNode<>(a, b);
        for (int i = 0; i < half; i++)
            newLeaf.values.add(values.remove(0));
        return new OperationResult<>(pivot, newLeaf, true);
    }

    private OperationResult<E> splitIndexNode(){

        int half = values.size()/2;
        E pivot = values.get(half);
        IndexNode<E> newIndex = new IndexNode<>(a, b);

        for (int i = 0; i < half; i++)
            newIndex.addPair(values.remove(0), children.remove(0));

        newIndex.children.add(children.remove(0));
        values.remove(0);

        return new OperationResult<>(pivot, newIndex, true);
    }

    protected void addPair(E e, ABNode<E> successor){
        values.add(e);
        children.add(successor);
    }

    protected void addPair(int i, E e, ABNode<E> successor){
        values.add(i, e);
        children.add(i, successor);
    }

    public abstract OperationResult<E> insert(List<E> e);

    public abstract OperationResult<E> remove(E e);

    public abstract E search(E e);

    public abstract String print(String prefix);

    @Override
    public String toString() {
        return print("");
    }
}
