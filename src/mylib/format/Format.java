package mylib.format;

import java.io.*;
import java.nio.file.Path;

public interface Format {

    void write(OutputStream out, Content content) throws Exception;

    Content read(InputStream in) throws Exception;

    default void write(File file, Content content) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            write(fos, content);
        }
    }

    default void write(Path path, Content content) throws Exception {
        write(path.toFile(), content);
    }

    default Content read(File file) throws Exception {
        return read(new FileInputStream(file));
    }

    default Content read(Path path) throws Exception {
        return read(path.toFile());
    }

}
