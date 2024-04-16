package diagram.store;

import mylib.format.Content;

import java.util.List;

public class DiagramData implements Content {

    private final Content content;

    public DiagramData(Content content) {
        this.content = content;
    }

    @Override
    public String getRootName() {
        return content.getRootName();
    }

    @Override
    public Object get(String... path) {
        return content.get(path);
    }

    @Override
    public void set(Object o, String... path) {
        content.set(o, path);
    }

    @Override
    public void clear(String... path) {
        content.clear(path);
    }

    @Override
    public List<Content> getSubContent(String... path) {
        return content.getSubContent(path);
    }

    public Content getContent() {
        return content;
    }
    
    public int getVersion() {
        return Integer.parseInt((String) content.get("diagram", "version"));
    }

    public String getKind() {
        return (String) content.get("diagram", "kind");
    }

    @Override
    public String toString() {
        return content.toString();
    }
}
