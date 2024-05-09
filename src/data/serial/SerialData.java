package data.serial;

import data.Content;
import data.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class SerialData implements Data, Serializable {

    private final String kind;
    private final int version;
    private final SerialContent content;

    public SerialData(String kind, int version) {
        this.kind = kind;
        this.version = version;
        this.content = new SerialContent("root");
    }
    @Override
    public String getKind() {
        return kind;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public Content getContent() {
        return content;
    }

    private static class SerialContent implements Content, Serializable {

        private final String tag;
        private String value;
        private final List<Content> children = new ArrayList<>();

        private SerialContent(String tag) {
            this.tag = tag;
        }

        @Override
        public String getTag() {
            return tag;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public Content getOrCreateChild(String tag) {
            Content childContent = getChild(tag);
            if (childContent == null) {
                childContent = new SerialContent(tag);
                children.add(childContent);
            }
            return childContent;
        }

        @Override
        public void clear() {
            children.clear();
        }

        @Override
        public List<Content> getChildren() {
            return children;
        }
    }
}
