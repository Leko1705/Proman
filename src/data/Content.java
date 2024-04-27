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

    default Content removeChild(String tag) {
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i).getTag().equals(tag)) {
                return this.getChildren().remove(i);
            }
        }
        return null;
    }

    List<Content> getChildren();

}
