package diagram.store;

import mylib.format.Format;
import mylib.format.XMLFormat;
import mylib.io.BuffedFile;

import java.nio.file.Path;

public class XMLDataFactory implements DataFactory {

    private static final Format format = new XMLFormat();

    @Override
    public void createFile(Path path, String kind, int version) {

        try(BuffedFile file = new BuffedFile(path.toAbsolutePath().toString())) {
            file.writeln("""
                    <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                    """);
            file.writeln("<diagram>");
            file.writeln("\t<version>" + version + "</version>");
            file.writeln("\t<kind>" + kind + "</kind>\n");
            file.writeln("\t<content>\n\t</content>");
            file.write("</diagram>");

        }
    }

    @Override
    public Format getFormat() {
        return format;
    }

}
