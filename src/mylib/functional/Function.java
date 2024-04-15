package mylib.functional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

@SuppressWarnings("unused")
public class Function implements Functional, Cloneable {

    private Node node;

    private final char[] vars;
    private String term;


    public static Function derivation(String term, char... var){
        return new Function(term, var).derivative();
    }


    public Function(String term, char... vars){
        this.term = term;
        this.vars = vars;
        checkSignature(term, vars);
        compile(term);
    }

    public Function(Function f){
        Function fc = f.clone();
        this.term = fc.term;
        this.vars = fc.vars;
        this.node = fc.node;
    }

    private void compile(String term){
        term = preprocess(term);
        FunctionLexer lexer = new FunctionLexer(term);
        FunctionParser parser = new FunctionParser(lexer, vars);
        node = new RootNode(parser.parse());
        node.accept(new SimplificationVisitor()); // optimize expression
    }

    private String preprocess(String term){
        term = term.replaceAll("-", "+-");
        if (term.startsWith("+")) term = term.substring(1);
        term = term.replaceAll("⁰", "^0");
        term = term.replaceAll("¹", "^1");
        term = term.replaceAll("²", "^2");
        term = term.replaceAll("³", "^3");
        term = term.replaceAll("⁴", "^4");
        term = term.replaceAll("⁵", "^5");
        term = term.replaceAll("⁶", "^6");
        term = term.replaceAll("⁷", "^7");
        term = term.replaceAll("⁸", "^8");
        term = term.replaceAll("⁹", "^9");
        return term;
    }

    public Double eval(){
        return eval(new Double[0]);
    }

    public Double eval(int... input){
        return eval(wrapIntegers(input));
    }

    @Override
    public Double eval(Double... input) {
        checkInput(input);
        HashMap<Character, Double> params = new HashMap<>();
        for (int i = 0; i < input.length; i++){
            params.put(vars[i], input[i]);
        }
        return node.accept(new EvalVisitor(params));
    }

    @Override
    public double gradient(Double... input) {
        return Functional.super.gradient(input);
    }

    public double gradient(int... input) {
        return gradient(wrapIntegers(input));
    }

    public double gradient() {
        return Functional.super.gradient();
    }

    private Double[] wrapIntegers(int[] input){
        Double[] wrappers = new Double[input.length];
        for (int i = 0; i < input.length; i++)
            wrappers[i] = (double) input[i];
        return wrappers;
    }

    public Function derivative(){
        if (vars.length == 0)
            return new Function("0");
        StringBuilder term = new StringBuilder();
        term.append(node.accept(new DerivationVisitor(vars[0])));
        for (int i = 1; i < vars.length; i++){
            term.append("*").append(node.accept(new DerivationVisitor(vars[i])));
        }
        return new Function(term.toString(), vars);
    }

    public boolean isConstant(){
        return node.getChild() instanceof ValueNode;
    }

    public boolean isLinear(){
        return node.accept(new LinearCheckVisitor());
    }

    public Function link(Function f){
        Objects.requireNonNull(f);
        if (vars.length != f.vars.length) throw new IllegalArgumentException("variables does not match");

        HashSet<Character> cache = new HashSet<>();
        for (char var : vars) cache.add(var);
        for (char var : f.vars) if (!cache.contains(var)) throw new IllegalArgumentException("variables does not match");

        Function copy = this.clone();
        copy.node.accept(new LinkVisitor(f.node.getChild()));
        copy.term = copy.node.accept(new StringifyVisitor());
        copy.node.accept(new SimplificationVisitor());
        return copy;
    }


    public boolean isVariable(char c){
        return Arrays.binarySearch(vars, c) < 0;
    }

    public String getTerm() {
        return term;
    }

    public String getSimplifiedTerm(){
        return node.accept(new StringifyVisitor());
    }

    private void checkSignature(String term, char[] vars){
        Objects.requireNonNull(term);
        Objects.requireNonNull(vars);
        if (term.isEmpty())
            throw new FunctionSyntaxException("empty term");
    }

    private void checkInput(Double[] input){
        Objects.requireNonNull(input);

        if (input.length != vars.length)
            throw new IllegalArgumentException("invalid param amount " + input.length + " for " + vars.length);

        for (Double d : input)
            Objects.requireNonNull(d);
    }

