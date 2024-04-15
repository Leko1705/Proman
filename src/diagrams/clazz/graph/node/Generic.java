package diagrams.clazz.graph.node;

import mylib.structs.ObservedList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Generic
        extends ClassElement {

    private String name;

    private final ObservedList<String> extensions = new ObservedList<>(new ArrayList<>());

    public Generic(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
        notifyClassChanged();
    }

    public List<String> getExtensions() {
        return extensions;
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder(name);

        if (!extensions.isEmpty()){
            Iterator<String> iterator = extensions.iterator();
            sb.append(" -> ").append(iterator.next());
            while (iterator.hasNext()){
                sb.append(" & ").append(iterator.next());
            }
        }

        return sb.toString();
    }

    @Override
    protected void setClassChangeListener(ClassChangeListener classChangeListener) {
        super.setClassChangeListener(classChangeListener);
        extensions.addListChangeListener(classChangeListener);
    }
}
