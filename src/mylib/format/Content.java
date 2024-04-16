package mylib.format;

import java.util.List;

public interface Content {

    String getRootName();

    Object get(String... path);

    void set(Object o, String... path);

    void clear(String... path);

    List<Content> getSubContent(String... path);

    default Object get(List<String> path) {
        return get(path.toArray(new String[0]));
    }

    default void set(Object o, List<String> path) {
        set(o, path.toArray(new String[0]));
    }


}
