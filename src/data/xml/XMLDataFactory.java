package data.xml;

import data.Data;
import data.DataFactory;
import mylib.io.BuffedFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public class XMLDataFactory implements DataFactory {

    @Override
    public void createFile(Path path, String kind, int version) {
        try(BuffedFile file = new BuffedFile(path.toAbsolutePath().toString()))
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element root = doc.createElement("root");
            doc.appendChild(root);

            Element versionElement = doc.createElement("version");
            versionElement.setTextContent(Integer.toString(version));
            root.appendChild(versionElement);

            Element kindElement = doc.createElement("kind");
            kindElement.setTextContent(kind);
            root.appendChild(kindElement);

            Element contentElement = doc.createElement("content");
            contentElement.setTextContent(kind);
            root.appendChild(contentElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{https://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Data load(InputStream in) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            documentBuilderFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(in);
            return new XMLData(document);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Data data, OutputStream out) {
        if (!(data instanceof XMLData xmlData)) {
            throw new IllegalArgumentException("Data must be of type XMLData");
        }

        clearBlankLine(xmlData.doc.getDocumentElement());

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{https://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(xmlData.doc);
            StreamResult result = new StreamResult(out);
            transformer.transform(source, result);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void clearBlankLine(Element element) {
        NodeList childNodes = element.getChildNodes();
        for (int index = 0; index < childNodes.getLength(); index++) {
            Node item = childNodes.item(index);
            if (item.getNodeType() != Node.ELEMENT_NODE && System.lineSeparator()
                    .equals(item.getNodeValue())) {
                element.removeChild(item);
            } else {
                if (item instanceof Element) {
                    clearBlankLine((Element) item);
                }
            }
        }
    }
}
