package diagram.store;

import mylib.format.Format;

import java.nio.file.Path;

public interface DataFactory {

    void createFile(Path path,
                    String kind,
                    int version);

    Format getFormat();
}
