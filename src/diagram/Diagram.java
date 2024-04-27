package diagram;

import context.BaseContext;
import context.Context;
import utils.BackgroundService;
import data.Data;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public abstract class Diagram<V> extends BaseContext<Data, V> {

    private Path path;

    protected Diagram(Context<?, ?> context) {
        super(context);
    }

    public abstract void onCreate(Data data);

    public Component getExportableComponent(){
        return this;
    }

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
