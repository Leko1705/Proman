package diagrams.clazz.graph.node;

import mylib.structs.ObservedList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Method
        extends ClassElement {

    private Visibility visibility;

    private String type;

    private String name;

    private final ObservedList<Parameter> parameters = new ObservedList<>(new ArrayList<>());

    private boolean isStatic = false;

    private boolean isAbstract = false;

    public Method(Visibility visibility, String name, String type) {
        setVisibility(visibility);
        setName(name);
        setType(type);
    }

    public Method(Visibility visibility, String name, String type, boolean isStatic, boolean isAbstract) {
        this(visibility, name, type);
        this.isStatic = isStatic;
        this.isAbstract = isAbstract;
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

    public List<Parameter> getParameters() {
        return parameters;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
        notifyClassChanged();
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
        notifyClassChanged();
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder(visibility.getTextFormat())
                .append(name)
                .append('(');

        if (!parameters.isEmpty()){
            Iterator<Parameter> iter = parameters.iterator();
            sb.append(iter.next().getText());
            while (iter.hasNext()){
                sb.append(", ").append(iter.next().getText());
            }
        }

        sb.append("): ").append(type);

        return sb.toString();
    }

    @Override
    protected void setClassChangeListener(ClassChangeListener classChangeListener) {
        super.setClassChangeListener(classChangeListener);
        parameters.addListChangeListener(classChangeListener);
    }
}
