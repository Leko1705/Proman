package mylib.functional;

class StringifyVisitor implements FunctionVisitor<String> {
    @Override
    public String visitVariableNode(Function.VariableNode node) {
        return node.getText().toString();
    }

    @Override
    public String visitValueNode(Function.ValueNode node) {
        if (node.getValue() == Math.PI) return "pi";
        else if (node.getValue() == Math.E) return "exp(1)";
        String str = Double.toString(node.getValue());
        if (str.endsWith(".0")) str = str.substring(0, str.length()-2);
        return str;
    }

    @Override
    public String visitAddNode(Function.AddNode node) {
        String s = "";
        if (node.getLeftChild() instanceof Function.BinaryNode)
            s += "(" + node.getLeftChild().accept(this) + ")+";
        else s = node.getLeftChild().accept(this) + "+";
        if (node.getRightChild() instanceof Function.BinaryNode){
            return s + "(" + node.getRightChild().accept(this) + ")";
        }
        return s + node.getRightChild().accept(this);
    }

    @Override
    public String visitMulNode(Function.MulNode node) {
        String s = "";
        if (node.getLeftChild() instanceof Function.BinaryNode)
            s += "(" + node.getLeftChild().accept(this) + ")*";
        else s = node.getLeftChild().accept(this) + "*";
        if (node.getRightChild() instanceof Function.BinaryNode){
            return s + "(" + node.getRightChild().accept(this) + ")";
        }
        return s + node.getRightChild().accept(this);
    }

    @Override
    public String visitDivNode(Function.DivNode node) {
        String s = "";
        if (node.getLeftChild() instanceof Function.BinaryNode)
            s += "(" + node.getLeftChild().accept(this) + ")/";
        else s = node.getLeftChild().accept(this) + "/";
        if (node.getRightChild() instanceof Function.BinaryNode){
            return s + "(" + node.getRightChild().accept(this) + ")";
        }
        return s + node.getRightChild().accept(this);
    }

    @Override
    public String visitModNode(Function.ModNode node) {
        String s = "";
        if (node.getLeftChild() instanceof Function.BinaryNode)
            s += "(" + node.getLeftChild().accept(this) + ")%";
        else s = node.getLeftChild().accept(this) + "%";
        if (node.getRightChild() instanceof Function.BinaryNode){
            return s + "(" + node.getRightChild().accept(this) + ")";
        }
        return s + node.getRightChild().accept(this);
    }

    @Override
    public String visitPowNode(Function.PowNode node) {
        String s = "";
        if (node.getLeftChild() instanceof Function.BinaryNode)
            s += "(" + node.getLeftChild().accept(this) + ")^";
        else s = node.getLeftChild().accept(this) + "^";
        if (node.getRightChild() instanceof Function.BinaryNode){
            return s + "(" + node.getRightChild().accept(this) + ")";
        }
        return s + node.getRightChild().accept(this);
    }

    @Override
    public String visitCallNode(Function.CallNode node) {
        return node.getName() + "(" + node.getChild().accept(this) + ")";
    }

    @Override
    public String visitNegationNode(Function.NegationNode node) {
        String s =  "-";
        if (node.getChild() instanceof Function.BinaryNode){
            return s + "(" + node.getChild().accept(this) + ")";
        }
        else return s + node.getChild().accept(this);
    }
}
