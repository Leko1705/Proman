package diagrams.clazz.graph.node;

import java.util.Objects;

public class Parameter
        extends ClassElement {

    private String type;

    private String name;

    public Parameter(String type, String name) {
        setType(type);
        setName(name);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = Objects.requireNonNull(type);
        notifyClassChanged();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
        notifyClassChanged();
    }

    @Override
    public String getText() {
        return name + ": " + type;
    }
}
