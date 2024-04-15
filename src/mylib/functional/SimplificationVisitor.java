package mylib.functional;

class SimplificationVisitor implements FunctionVisitor<Void> {

    @Override
    public Void visitVariableNode(Function.VariableNode node) {
        return null;
    }

    @Override
    public Void visitValueNode(Function.ValueNode node) {
        return null;
    }

    @Override
    public Void visitAddNode(Function.AddNode node) {
        node.getLeftChild().accept(this);
        node.getRightChild().accept(this);

        if (node.getLeftChild() instanceof Function.ValueNode left
                && node.getRightChild() instanceof Function.ValueNode right){
            Function.ValueNode simplifiedNode = new Function.ValueNode(left.getValue() + right.getValue());
            replaceChild(node.getParent(), node, simplifiedNode);
        }
        else if (node.getLeftChild() instanceof Function.ValueNode valueNode){
            if (valueNode.getValue() == 0.0){
                replaceChild(node.getParent(), node, node.getRightChild());
            }
        }
        else if (node.getRightChild() instanceof Function.ValueNode valueNode){
            if (valueNode.getValue() == 0.0){
                replaceChild(node.getParent(), node, node.getLeftChild());
            }
            else if (node.getLeftChild() instanceof Function.AddNode addNode){

                // simplify (u(x)+3)+2 to u(x)+5
                if (addNode.getRightChild() instanceof Function.ValueNode addRight){
                    replaceChild(node.getParent(), node, new Function.AddNode(
                            addNode.getLeftChild(),
                            new Function.ValueNode(valueNode.getValue() + addRight.getValue())
                    ));
                }

                // simplify (3+u(x))+2 to u(x)+5
                else if (addNode.getLeftChild() instanceof Function.ValueNode addLeft){
                    replaceChild(node.getParent(), node, new Function.AddNode(
                            addNode.getRightChild(),
                            new Function.ValueNode(valueNode.getValue() + addLeft.getValue())
                    ));
                }
            }
        }
        else if (node.getRightChild() instanceof Function.VariableNode variableNode){

            // simplify x+x to x*2
            if (node.getLeftChild() instanceof Function.VariableNode left){
                if (left.getText().equals(variableNode.getText())){
                    replaceChild(node.getParent(), node, new Function.MulNode(new Function.ValueNode(2), variableNode));
                }
            }

            else if (node.getLeftChild() instanceof Function.AddNode addNode){

                // try simplify (u(x)+x)+x to u(x)+2*x
                if (addNode.getRightChild() instanceof Function.VariableNode addRight){
                    if (variableNode.getText().equals(addRight.getText())){
                        Function.AddNode optimAddNode = new Function.AddNode(new Function.MulNode(addRight, new Function.ValueNode(2)),
                                addNode.getRightChild());
                        replaceChild(node.getParent(), node,
                                optimAddNode);
                    }
                }

                // try simplify (x+u(x))+x to u(x)+2*x
                else if (addNode.getLeftChild() instanceof Function.VariableNode addLeft){
                    if (variableNode.getText().equals(addLeft.getText())){
                        Function.AddNode optimAddNode = new Function.AddNode(new Function.MulNode(addLeft, new Function.ValueNode(2)),
                                addNode.getRightChild());
                        replaceChild(node.getParent(), node,
                                optimAddNode);
                    }
                }
            }

            else if (node.getLeftChild() instanceof Function.MulNode mulNode){
                if (mulNode.getLeftChild() instanceof Function.VariableNode left){
                    if (variableNode.getText().equals(left.getText())) {

                        // simplify (x*2)+x to x*3
                        if (mulNode.getRightChild() instanceof Function.ValueNode right) {
                            replaceChild(node.getParent(), node, new Function.MulNode(
                                    new Function.ValueNode(right.getValue() + 1),
                                    variableNode));
                        }
                        else {
                            // simplify (x*u(x))+x to x*(u(x)+1)
                            Function.MulNode optimMulNode = new Function.MulNode(
                                    variableNode,
                                    new Function.AddNode(mulNode.getRightChild(), new Function.ValueNode(1))
                            );
                            replaceChild(node.getParent(), node, optimMulNode);
                            optimMulNode.accept(this); // try to simplify the new factor
                        }
                    }
                }
                else if (mulNode.getRightChild() instanceof Function.VariableNode right){
                    if (variableNode.getText().equals(right.getText())) {

                        // simplify (2*x)+x to x*3
                        if (mulNode.getLeftChild() instanceof Function.ValueNode left) {
                            replaceChild(node.getParent(), node, new Function.MulNode(
                                    new Function.ValueNode(left.getValue() + 1),
                                    variableNode));
                        }
                        else {
                            // simplify (u(x)*x)+x to x*(u(x)+1)
                            Function.MulNode optimMulNode = new Function.MulNode(
                                    variableNode,
                                    new Function.AddNode(mulNode.getLeftChild(), new Function.ValueNode(1))
                            );
                            replaceChild(node.getParent(), node, optimMulNode);
                            optimMulNode.accept(this); // try to simplify the new factor
                        }
                    }
                }
            }
        }
        else if (node.getLeftChild().accept(new EqualityCheckVisitor(node.getRightChild()))){
            replaceChild(node.getParent(), node, new Function.MulNode(
                    node.getLeftChild(),
                    new Function.ValueNode(2)
            ));
            node.getParent().accept(this);
        }
        return null;
    }

