package mylib.functional;

class LinkVisitor implements FunctionVisitor<Void> {

    private final Function.Node insertion;

    LinkVisitor(Function.Node node){
        this.insertion = node;
    }

    @Override
    public Void visitVariableNode(Function.VariableNode node) {
        replaceChild(node.getParent(), node, insertion);
        return null;
    }

    @Override
    public Void visitValueNode(Function.ValueNode node) {
        return null;
    }

    @Override
    public Void visitAddNode(Function.AddNode node) {
        return handleBinaryNode(node);
    }

    @Override
    public Void visitMulNode(Function.MulNode node) {
        return handleBinaryNode(node);
    }

    @Override
    public Void visitDivNode(Function.DivNode node) {
        return handleBinaryNode(node);
    }

    @Override
    public Void visitModNode(Function.ModNode node) {
        return handleBinaryNode(node);
    }

    @Override
    public Void visitPowNode(Function.PowNode node) {
        return handleBinaryNode(node);
    }

    @Override
    public Void visitCallNode(Function.CallNode node) {
        return node.getArgument().accept(this);
    }

    @Override
    public Void visitNegationNode(Function.NegationNode node) {
        return node.getChild().accept(this);
    }

    private Void handleBinaryNode(Function.BinaryNode node){
        node.getLeftChild().accept(this);
        node.getRightChild().accept(this);
        return null;
    }

    public void replaceChild(Function.Node parent, Function.Node oldChild, Function.Node newChild){
        if (parent instanceof Function.BinaryNode binaryNode){
            if (binaryNode.getLeftChild() == oldChild){
                binaryNode.setLeftChild(newChild);
            }
            else {
                binaryNode.setRightChild(newChild);
            }
            newChild.setParent(binaryNode);
        }
        else {
            parent.setChild(newChild);
            newChild.setParent(parent);
        }
    }

}