    @Override
    public String toString() {
        return getTerm();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Function f)) return false;
        if (vars.length != f.vars.length) return false;

        HashSet<Character> cache = new HashSet<>();
        for (char var : vars) cache.add(var);
        for (char var : f.vars) if (!cache.contains(var)) return false;

        return node.accept(new EqualityCheckVisitor(f.node.getChild()));
    }

    @Override
    public Function clone() {
        try {
            return (Function) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }


    // ---------------- Abstract Syntax Tree Nodes ----------------


    interface Node extends Cloneable {
        Node getParent();
        void setParent(Node parent);
        <T> T accept(FunctionVisitor<T> visitor);
        default Node getChild(){
            return null;
        }
        default void setChild(Node child){}
    }


    abstract static class AbstractNode implements Node {
        private Node parent;
        public Node getParent() {
            return parent;
        }
        public void setParent(Node parent) {
            this.parent = parent;
        }
    }


    static class RootNode extends AbstractNode {
        private Node child;
        public RootNode(Node child){
            this.child = child;
            this.child.setParent(this);
        }
        public <T> T accept(FunctionVisitor<T> visitor) {
            return child.accept(visitor);
        }
        public void setChild(Node child) {
            this.child = child;
        }
        public Node getChild() {
            return child;
        }
    }


    abstract static class BinaryNode extends AbstractNode {
        private Node leftChild, rightChild;
        public BinaryNode(Node leftChild, Node rightChild) {
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.leftChild.setParent(this);
            this.rightChild.setParent(this);
        }
        public Node getLeftChild() {
            return leftChild;
        }
        public Node getRightChild() {
            return rightChild;
        }
        public void setLeftChild(Node leftChild) {
            this.leftChild = leftChild;
        }
        public void setRightChild(Node rightChild) {
            this.rightChild = rightChild;
        }
    }


    static class AddNode extends BinaryNode {
        public AddNode(Node leftChild, Node rightChild) {
            super(leftChild, rightChild);
        }
        public <T> T accept(FunctionVisitor<T> visitor) {
            return visitor.visitAddNode(this);
        }
    }


    static class CallNode extends AbstractNode {
        private Node argument;
        private final String name;
        private final BuildInFunction buildInFunction;
        public CallNode(String name, Node argument, BuildInFunction f){
            this.name = name;
            this.argument = argument;
            this.argument.setParent(this);
            this.buildInFunction = f;
        }
        public Node getArgument() {
            return argument;
        }
        public String getName() {
            return name;
        }
        public Node getChild() {
            return argument;
        }
        public void setChild(Node child) {
            this.argument = child;
        }
        public BuildInFunction getBuildInFunction() {
            return buildInFunction;
        }
        public <T> T accept(FunctionVisitor<T> visitor) {
            return visitor.visitCallNode(this);
        }
    }

    interface BuildInFunction{
        double eval(double x);
    }

    static class DivNode extends BinaryNode {
        public DivNode(Node leftChild, Node rightChild) {
            super(leftChild, rightChild);
        }
        public <T> T accept(FunctionVisitor<T> visitor) {
            return visitor.visitDivNode(this);
        }
    }


    static class ModNode extends BinaryNode {
        public ModNode(Node leftChild, Node rightChild) {
            super(leftChild, rightChild);
        }
        public <T> T accept(FunctionVisitor<T> visitor) {
            return visitor.visitModNode(this);
        }
    }


    static class MulNode extends BinaryNode {
        public MulNode(Node leftChild, Node rightChild) {
            super(leftChild, rightChild);
        }
        public <T> T accept(FunctionVisitor<T> visitor) {
            return visitor.visitMulNode(this);
        }
    }


    static class PowNode extends BinaryNode {
        public PowNode(Node leftChild, Node rightChild) {
            super(leftChild, rightChild);
        }
        public <T> T accept(FunctionVisitor<T> visitor) {
            return visitor.visitPowNode(this);
        }
    }


    static class NegationNode extends AbstractNode {
        private Node child;
        public NegationNode(Node child){
            this.child = child;
            this.child.setParent(this);
        }
        public Node getChild() {
            return child;
        }
        public void setChild(Node child) {
            this.child = child;
        }
        public <T> T accept(FunctionVisitor<T> visitor) {
            return visitor.visitNegationNode(this);
        }
    }


    static class ValueNode extends AbstractNode {
        private final double value;
        public ValueNode(String value){
            this.value = Double.parseDouble(value);
        }
        public ValueNode(double value){
            this.value = value;
        }
        public double getValue() {
            return value;
        }
        public <T> T accept(FunctionVisitor<T> visitor) {
            return visitor.visitValueNode(this);
        }
    }


    static class VariableNode extends AbstractNode {
        private final Character text;
        public VariableNode(String name){
            this.text = name.charAt(0);
        }
        public Character getText() {
            return text;
        }
        public <T> T accept(FunctionVisitor<T> visitor) {
            return visitor.visitVariableNode(this);
        }
    }



    // ---------------- Lexer class ----------------


    static class FunctionLexer {
        private static final Token END_TOKEN = new Token(Token.END, null);
        private static final Token OPEN_BRACKET_TOKEN = new Token(Token.BRACKET_OPEN, "(");
        private static final Token CLOSED_BRACKET_TOKEN = new Token(Token.BRACKET_CLOSED, ")");
        private static final Token ADD_TOKEN = new Token(Token.ADD, "+");
        private static final Token SUB_TOKEN = new Token(Token.SUB, "-");
        private static final Token MUL_TOKEN = new Token(Token.MUL, "*");
        private static final Token DIV_TOKEN = new Token(Token.DIV, "/");
        private static final Token MOD_TOKEN = new Token(Token.MOD, "%");
        private static final Token POW_TOKEN = new Token(Token.POW, "^");

        private final String term;
        private final int length;

        private int pos = 0;

        private Token head = null;

        public FunctionLexer(String term){
            this.term = term;
            this.length = term.length();
            poll();
        }

        public Token peek(){
            return head;
        }

        public Token poll(){
            Token returned = head;
            head = scan();
            return returned;
        }

        public boolean hasNext(){
            return head != END_TOKEN;
        }

        private Token scan(){

            skipWhitespace();
            if (pos >= length)
                return END_TOKEN;

            char c = term.charAt(pos);

            if (Character.isDigit(c) || c == '.'){
                return createDigitToken();
            }
            else if (Character.isLetter(c)){
                return createIdentifierToken();
            }

            pos++;
            if (c == '(') {
                return OPEN_BRACKET_TOKEN;
            }
            else if (c == ')'){
                return CLOSED_BRACKET_TOKEN;
            }
            else if (c == '+'){
                return ADD_TOKEN;
            }
            else if (c == '-'){
                return SUB_TOKEN;
            }
            else if (c == '*'){
                return MUL_TOKEN;
            }
            else if (c == '/'){
                return DIV_TOKEN;
            }
            else if (c == '%'){
                return MOD_TOKEN;
            }
            else if (c == '^'){
                return POW_TOKEN;
            }

            throw new FunctionSyntaxException("unexpected char '" + c + "'");
        }

        private void skipWhitespace(){
            while (pos < length && Character.isWhitespace(term.charAt(pos)))
                pos++;
        }

        private Token createDigitToken(){
            StringBuilder val = new StringBuilder();
            char c = term.charAt(pos);
            boolean dotFound = false;
            while (pos < length && ((Character.isDigit(c)) || c == '.')){
                val.append(c);
                if (c == '.'){
                    if (dotFound)
                        throw new FunctionSyntaxException("too many '.' in " + val);
                    dotFound = true;
                }
                if (++pos < length)
                    c = term.charAt(pos);
            }
            return new Token(Token.NUMERIC, val.toString());
        }

        private Token createIdentifierToken() {
            StringBuilder val = new StringBuilder();
            char c = term.charAt(pos);
            while (pos < length && Character.isLetter(c)){
                val.append(c);
                if (++pos < length)
                    c = term.charAt(pos);
            }
            return new Token(Token.IDENTIFIER, val.toString());
        }

    }



    // ---------------- Parser class ----------------



    static class FunctionParser {

        private final FunctionLexer lexer;
        private final HashSet<Character> vars;

        public FunctionParser(FunctionLexer lexer, char[] vars){
            this.lexer = lexer;
            this.vars = new HashSet<>();
            for (char c : vars)
                this.vars.add(c);
        }

        public Node parse(){
            return parseAdd();
        }

        private Node parseAdd(){
            Node exp = parseMul();

            while (lexer.hasNext() && lexer.peek().type() != Token.BRACKET_CLOSED){
                Token next = lexer.poll();
                if (!isAddOperator(next.type()))
                    throw new FunctionSyntaxException("unexpected token '" + next.value() + "'");
                exp = new AddNode(exp, parseMul());
            }

            return exp;
        }

        private Node parseMul(){
            Node exp = parsePow();

            while (lexer.hasNext()){
                Token next = lexer.peek();
                if (!isMulOperator(next.type()))
                    break;
                lexer.poll();
                exp = createMulOperatorNode(exp, parsePow(), next.type());
            }

            return exp;
        }

        private Node parsePow(){
            Node exp = parsePrimitive();

            while (lexer.hasNext()){
                Token next = lexer.peek();
                if (!isPowOperator(next.type()))
                    break;
                lexer.poll();
                exp = new PowNode(exp, parsePrimitive());
            }

            return exp;
        }

        private Node parsePrimitive(){
            Token next = lexer.poll();
            if (next.type() == Token.NUMERIC){

                return new ValueNode(next.value());
            }
            else if (next.type() == Token.IDENTIFIER){
                if (lexer.peek().type() != Token.BRACKET_OPEN){
                    if (next.value().length() > 1)
                        return new ValueNode(checkBuildInConstant(next.value()));
                    else if (!vars.contains(next.value().charAt(0)))
                        throw new FunctionSyntaxException("can not find symbol '" + next.value() + "'");
                    return new VariableNode(next.value());
                }
                else {
                    checkBuildInFunction(next.value());
                    lexer.poll();
                    Node node = new CallNode(next.value(), parse(), buildIns.get(next.value));
                    next = lexer.poll();
                    if (next.type() != Token.BRACKET_CLOSED)
                        throw new FunctionSyntaxException("missing ')'");
                    return node;
                }
            }
            else if (next.type() == Token.SUB){
                return parseNegation();
            }
            else if (next.type() == Token.BRACKET_OPEN){
                Node node = parse();
                next = lexer.poll();
                if (next.type() != Token.BRACKET_CLOSED)
                    throw new FunctionSyntaxException("missing ')");
                return node;
            }
            if (next.type() == Token.ADD){
                if (lexer.peek().type() == Token.SUB){
                    return parsePrimitive();
                }
            }
            throw new FunctionSyntaxException("unexpected token '" + next.value() + "'");
        }

        private Node parseNegation(){
            return new NegationNode(parsePrimitive());
        }
        private boolean isAddOperator(int o){
            return o == Token.ADD;
        }
        private boolean isMulOperator(int o){
            return o == Token.MUL || o == Token.DIV || o == Token.MOD;
        }
        private boolean isPowOperator(int o){
            return o == Token.POW;
        }

        private Node createMulOperatorNode(Node left, Node right, int o){
            if (o == Token.MUL)
                return new MulNode(left, right);
            else if (o == Token.DIV)
                return new DivNode(left, right);
            else if (o == Token.MOD)
                return new ModNode(left ,right);
            else
                throw new IllegalStateException();
        }

        private void checkBuildInFunction(String name){
            if (!buildIns.containsKey(name))
                throw new FunctionSyntaxException("unknown build in function '" + name + "'");
        }

        private double checkBuildInConstant(String constant){
            return switch (constant){
                case "pi" -> Math.PI;
                default -> throw new FunctionSyntaxException("variable must be exactly one a char");
            };

        }

    }

    record Token(int type, String value) {

        public static final int NUMERIC = 0;
        public static final int BRACKET_OPEN = 1;
        public static final int BRACKET_CLOSED = 2;
        public static final int IDENTIFIER = 3;
        public static final int ADD = 4;
        public static final int SUB = 5;
        public static final int MUL = 6;
        public static final int DIV = 7;
        public static final int MOD = 8;
        public static final int POW = 9;
        public static final int END = -1;
    }

    private static final HashMap<String, BuildInFunction> buildIns = new HashMap<>(){{
        put("exp", Math::exp);
        put("ln", Math::log);
        put("log", Math::log10);
        put("log2", x -> Math.log(x) / Math.log(2));
        put("sqrt", Math::sqrt);
        put("sin", Math::sin);
        put("asin", Math::asin);
        put("cos", Math::cos);
        put("acos", Math::acos);
        put("tan", Math::tan);
        put("atan", Math::atan);
        put("tanh", Math::tanh);
    }};

}
