package main;

import context.Context;
import mylib.swingx.JComponentRecycler;
import mylib.swingx.JRecycler;
import mylib.swingx.RecyclerView;
import utils.DirectoryChangeAdapter;
import utils.DirectoryWatcher;
import utils.FileManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectSelectionView extends JPanel {

    private final Context<?, ?> context;

    private final JComponentRecycler recycler = new JComponentRecycler();


    public ProjectSelectionView(Context<?, ?> context){
        this.context = context;

        setLayout(new BorderLayout());
        add(recycler, BorderLayout.CENTER);

        FileManager fileManager = FileManager.getManager();
        List<Path> projectPaths = fileManager.getSubDirectories(fileManager.getRoot());
        for (Path prjectPath : projectPaths)
            addProject(prjectPath);

        try {
            new DirectoryWatcher(FileManager.getManager().getRoot(), new DirectoryChangeAdapter() {
                @Override
                public void fileCreated(File file) {
                    addProject(file.toPath());
                }

                @Override
                public void fileDeleted(File file) {
                    Path path = file.toPath();
                    for (Component view : recycler){
                        if (view instanceof ProjectCell cell){
                            if (path.equals(cell.getPath())){
                                recycler.remove(cell);
                                break;
                            }
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addProject(Path projectPath){
        recycler.add(new ProjectCell(context, projectPath, recycler));
    }

}
