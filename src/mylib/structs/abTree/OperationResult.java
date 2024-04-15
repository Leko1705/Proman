package mylib.structs.abTree;

public record OperationResult<E extends Comparable<E>>(E pivot, ABNode<E> node, boolean success) {

    public OperationResult(boolean success){
        this(null, null, success);
    }

}
