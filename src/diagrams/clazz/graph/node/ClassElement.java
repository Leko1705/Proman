package diagrams.clazz.graph.node;

public abstract class ClassElement {

    private ClassChangeListener classChangeListener;

    protected void setClassChangeListener(ClassChangeListener classChangeListener) {
        this.classChangeListener = classChangeListener;
    }

    public void notifyClassChanged() {
        if (classChangeListener != null)
            classChangeListener.onChange();

    }

    public abstract String getText();

    @Override
    public String toString() {
        return getText();
    }
}
