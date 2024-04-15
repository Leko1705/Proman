package project;

import context.BaseContext;
import context.Context;

import java.nio.file.Path;

public abstract class Project<V> extends BaseContext<Path, V> {

    protected Project(Context<?, ?> parent) {
        super(parent);
    }

    @Override
    public abstract void onCreate(Path projectPath);


    @Override
    public abstract void closeRecentOpened();

}
