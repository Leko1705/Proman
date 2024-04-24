package export;

import context.Context;
import mylib.io.FileWindow2;

import java.nio.file.Path;

public class DiagExporter implements Exporter<Path> {

    @Override
    public void export(Context<?, ?> context, Path path) {
        new FileWindow2().save(path.toFile());
    }

}