    @Override
    public Void visitMulNode(Function.MulNode node) {
        node.getLeftChild().accept(this);
        node.getRightChild().accept(this);

        if (node.getLeftChild() instanceof Function.VariableNode
            && !(node.getRightChild() instanceof Function.VariableNode)){
                return visitMulNode(swapChildren(node));
        }
        else if (node.getLeftChild() instanceof Function.ValueNode
                && node.getRightChild() instanceof Function.BinaryNode){
            return visitMulNode(swapChildren(node));
        }

        if (node.getLeftChild() instanceof Function.ValueNode left
                && node.getRightChild() instanceof Function.ValueNode right){
            Function.ValueNode simplifiedNode = new Function.ValueNode(left.getValue() * right.getValue());
            replaceChild(node.getParent(), node, simplifiedNode);
        }

        // simplify 0*x to 0 or 1*x to x
        else if (node.getLeftChild() instanceof Function.ValueNode valueNode){
            if (valueNode.getValue() == 1.0){
                replaceChild(node.getParent(), node, node.getRightChild());
            }
            else if (valueNode.getValue() == 0.0){
                replaceChild(node.getParent(), node, new Function.ValueNode(0.0));
            }

        }

        else if (node.getRightChild() instanceof Function.ValueNode valueNode){
            if (valueNode.getValue() == 1.0){
                replaceChild(node.getParent(), node, node.getLeftChild());
            }
            else if (valueNode.getValue() == 0.0){
                replaceChild(node.getParent(), node, new Function.ValueNode(0.0));
            }
            else
                return optimizeMulConstantRight(node, valueNode);
        }

        else if (node.getRightChild() instanceof Function.VariableNode variableNode){
            return optimizeMulVariableRight(node, variableNode);
        }
        else if (node.getRightChild() instanceof Function.PowNode powNode){
            return optimizeMulPowRight(node, powNode);
        }
        else if (node.getLeftChild().accept(new EqualityCheckVisitor(node.getRightChild()))){
            replaceChild(node.getParent(), node, new Function.PowNode(
                    node.getLeftChild(),
                    new Function.ValueNode(2)
            ));
            node.getParent().accept(this);
        }

        return null;
    }

    private Void optimizeMulConstantRight(Function.MulNode node, Function.ValueNode valueNode){
        if (node.getLeftChild() instanceof Function.MulNode mulNode){

            // simplify (2*u(x))*3 to 6*u(x)
            if (mulNode.getLeftChild() instanceof Function.ValueNode mulLeft){

                Function.MulNode optimMulNode =
                        new Function.MulNode(new Function.ValueNode(mulLeft.getValue() * valueNode.getValue()),
                                mulNode.getRightChild());
                replaceChild(node.getParent(), node, optimMulNode);
            }

            // simplify (u(x)*2)*3 to 6*u(x)
            else if (mulNode.getRightChild() instanceof Function.ValueNode mulRight){
                Function.MulNode optimMulNode =
                        new Function.MulNode(new Function.ValueNode(mulRight.getValue() * valueNode.getValue()),
                                mulNode.getLeftChild());
                replaceChild(node.getParent(), node, optimMulNode);
            }
        }

        return null;
    }


