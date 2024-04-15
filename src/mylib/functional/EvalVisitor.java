package mylib.functional;

import java.util.HashMap;

class EvalVisitor implements FunctionVisitor<Double> {

    private final HashMap<Character, Double> vars;

    public EvalVisitor(HashMap<Character, Double> vars){
        this.vars = vars;
    }

    @Override
    public Double visitVariableNode(Function.VariableNode node) {
        return vars.get(node.getText());
    }

    @Override
    public Double visitValueNode(Function.ValueNode node) {
        return node.getValue();
    }

    @Override
    public Double visitAddNode(Function.AddNode node) {
        return node.getLeftChild().accept(this) + node.getRightChild().accept(this);
    }

    @Override
    public Double visitMulNode(Function.MulNode node) {
        return node.getLeftChild().accept(this) * node.getRightChild().accept(this);

    }

    @Override
    public Double visitDivNode(Function.DivNode node) {
        return node.getLeftChild().accept(this) / node.getRightChild().accept(this);

    }

    @Override
    public Double visitModNode(Function.ModNode node) {
        return node.getLeftChild().accept(this) % node.getRightChild().accept(this);
    }

    @Override
    public Double visitPowNode(Function.PowNode node) {
        return Math.pow(node.getLeftChild().accept(this), node.getRightChild().accept(this));
    }

    @Override
    public Double visitCallNode(Function.CallNode node) {
        return node.getBuildInFunction().eval(node.getChild().accept(this));
    }

    @Override
    public Double visitNegationNode(Function.NegationNode node) {
        return -node.getChild().accept(this);
    }
}
