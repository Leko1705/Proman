package data;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public interface DataFactory {

    void createFile(Path path,
                    String kind,
                    int version);

    Data load(InputStream in);

    void save(Data data, OutputStream out);

}
