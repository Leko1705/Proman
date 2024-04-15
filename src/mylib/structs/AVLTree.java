package mylib.structs;

import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

@SuppressWarnings("unused")
public class AVLTree<E extends Comparable<E>> extends AbstractTree<E> {

    private int size = 0;

    public Node node;

    private int height = 0;

    public AVLTree(){
    }

    public AVLTree(E e){
        insert(e);
    }

    public AVLTree(Collection<? extends E> collection){
        addAll(collection);
    }

    @Override
    public boolean insert(E e) {
        if (e == null) return false;
        if (isEmpty()){
            node = new Node(e, new AVLTree<>(), new AVLTree<>());
            size++;
            updateHeight();
            return true;
        }

        boolean success = false;
        int comparison = e.compareTo(node.element);

        if (comparison < 0) {
            success = node.left.insert(e);
        }

        else if (comparison > 0) {
            success = node.right.insert(e);
        }

        if (success){
            size++;
            updateHeight();
            rotate();
        }
        return success;
    }

    @Override
    public E search(E e) {
        if (e == null || isEmpty()) return null;
        if (node.element.compareTo(e) == 0) return node.element;
        int comparison = e.compareTo(node.element);
        if (comparison < 0)
            return node.left.search(e);
        else
            return node.right.search(e);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        if (isEmpty()) return false;

        if (node.element == o){
            deepRemove();
            updateHeight();
            size--;
            return true;
        }

        boolean success;
        @SuppressWarnings("unchecked") E e = (E) o;

        if (e.compareTo(node.element) < 0){
            success = node.left.remove(o);
        }
        else {
             success = node.right.remove(o);
        }
        if (success) {
            updateHeight();
            rotate();
            size--;
        }
        return success;
    }

    private void deepRemove(){
        if (node.left.isEmpty()){
            if (node.right.isEmpty()){
                // no successor exist
                node = null;
            }
            else {
                // only right successor exist
                node = node.right.node;
            }
        }
        else if (node.right.isEmpty()){
            // only left successor exist
            node = node.left.node;
        }
        else {
            // left and right successor exist
            Node rightSuccessorNode = node.right.node;
            if (rightSuccessorNode.left.isEmpty()){
                // right successor has no left successor
                node.element = rightSuccessorNode.element;
                node.right = rightSuccessorNode.right;
            }
            else {
                AVLTree<E> previous = node.right.getMostLeftAncestor();
                AVLTree<E> smallest = previous.node.left;
                this.node.element = smallest.node.element;
                previous.remove(smallest.node.element);
            }
        }
    }

    private AVLTree<E> getMostLeftAncestor(){
        Node leftSuccessor = node.left.node;
        if (leftSuccessor.left.isEmpty()) {
            return this;
        }
        else {
            return node.left.getMostLeftAncestor();
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return node == null;
    }

    public int height(){
        return height;
    }

    private void updateHeight(){
        height = isEmpty()
                        ? 0
                        : Math.max(getLeftTree().height(), getRightTree().height()) + 1;
    }

    @Override
    public int balance() {
        if (isEmpty()) return 0;
        return getRightTree().height() - getLeftTree().height();
    }

    @Override
    public Iterator<E> iterator() {
        return new InOrderTraverser();
    }

    public Iterator<E> preorderIterator(){
        return new PreOrderTraverser();
    }

    public Iterator<E> inorderIterator(){
        return iterator();
    }

    public Iterator<E> postorderIterator(){
        return new PostOrderTraverser();
    }

    private void rotate(){

        if (balance() >= 2){

            if (getRightTree().balance() < 0)
                getRightTree().rotateRight();

            rotateLeft();
        }
        else if (balance() <= -2){

            if (getLeftTree().balance() > 0)
                getLeftTree().rotateLeft();

            rotateRight();
        }

    }

    private void rotateLeft(){
        Node green = getRightTree().node;
        getRightTree().node = green.left.node;
        green.left.node = node;
        node = green;

        getLeftTree().getRightTree().updateHeight();
        getLeftTree().updateHeight();
        updateHeight();

    }

    private void rotateRight(){
        Node green = getLeftTree().node;
        getLeftTree().node = green.right.node;
        green.right.node = node;
        node = green;

        getRightTree().getLeftTree().updateHeight();
        getRightTree().updateHeight();
        updateHeight();
    }

    private AVLTree<E> getLeftTree(){
        return node.left;
    }

    private AVLTree<E> getRightTree(){
        return node.right;
    }

    public class Node implements Cloneable {
        public AVLTree<E> left, right;
        public E element;
        Node(E e, AVLTree<E> left, AVLTree<E> right){
            this.element = e;
            this.left = left;
            this.right = right;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Node clone() {
            try {
                return (Node) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }


    public abstract class Traverser implements Iterator<E> {

        AVLTree<E> current;

        int jumpState = 1;
        Stack<AVLTree<E>> stack = new Stack<>();

        protected Traverser(){
            current = AVLTree.this;
            if (current.node != null)
                next();
        }

        public E getCurrent(){
            if (current == null)
                return null;
            return current.node.element;
        }

        public abstract E next();

        public boolean hasNext(){
            return current != null && current.node != null;
        }

        protected AVLTree<E> pop(){
            if (stack.isEmpty())
                return null;
            else
                return stack.pop();

        }
    }


    public class InOrderTraverser extends Traverser {

        @Override
        public E next() {

            E element = current.node.element;

            if (jumpState == 1){
                stack.push(current);
                if (!current.node.left.isEmpty()){
                    current = current.node.left;
                    next();
                }
                else {
                    jumpState = 0;
                }
            }
            else if (jumpState == 0){
                AVLTree<E> comparable = pop();
                if (current.equals(comparable)){
                    jumpState = -1;
                    next();
                }
                else {
                    current = comparable;
                    jumpState = -1;
                }
            }
            else if (jumpState == -1){
                if (!current.node.right.isEmpty()) {
                    current = current.node.right;
                    jumpState = 1;
                    next();
                }
                else {
                    current = pop();
                }
            }
            return element;
        }
    }

    public class PostOrderTraverser extends Traverser {

        @Override
        public E next() {

            E element = current.node.element;

            if (jumpState == 1){
                if (!current.node.left.isEmpty()){
                    stack.push(current);
                    current = current.node.left;
                    next();
                }
                else if (!current.node.right.isEmpty()){
                    stack.push(current);
                    current = current.node.right;
                    next();
                }
                else {
                    jumpState = 0;
                }
            }
            else if (jumpState == 0){
                AVLTree<E> previous = pop();
                if (previous == null){
                    current = null;
                    return element;
                }

                if (previous.node.right.isEmpty()){
                    current = previous;
                     return element;
                }

                if (!previous.node.right.equals(current)){
                    stack.push(previous);
                    current = previous.node.right;
                    jumpState = 1;
                    next();
                }
                else {
                    current = previous;
                }
            }

            return element;

        }
    }


    public class PreOrderTraverser extends Traverser {

        @Override
        public E next() {

            E element = current.node.element;

            if (jumpState == 1){
                jumpState = 0;
            }

            else if (jumpState == 0){

                if (current.node.left.isEmpty() && current.node.right.isEmpty()){
                    AVLTree<E> previous = pop();
                    if (previous == null){
                        current = null;
                        return element;
                    }

                    current = previous.node.right;
                    if (current.node.right == null)
                        current = null;
                }

                else if (!current.node.left.isEmpty()){
                    stack.push(current);
                    current = current.node.left;
                }

                else { // !current.node.right.isEmpty()
                    current = current.node.right;
                }
            }

            return element;
        }
    }

}
