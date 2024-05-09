package data;

import java.util.List;

public interface Content {

    String getTag();

    String getValue();

    void setValue(String value);

    default Content getChild(String tag) {
        if (tag == null) throw new NullPointerException("child is null");

        for (Content childContent : getChildren()) {
            String childValue = childContent.getTag();
            if (childValue.equals(tag)) {
                return childContent;
            }
        }
        return null;
    }

    Content getOrCreateChild(String child);

    void clear();

    List<Content> getChildren();

}
