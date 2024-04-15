package mylib.structs;

import java.util.*;

public class PairingHeap<E extends Comparable<E>> extends AbstractHeap<E> {

    public Node root = null;

    private int size = 0;

    public PairingHeap(){
    }

    public PairingHeap(E e){
        insert(e);
    }

    public PairingHeap(Collection<E> c){
        addAll(c);
    }

    @Override
    public boolean insert(E e) {
        if (root == null)
            root = new Node(e);
        else
            root.children.add(new Node(e));
        size++;
        return true;
    }

    @Override
    public int balance() {
        return 0;
    }

    @Override
    public E search(E e) {
        for (E c : this)
            if (c.compareTo(e) == 0)
                return c;
        return null;
    }

    @Override
    public int height() {
        return 0;
    }

    @Override
    public boolean remove(Object o) {
        Itr itr = new Itr();
        while (itr.hasNext()){
            if (itr.next() == o){
                itr.remove();
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E poll() {
        E e = root.element;
        removeNode(null, root);
        size--;
        return e;
    }

    private void removeNode(Node parent, Node node){
        if (parent != null) {
            parent.children.remove(node);
            node.children.addFirst(root);
        } // else parent is root node -> no removable child
        mergeAll(node.children);
    }

    void mergeAll(List<Node> nodes){

        List<Node> next = new LinkedList<>();
        ListIterator<Node> itr = nodes.listIterator();
        while (itr.hasNext()) {
            Node merged = itr.next();
            if (itr.hasNext())
                merged = merge(merged, itr.next());
            next.add(merged);
        }

        while (next.size() > 1) {
            nodes = next;
            next = new LinkedList<>();
            itr = nodes.listIterator(nodes.size());
            while (itr.hasPrevious()) {
                Node merged = itr.previous();
                if (itr.hasPrevious())
                    merged = merge(merged, itr.previous());
                next.add(merged);
            }
        }

        root = next.isEmpty() ? null : next.get(0);
    }

    Node merge(Node n1, Node n2){
        if (n1.element.compareTo(n2.element) <= 0){
            n1.children.add(n2);
            return n1;
        }
        else {
            n2.children.add(n1);
            return n2;
        }
    }

    @Override
    public E peek() {
        if (root == null) return null;
        return root.element;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }


    public class Node {
        public Node(E e){
            this.element = e;
        }
        public E element;
        public LinkedList<Node> children = new LinkedList<>();

        @Override
        public String toString() {
            return "[" + element + "] -> " + children;
        }
    }


    private class Itr implements Iterator<E> {

        private final ArrayDeque<State> stack = new ArrayDeque<>();
        private Node current;

        public Itr(){
            current = root;
            if (current != null)
                stack.push(wrapState(current));
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public E next() {
            E res = current.element;
            nextNode();
            return res;
        }

        private void nextNode(){
            assert stack.peek() != null;
            Iterator<Node> itr = stack.peek().itr;
            assert itr != null;
            if (itr.hasNext()){
                current = itr.next();
                stack.push(wrapState(current));
            }
            else {
                stack.pop();
                if (stack.isEmpty())
                    return;
                nextNode();
            }
        }

        @Override
        public void remove() {
            PairingHeap.this.removeNode(getParent(), current);
        }

        private State wrapState(Node node){
            return new State(node, node.children.iterator());
        }

        private Node getParent(){
            State curr = stack.pop();
            assert stack.peek() != null;
            Node parent = stack.peek().node;
            stack.push(curr);
            return parent;
        }

        private class State {
            public final Node node;
            public final Iterator<Node> itr;
            private State(Node node, Iterator<Node> itr) {
                this.node = node;
                this.itr = itr;
            }
        }

    }

}