    private Void optimizeMulVariableRight(Function.MulNode node, Function.VariableNode variableNode){

        // optimize x*x = x^2
        if (node.getLeftChild() instanceof Function.VariableNode left){
            if (left.getText().equals(variableNode.getText())){
                replaceChild(node.getParent(), node, new Function.PowNode(left, new Function.ValueNode(2)));
            }
        }

        else if (node.getLeftChild() instanceof Function.MulNode mulNode){

            // simplify (2*x)*x to 2*x^2
            if (mulNode.getLeftChild() instanceof Function.ValueNode mulLeft
                    && mulNode.getRightChild() instanceof Function.VariableNode mulRight){
                if (variableNode.getText().equals(mulRight.getText())) {
                    Function.MulNode optimMulNode =
                            new Function.MulNode(
                                    mulLeft,
                                    new Function.PowNode(variableNode, new Function.ValueNode(2))
                            );
                    replaceChild(node.getParent(), node, optimMulNode);
                }
            }

            // simplify (x*2)*x to 2*x^2
            else if (mulNode.getRightChild() instanceof Function.ValueNode mulRight
                    && mulNode.getLeftChild() instanceof Function.VariableNode mulLeft){
                if (variableNode.getText().equals(mulLeft.getText())) {
                    Function.MulNode optimMulNode =
                            new Function.MulNode(
                                    mulRight,
                                    new Function.PowNode(variableNode, new Function.ValueNode(2))
                            );
                    replaceChild(node.getParent(), node, optimMulNode);
                }
            }
        }

        else if (node.getLeftChild() instanceof Function.PowNode powNode){

            if (powNode.getLeftChild() instanceof Function.VariableNode left){
                if (variableNode.getText().equals(left.getText())) {
                    // simplify (x^2)*x to x^3
                    if (powNode.getRightChild() instanceof Function.ValueNode right) {
                        Function.PowNode optimPowNode =
                                new Function.PowNode(
                                        variableNode,
                                        new Function.ValueNode(right.getValue() + 1)
                                );

                        replaceChild(node.getParent(), node, optimPowNode);
                    }

                    else {
                        // simplify (x^u(x))*x to x^(u(x)+1)
                        Function.PowNode optimPowNode =
                                new Function.PowNode(
                                        variableNode,
                                        new Function.AddNode(powNode.getRightChild(), new Function.ValueNode(1))
                                );
                        replaceChild(node.getParent(), node, optimPowNode);
                        optimPowNode.accept(this);
                    }
                }
            }
        }
        return null;
    }

    public Void optimizeMulPowRight(Function.MulNode node, Function.PowNode powNodeRight){

        if (node.getLeftChild() instanceof Function.PowNode powNodeLeft) {
            if(powNodeLeft.getLeftChild().accept(new EqualityCheckVisitor(powNodeRight.getLeftChild()))){
                replaceChild(node.getParent(), node, new Function.PowNode(
                        powNodeLeft.getLeftChild(),
                        new Function.AddNode(
                                powNodeLeft.getRightChild(),
                                powNodeRight.getRightChild()
                        )
                ));
                node.getParent().accept(this);
            }
        }

        return null;
    }


    @Override
    public Void visitDivNode(Function.DivNode node) {
        node.getLeftChild().accept(this);
        node.getRightChild().accept(this);

        if (node.getLeftChild() instanceof Function.ValueNode left
                && node.getRightChild() instanceof Function.ValueNode right){
            Function.ValueNode simplifiedNode = new Function.ValueNode(left.getValue() / right.getValue());
            replaceChild(node.getParent(), node, simplifiedNode);
        }
        else if (node.getRightChild() instanceof Function.ValueNode valueNode){
            if (valueNode.getValue() == 1.0){
                replaceChild(node.getParent(), node, node.getLeftChild());
            }
        }
        if (node.getLeftChild() instanceof Function.VariableNode left
                && node.getRightChild() instanceof Function.VariableNode right){
            if (left.getText().equals(right.getText())){
                replaceChild(node.getParent(), node, new Function.ValueNode(1));
            }
        }
        else if (node.getLeftChild().accept(new EqualityCheckVisitor(node.getRightChild()))){
            replaceChild(node.getParent(), node, new Function.ValueNode(1));
            node.getParent().accept(this);
        }
        return null;
    }

