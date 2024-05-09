package data.xml;

import data.Content;
import data.Data;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class XMLData implements Data {

    protected final Document doc;
    private final XMLContent root;

    public XMLData(Document doc) {
        this.doc = doc;
        this.root = new XMLContent(doc.getDocumentElement());
    }

    @Override
    public String getKind() {
        return root.element.getElementsByTagName("kind").item(0).getTextContent();
    }

    @Override
    public int getVersion() {
        return Integer.parseInt(root.element.getElementsByTagName("version").item(0).getTextContent());
    }

    @Override
    public Content getContent() {
        return root;
    }


    private class XMLContent implements Content {

        private final Element element;

        private XMLContent(Element node) {
            this.element = node;
        }

        @Override
        public String getTag() {
            return element.getTagName();
        }

        @Override
        public String getValue() {
            return element.getTextContent();
        }

        @Override
        public void setValue(String value) {
            element.setTextContent(value);
        }

        @Override
        public Content getOrCreateChild(String child) {
            Node node = element.getElementsByTagName(child).item(0);
            if (node != null){
                return new XMLContent((Element) node);
            }
            return new XMLContent(createNewNode(child));
        }

        private Element createNewNode(String tag) {
            Element newElement = doc.createElement(tag);
            element.appendChild(newElement);
            return newElement;
        }

        @Override
        public void clear() {
            while (element.hasChildNodes())
                element.removeChild(element.getFirstChild());
        }

        @Override
        public List<Content> getChildren() {
            List<Content> children = new ArrayList<>();

            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element child = (Element) node;
                    children.add(new XMLContent(child));
                }
            }

            return children;
        }

    }

}
