package diagrams.clazz.graph.node;

public enum ClassType {

    CLASS("Class"),

    INTERFACE("<<interface>>"),

    ANNOTATION("<<annotation>>"),

    ABSTRACT("{abstract}"),

    RECORD("Record"),

    ENUMERATION("<<enumeration>>");

    private final String textFormat;

    ClassType(String textFormat) {
        this.textFormat = textFormat;
    }

    public String getTextFormat() {
        return textFormat;
    }

    @Override
    public String toString() {
        return textFormat;
    }

    public static ClassType fromTextFormat(String textFormat) {
        for (ClassType classType : ClassType.values()) {
            if (classType.textFormat.equals(textFormat)) {
                return classType;
            }
        }
        return null;
    }

}