    @Override
    public Void visitModNode(Function.ModNode node) {
        node.getLeftChild().accept(this);
        node.getRightChild().accept(this);

        if (node.getLeftChild() instanceof Function.ValueNode left
                && node.getRightChild() instanceof Function.ValueNode right){
            Function.ValueNode simplifiedNode = new Function.ValueNode(left.getValue() % right.getValue());
            replaceChild(node.getParent(), node, simplifiedNode);
        }
        if (node.getLeftChild().accept(new EqualityCheckVisitor(node.getRightChild()))){
            replaceChild(node.getParent(), node, new Function.ValueNode(0));
            node.getParent().accept(this);
        }
        else if (node.getLeftChild() instanceof Function.ValueNode left){
            if (left.getValue() == 0){
                replaceChild(node.getParent(), node, new Function.ValueNode(0));
            }
        }
        else if (node.getRightChild() instanceof Function.ValueNode right){
            if (right.getValue() == 0 || right.getValue() == 1){
                replaceChild(node.getParent(), node, new Function.ValueNode(0));
            }
        }

        return null;
    }

    @Override
    public Void visitPowNode(Function.PowNode node) {
        node.getLeftChild().accept(this);
        node.getRightChild().accept(this);

        if (node.getLeftChild() instanceof Function.ValueNode left
                && node.getRightChild() instanceof Function.ValueNode right){
            Function.ValueNode simplifiedNode = new Function.ValueNode(Math.pow(left.getValue(), right.getValue()));
            replaceChild(node.getParent(), node, simplifiedNode);
        }
        else if (node.getRightChild() instanceof Function.ValueNode valueNode){
            if (valueNode.getValue() == 1.0){
                replaceChild(node.getParent(), node, node.getLeftChild());
            }
            else if (valueNode.getValue() == 0.0){
                replaceChild(node.getParent(), node, new Function.ValueNode(1));
            }
        }
        if (node.getLeftChild() instanceof Function.ValueNode left){
            if (left.getValue() == 1){
                replaceChild(node.getParent(), node, new Function.ValueNode(1));
            }
            else if (left.getValue() == 0){
                replaceChild(node.getParent(), node, new Function.ValueNode(0));
            }
        }
        else if (node.getRightChild() instanceof Function.PowNode exponent){
            node.setRightChild(new Function.MulNode(
                    exponent.getLeftChild(),
                    exponent.getRightChild()
            ));
            node.getParent().accept(this);
        }
        else if (node.getLeftChild() instanceof Function.PowNode base){
            replaceChild(node.getParent(), node, new Function.PowNode(
                   base.getLeftChild(),
                   new Function.MulNode(
                           base.getRightChild(),
                           node.getRightChild()
                   )
            ));
            node.getParent().accept(this);
        }
        return null;
    }

    @Override
    public Void visitCallNode(Function.CallNode node) {
        node.getChild().accept(this);

        if (node.getArgument() instanceof Function.ValueNode valueNode){
            replaceChild(node.getParent(), node, new Function.ValueNode(
                    node.getBuildInFunction().eval(valueNode.getValue())));
        }
        else if (node.getArgument() instanceof Function.CallNode argNode){
            if (node.getName().equals("exp") && argNode.getName().equals("ln")
                || node.getName().equals("ln") && argNode.getName().equals("exp")){
                replaceChild(node.getParent(), node, argNode.getChild());
            }
        }

        return null;
    }

    @Override
    public Void visitNegationNode(Function.NegationNode node) {
        node.getChild().accept(this);

        Function.Node parent = node.getParent();
        if (parent instanceof Function.NegationNode negationNode){
            Function.Node grandParent = negationNode.getParent();
            replaceChild(grandParent, negationNode, node.getChild());
        }
        else if (node.getChild() instanceof Function.ValueNode valueNode){
            replaceChild(parent, node, new Function.ValueNode(-valueNode.getValue()));
        }

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

    public <T extends Function.BinaryNode> T swapChildren(T node){
        Function.Node left = node.getLeftChild();
        node.setLeftChild(node.getRightChild());
        node.setRightChild(left);
        return node;
    }

}
