package mylib.format;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class XMLFormat implements Format {

    @Override
    public void write(OutputStream out, Content content) throws Exception {
        if (!(content instanceof XMLContent c)) return;

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        //transformer.setOutputProperty("{https://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(c.document());
        StreamResult result = new StreamResult(out);
        transformer.transform(source, result);
    }

    @Override
    public Content read(InputStream in) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return new XMLContent(documentBuilder.parse(in));
    }

}
