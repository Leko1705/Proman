package mylib.functional;

import java.util.ArrayDeque;

class EqualityCheckVisitor implements FunctionVisitor<Boolean> {

    private final ArrayDeque<Function.Node> stack = new ArrayDeque<>();

    public EqualityCheckVisitor(Function.Node checked){
        stack.push(checked);
    }

    @Override
    public Boolean visitVariableNode(Function.VariableNode node) {
        if (!(stack.peek() instanceof Function.VariableNode variableNode)) return false;
        return node.getText().equals(variableNode.getText());
    }

    @Override
    public Boolean visitValueNode(Function.ValueNode node) {
        if (!(stack.peek() instanceof Function.ValueNode valueNode)) return false;
        return node.getValue() == valueNode.getValue();
    }

    @Override
    public Boolean visitAddNode(Function.AddNode node) {
        boolean res = false;

        if (stack.peek() instanceof Function.AddNode addNode) {
            stack.push(addNode.getLeftChild());

            if (node.getLeftChild().accept(this)) {
                stack.push(addNode.getRightChild());
                res = node.getRightChild().accept(this);
                stack.pop();
            } else if (node.getRightChild().accept(this)) {
                stack.push(addNode.getRightChild());
                res = node.getLeftChild().accept(this);
                stack.pop();
            }
            stack.pop();
        }
        else if (stack.peek() instanceof Function.MulNode mulNode) {
            if (mulNode.getRightChild() instanceof Function.ValueNode valueNode
                && valueNode.getValue() == 2){
                stack.push(mulNode.getLeftChild());
                if(node.getLeftChild().accept(this)){
                    res = node.getRightChild().accept(this);
                }
                stack.pop();
            }
            else if (mulNode.getLeftChild() instanceof Function.ValueNode valueNode
                    && valueNode.getValue() == 2){
                stack.push(mulNode.getRightChild());
                if(node.getRightChild().accept(this)){
                    res = node.getLeftChild().accept(this);
                }
                stack.pop();
            }
        }


        return res;
    }

    @Override
    public Boolean visitMulNode(Function.MulNode node) {
        boolean res = false;

        if (stack.peek() instanceof Function.MulNode mulNode) {
            stack.push(mulNode.getLeftChild());

            if (node.getLeftChild().accept(this)) {
                stack.push(mulNode.getRightChild());
                res = node.getRightChild().accept(this);
                stack.pop();
            } else if (node.getRightChild().accept(this)) {
                stack.push(mulNode.getRightChild());
                res = node.getLeftChild().accept(this);
                stack.pop();
            }
            stack.pop();
        }
        else if (stack.peek() instanceof Function.PowNode powNode) {
            if (powNode.getRightChild() instanceof Function.ValueNode valueNode
                    && valueNode.getValue() == 2){
                stack.push(powNode.getLeftChild());
                if(node.getLeftChild().accept(this)){
                    res = node.getRightChild().accept(this);
                }
                stack.pop();
            }
            else if (powNode.getLeftChild() instanceof Function.ValueNode valueNode
                    && valueNode.getValue() == 2){
                stack.push(powNode.getRightChild());
                if(node.getRightChild().accept(this)){
                    res = node.getLeftChild().accept(this);
                }
                stack.pop();
            }
        }
        else if (stack.peek() instanceof Function.AddNode addNode){
            stack.push(node);
            res = visitAddNode(addNode);
            stack.pop();
        }

        return res;
    }

    @Override
    public Boolean visitDivNode(Function.DivNode node) {
        if (!(stack.peek() instanceof Function.DivNode divNode)) return false;
        boolean res = false;
        stack.push(divNode.getLeftChild());
        if (node.getLeftChild().accept(this)){
            stack.push(divNode.getRightChild());
            res = node.getRightChild().accept(this);
            stack.pop();
        }
        stack.pop();
        return res;
    }

    @Override
    public Boolean visitModNode(Function.ModNode node) {
        if (!(stack.peek() instanceof Function.ModNode modNode)) return false;
        boolean res = false;
        stack.push(modNode.getLeftChild());
        if (node.getLeftChild().accept(this)){
            stack.push(modNode.getRightChild());
            res = node.getRightChild().accept(this);
            stack.pop();
        }
        stack.pop();
        return res;
    }

    @Override
    public Boolean visitPowNode(Function.PowNode node) {
        boolean res = false;

        if (stack.peek() instanceof Function.PowNode powNode) {
            stack.push(powNode.getLeftChild());
            if (node.getLeftChild().accept(this)) {
                stack.push(powNode.getRightChild());
                res = node.getRightChild().accept(this);
                stack.pop();
            }
            stack.pop();
        }
        else if (stack.peek() instanceof Function.MulNode mulNode){
            stack.push(node);
            res = visitMulNode(mulNode);
            stack.pop();
        }
        return res;
    }

    @Override
    public Boolean visitCallNode(Function.CallNode node) {
        if (!(stack.peek() instanceof Function.CallNode callNode)) return false;
        if (!callNode.getName().equals(node.getName())) return false;
        stack.push(callNode.getArgument());
        boolean res = node.getArgument().accept(this);
        stack.pop();
        return res;
    }

    @Override
    public Boolean visitNegationNode(Function.NegationNode node) {
        if (!(stack.peek() instanceof Function.NegationNode negationNode)) return false;
        stack.push(negationNode.getChild());
        boolean res = node.getChild().accept(this);
        stack.pop();
        return res;
    }

}
