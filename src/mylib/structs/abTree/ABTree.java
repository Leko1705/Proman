package mylib.structs.abTree;

import mylib.structs.AbstractTree;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;

public class ABTree<E extends Comparable<E>> extends AbstractTree<E> {

    private int size = 0;

    private final int a, b;

    ABNode<E> root;

    public ABTree(int a, int b){
        if (a < 2 || b < a || b < 2 * a - 1)
            throw new IllegalArgumentException("a=" + a + " b=" + b);
        this.a = a;
        this.b = b;
        root = new LeafNode<>(a, b);
    }

    @Override
    public boolean insert(E e) {
        OperationResult<E> result = root.insert(List.of(e));
        if (result.node() != null){
            ABNode<E> newRoot = new IndexNode<>(a, b);
            newRoot.addPair(result.pivot(), result.node());
            newRoot.children.add(root);
            root = newRoot;
        }
        if (result.success()) size++;
        return result.success();
    }

    @Override
    public int balance() {
        return 0;
    }

    @Override
    public E search(E o) {
        return root.search(o);
    }

    @Override
    public boolean remove(Object o) {
        OperationResult<E> result = root.remove((E) o);
        if (result.success()) size--;
        return result.success();
    }

    @Override
    public void clear() {
        root = null; // help gc
        root = new LeafNode<>(a, b);
        size = 0;
    }

    @Override
    public int height() {
        return (int) (1 + Math.log((double) (size + 1) /2) / Math.log(a));
    }

    @Override
    public int size() {
        return size;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public String toString() {
        return root.toString();
    }



    private class Itr implements Iterator<E> {

        private Iterator<E> leafItr = null;

        private final ArrayDeque<Iterator<ABNode<E>>> stack = new ArrayDeque<>();

        public Itr(){
            if (isEmpty()) return;
            if (root instanceof LeafNode<E>)
                leafItr = root.values.iterator();
            else {
                nextLeafNode(root);
            }
        }

        @Override
        public boolean hasNext() {
            return leafItr != null;
        }

        @Override
        public E next() {
            E elem = leafItr.next();

            if (!leafItr.hasNext())
                jumpToNextLeafNode();

            return elem;
        }

        private void jumpToNextLeafNode(){
            Iterator<ABNode<E>> indexItr;
            do {
                if (stack.isEmpty()) {
                    leafItr = null;
                    return;
                }
                indexItr = stack.pop();
            } while (!indexItr.hasNext());

            nextLeafNode(indexItr.next());
        }

        private void nextLeafNode(ABNode<E> node){
            ABNode<E> curr = node;
            while (curr instanceof IndexNode<E>){
                Iterator<ABNode<E>> childItr = curr.children.iterator();
                childItr.next();
                stack.push(childItr);
                curr = curr.children.get(0);
            }
            leafItr = curr.values.iterator();
        }

    }

}
