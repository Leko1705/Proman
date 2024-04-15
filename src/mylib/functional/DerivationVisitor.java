package mylib.functional;

class DerivationVisitor implements FunctionVisitor<String> {


    /**
     * StringifyVisitor to get the non derived sub-term.
     */
    private final StringifyVisitor stringifier = new StringifyVisitor();


    /**
     * the current variable to derive for
     */
    private final char variable;

    public DerivationVisitor(char var){
        this.variable = var;
    }

    @Override
    public String visitVariableNode(Function.VariableNode node) {
        return node.getText() == variable ? "1" : "0";
    }

    @Override
    public String visitValueNode(Function.ValueNode node) {
        return "0";
    }

    @Override
    public String visitAddNode(Function.AddNode node) {
        // d[u(x)+v(x)] = u'(x) + v'(x)
        return "(" + node.getLeftChild().accept(this) + "+" + node.getRightChild().accept(this) + ")";
    }

    @Override
    public String visitMulNode(Function.MulNode node) {
        // product-rule
        // d[u(x)*v(x)] = u'(x) * v(x) + u(x) * v'(x)
        return "(" + node.getLeftChild().accept(this) + "*" + node.getRightChild().accept(stringifier)
                + "+" + node.getLeftChild().accept(stringifier) + "*" + node.getRightChild().accept(this) + ")";
    }

    @Override
    public String visitDivNode(Function.DivNode node) {
        // quotient-rule
        // d[u(x)/v(x)] = (u'(x)*v(x) - u(x)*v'(x)) / (v(x)^2)
        return "((" + node.getLeftChild().accept(this) + "*" + node.getRightChild().accept(stringifier)
                + "-" + node.getLeftChild().accept(stringifier) + "*" + node.getRightChild().accept(this)
                + ")/(" + node.getRightChild().accept(stringifier) + ")^2)";
    }

    @Override
    public String visitModNode(Function.ModNode node) {
        throw new UnsupportedOperationException("modulo derivation is not supported yet");
    }

    @Override
    public String visitPowNode(Function.PowNode node) {
        if (node.getLeftChild() instanceof Function.VariableNode left
                && node.getRightChild() instanceof Function.ValueNode right){
            // basic derivative
            // d[x^n] = n*x^(n-1)
            return left.getText() == variable
                    ? right.getValue() + "*" + variable + "^" + (right.getValue()-1)
                    : "0";
        }

        else if (node.getLeftChild() instanceof Function.ValueNode left) {
            // d[a^u(x)] = ln(a) * a^u(x) * u'(x)
            double a = left.getValue();
            Function.Node u = node.getRightChild();
            return "ln(" + a + ")*" + a + "^" + u.accept(stringifier) + "*" + u.accept(this);
        }

        // d[u(x)^v(x)] = u(x)^v(x) * d[ln(u(x))*v(x)]
        return node.accept(stringifier) + "*"
                + Function.derivation(
                        "ln(" + node.getLeftChild().accept(stringifier) + ")*" + node.getRightChild().accept(stringifier),
                            variable);
    }

    @Override
    public String visitCallNode(Function.CallNode node) {
        return switch (node.getName()){
            case "ln" -> deriveLn(node.getArgument());
            case "exp" -> deriveExp(node.getArgument());
            case "sin", "cos", "tan", "asin", "acos" -> deriveTrigonometrical(node);
            default -> throw new UnsupportedOperationException();
        };
    }

    private String deriveTrigonometrical(Function.CallNode node) {
        return switch (node.getName()){
            case "sin" ->
                    "cos(" + node.getArgument().accept(stringifier) + ")*" + node.getArgument().accept(this);
            case "cos" ->
                    "-sin(" + node.getArgument().accept(stringifier) + ")*" + node.getArgument().accept(this);
            default -> throw new UnsupportedOperationException();
        };
    }

    private String deriveLn(Function.Node arg){
        return "(1/" + arg.accept(stringifier) + ")*" + arg.accept(this);
    }

    private String deriveExp(Function.Node arg){
        return "exp(" + arg.accept(stringifier) + ")*" + arg.accept(this);
    }

    @Override
    public String visitNegationNode(Function.NegationNode node) {
        return "(-" + node.getChild().accept(this) + ")";
    }

}
