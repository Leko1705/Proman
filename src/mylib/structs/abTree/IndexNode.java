package mylib.structs.abTree;

import java.util.List;

public class IndexNode<E extends Comparable<E>> extends ABNode<E> {

    public IndexNode(int a, int b) {
        super(a, b);
    }

    @Override
    public OperationResult<E> insert(List<E> e) {
        ABNode<E> successor = chooseSubTree(e.get(e.size()-1));
        OperationResult<E> result = successor.insert(e);

        if (result.node() != null){
            int index = indexOf(successor);
            addPair(index, result.pivot(), result.node());
            result = handleOverflow();
        }
        return result;
    }

    private OperationResult<E> handleOverflow(){
        return hasOverflow()
                ? split()
                : new OperationResult<>(true);
    }

    @Override
    public OperationResult<E> remove(E e) {
        ABNode<E> successor = chooseSubTree(e);
        OperationResult<E> result = successor.remove(e);

        if (result.node() != null){
            // todo recursive remove
        }

        return result;
    }

    @Override
    public E search(E e) {
        ABNode<E> successor = chooseSubTree(e);
        return successor.search(e);
    }

    @Override
    public String print(String prefix) {
        StringBuilder sb = new StringBuilder(prefix);

        for (int i = 0; i < values.size(); i++){
            sb.append(values.get(i))
                    .append('\n')
                    .append(prefix)
                    .append("  ")
                    .append(children.get(i).print(prefix + "\t"))
                    .append('\n');
        }

        sb.append(prefix)
                .append("  ")
                .append(lastChild().print(prefix + "\t"))
                .append('\n');

        return sb.toString();
    }

}
