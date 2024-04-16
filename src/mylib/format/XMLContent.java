package mylib.format;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record XMLContent(Document document) implements Content {

    @Override
    public String getRootName() {
        return document.getDocumentElement().getNodeName();
    }

    @Override
    public Object get(String... path) {
        Element element = getElement(path);
        return element != null ? element.getTextContent() : null;
    }

    @Override
    public void set(Object o, String... path) {

        Element element = document.getDocumentElement();

        if (!element.getNodeName().equals(path[0])) {
            return;
        }

        for (int i = 1; i < path.length; i++) {
            String tag = path[i];
            NodeList nodes = element.getElementsByTagName(tag);
            if (nodes.getLength() == 0) {
                expand(path, i, element, o);
                return;
            }
            element = (Element) nodes.item(0);
        }

        element.setTextContent(Objects.toString(o));
    }


    private void expand(String[] path, int index, Element parent, Object o){

        for (int i = index; i < path.length; i++) {
            String tag = path[i];
            Element newElement = document.createElement(tag);
            parent.insertBefore(newElement, null);
            parent = newElement;
        }

        parent.setTextContent(Objects.toString(o));
    }

    @Override
    public void clear(String... path) {
        if (path.length == 0) return;

        Element element = document.getDocumentElement();
        if (!element.getNodeName().equals(path[0])) return;

        NodeList nodes = null;

        for (int i = 1; i < path.length; i++) {
            String tag = path[i];
            nodes = element.getElementsByTagName(tag);
            if (nodes.getLength() == 0) return;
            element = (Element) nodes.item(0);
        }

        if (nodes == null) return;

        nodes = element.getElementsByTagName("*");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            node.getParentNode().removeChild(node);
        }
    }

    @Override
    public List<Content> getSubContent(String... path) {
        Element element = getElement(path);
        if (element == null) return null;

        List<Content> subContents = new ArrayList<>();
        NodeList nodes = element.getElementsByTagName("*");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            Document newDocument = getNewDocument();
            assert newDocument != null;
            newDocument.appendChild(newDocument.importNode(node, true));
            subContents.add(new XMLContent(newDocument));
        }

        return subContents;
    }

    private Document getNewDocument() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.newDocument();
        } catch (ParserConfigurationException ex) {
            return null;
        }
    }


    private Element getElement(String... path){
        Element element = document.getDocumentElement();
        String tag = path[0];

        if (!tag.equals(element.getNodeName()))
            return null;

        for (int i = 1; i < path.length; i++) {
            tag = path[i];
            NodeList nodes = element.getElementsByTagName(tag);
            if (nodes.getLength() == 0) return null;
            element = (Element) nodes.item(0);
        }

        return element;
    }

    @Override
    public String toString() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //transformer.setOutputProperty("{https://xml.apache.org/xslt}indent-amount", "2");
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(document);
            transformer.transform(source, result);
            return result.getWriter().toString();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
