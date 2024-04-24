package export;

import context.Context;

import java.io.File;

public interface Exporter<T> {

    void export(Context<?, ?> context, T t);

}
