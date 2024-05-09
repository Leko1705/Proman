package main;

import context.Context;
import mylib.swingx.JComponentRecycler;
import project.Project;
import project.SimpleProject;
import utils.FileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectCell extends JPanel {

    private final Context<?, ?> context;
    private final Path path;
    private final JComponentRecycler recycler;

    public ProjectCell(Context<?, ?> context, Path path, JComponentRecycler recycler){
        this.context = context;
        this.path = path;
        this.recycler = recycler;
        setLayout(new BorderLayout());
        add(new JLabel(FileManager.getManager().getFileName(path, false), JLabel.CENTER), BorderLayout.CENTER);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    new ProjectPopUpMenu().show(e.getComponent(), e.getX(), e.getY());
                }
                else {
                    openProject();
                }
            }
        });
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension dim = super.getMaximumSize();
        dim.height = 50;
        return dim;
    }

    public Path getPath() {
        return path;
    }

    protected void openProject() {
            Project<?> project = new SimpleProject(context);
            context.getApplicationContext().open(project);
            project.onCreate(path.toAbsolutePath());
    }

    private class ProjectPopUpMenu extends JPopupMenu {

        public ProjectPopUpMenu() {
            JMenuItem removeItem = new JMenuItem("remove");
            removeItem.addActionListener(e -> {
                path.toFile().delete();
                recycler.remove(this);
                SwingUtilities.invokeLater(recycler::repaint);
            });
            add(removeItem);
        }
    }

}
