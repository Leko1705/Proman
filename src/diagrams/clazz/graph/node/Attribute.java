package diagrams.clazz.graph.node;

import java.util.Objects;

public class Attribute
        extends ClassElement {

    String type;

    String name;

    private Visibility visibility;

    private boolean isStatic = false;

    private boolean hasGetter = false;

    private boolean hasSetter = false;


    public Attribute(Visibility visibility, String type, String name) {
        setVisibility(visibility);
        setType(type);
        setName(name);
    }

    public Attribute(Visibility visibility, String type, String name, boolean isStatic) {
        this(visibility, type, name);
        this.isStatic = isStatic;
    }


    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = Objects.requireNonNull(visibility);
        notifyClassChanged();
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

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
        notifyClassChanged();
    }

    public boolean hasSetter() {
        return hasSetter;
    }

    public void setSetter(boolean hasSetter) {
        this.hasSetter = hasSetter;
        notifyClassChanged();
    }

    public boolean hasGetter() {
        return hasGetter;
    }

    public void setGetter(boolean hasGetter) {
        this.hasGetter = hasGetter;
        notifyClassChanged();
    }

    @Override
    public String getText() {
        return visibility.getTextFormat() + name + ": " + type;
    }

}
