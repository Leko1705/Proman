package diagram;

import context.BaseContext;
import context.Context;
import diagram.store.BackgroundService;
import diagram.store.DiagramData;

import javax.swing.*;
import java.nio.file.Path;
import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class Diagram<V> extends BaseContext<DiagramData, V> {

    private Path path;

    protected Diagram(Context<?, ?> context) {
        super(context);
    }

    public abstract void onCreate(DiagramData data);

    protected void setPath(Path path){
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public void closeRecentOpened() {
        throw new UnsupportedOperationException("closeRecentOpened");
    }

    public void installPopUpMenu(JPopupMenu menu){ }


    public void updateDiagramFiles(Runnable task){
        BackgroundService.performTask(task);
    }

}
