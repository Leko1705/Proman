package mylib.functional;

interface FunctionVisitor<T> {

    T visitVariableNode(Function.VariableNode node);
    T visitValueNode(Function.ValueNode node);
    T visitAddNode(Function.AddNode node);
    T visitMulNode(Function.MulNode node);
    T visitDivNode(Function.DivNode node);
    T visitModNode(Function.ModNode node);
    T visitPowNode(Function.PowNode node);
    T visitCallNode(Function.CallNode node);
    T visitNegationNode(Function.NegationNode node);

}
