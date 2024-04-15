package mylib.functional;

class LinearCheckVisitor implements FunctionVisitor<Boolean> {

    @Override
    public Boolean visitVariableNode(Function.VariableNode node) {
        return true;
    }

    @Override
    public Boolean visitValueNode(Function.ValueNode node) {
        return true;
    }

    @Override
    public Boolean visitAddNode(Function.AddNode node) {
        return node.getLeftChild().accept(this) && node.getRightChild().accept(this);
    }

    @Override
    public Boolean visitMulNode(Function.MulNode node) {
        if (node.getLeftChild() instanceof Function.ValueNode
                && node.getRightChild() instanceof Function.VariableNode)
            return true;
        return node.getRightChild() instanceof Function.ValueNode
                && node.getLeftChild() instanceof Function.VariableNode;
    }

    @Override
    public Boolean visitDivNode(Function.DivNode node) {
        return false;
    }

    @Override
    public Boolean visitModNode(Function.ModNode node) {
        return false;
    }

    @Override
    public Boolean visitPowNode(Function.PowNode node) {
        return false;
    }

    @Override
    public Boolean visitCallNode(Function.CallNode node) {
        return false;
    }

    @Override
    public Boolean visitNegationNode(Function.NegationNode node) {
        return node.getChild().accept(this);
    }
}
