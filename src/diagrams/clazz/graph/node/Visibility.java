package diagrams.clazz.graph.node;

public enum Visibility {

    PUBLIC("+"),

    PROTECTED("#"),

    PRIVATE("-"),

    DEFAULT("~"),;


    private final String textFormat;

    Visibility(String textFormat) {
        this.textFormat = textFormat;
    }

    public String getTextFormat() {
        return textFormat;
    }

}
