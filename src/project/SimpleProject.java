package project;

import context.Context;
import diagram.*;
import diagram.store.BackgroundService;
import mylib.swingx.JComponentRecycler;
import utils.DirectoryChangeAdapter;
import utils.DirectoryWatcher;
import utils.FileManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SimpleProject extends Project<DiagramOpenIntent> {

    private DirectoryWatcher diagramWatcher;


    private Path projectPath;
    private final JComponentRecycler diagramRecycler = new JComponentRecycler();
    private final DiagramTabPanel diagramTabbedPane = new DiagramTabPanel();

    public SimpleProject(Context<?, ?> parent) {
        super(parent);
    }

    @Override
    public void onCreate(Path projectPath) {
        this.projectPath = projectPath;
        setUpGUI();
        setMenuBar();
        loadExistentDiagrams(projectPath);
        setUpDiagramWatcher(projectPath);
        BackgroundService.startService();
    }

    @Override
    public void open(DiagramOpenIntent intent) {
        diagramTabbedPane.addTab(intent.title(), intent.icon(), intent.diagram());
    }

    public boolean isOpen(String title){
        return diagramTabbedPane.isOpen(title);
    }

    public Diagram<?> getOpenDiagram(String title){
        return diagramTabbedPane.getOpened(title);
    }

    private void setUpGUI(){
        setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(diagramRecycler);
        splitPane.setRightComponent(diagramTabbedPane);
        final double SPLIT_WEIGHT = 0.1;
        splitPane.setResizeWeight(SPLIT_WEIGHT);
        splitPane.setDividerLocation(SPLIT_WEIGHT);

        add(splitPane, BorderLayout.CENTER);
    }

    private void setMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem closeProjectItem = new JMenuItem("close project");
        closeProjectItem.addActionListener(e -> close());
        fileMenu.add(closeProjectItem);

        JMenuItem newDiagramItem = new JMenuItem("new diagram");
        newDiagramItem.addActionListener(e -> {
            NewDiagramPanel newDiagramPanel = new NewDiagramPanel(projectPath, this::newFileDetected);
            getApplicationContext().open(newDiagramPanel);
            newDiagramPanel.init(this);
        });
        fileMenu.add(newDiagramItem);

        menuBar.add(fileMenu);
        ((JFrame)SwingUtilities.getWindowAncestor(this)).setJMenuBar(menuBar);
    }

    private void loadExistentDiagrams(Path projectPath) {
        FileManager fileManager = FileManager.getManager();
        List<Path> projectPaths = fileManager.getFiles(projectPath);
        for (Path diagramPath : projectPaths) {
            diagramRecycler.add(new DiagramCell(this, diagramPath.toAbsolutePath(), diagramRecycler));
        }
    }

    private void setUpDiagramWatcher(Path projectPath){
        try {
            diagramWatcher = new DirectoryWatcher(projectPath, new DirectoryChangeAdapter() {
                @Override
                public void fileCreated(File file) {
                    newFileDetected(file);
                }

                @Override
                public void fileDeleted(File file) {
                    Path path = file.toPath();
                    if (!".diag".equals(getFileExtension(path))) return;
                    for (Component c : getCopyOfDiagramCells()){
                        DiagramCell cell = (DiagramCell) c;
                        if (cell.path.equals(path)) {
                            diagramRecycler.remove(cell);
                            break;
                        }
                    }
                    repaint();
                }

            }).start();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void newFileDetected(File file){
        Path path = file.toPath();
        String extension = getFileExtension(path);
        if (!"diag".equals(extension)) return;
        diagramRecycler.add(new DiagramCell(SimpleProject.this, path.toAbsolutePath(), diagramRecycler));
        repaint();
    }

    private Collection<Component> getCopyOfDiagramCells(){
        Iterator<Component> source = diagramRecycler.iterator();
        List<Component> target = new ArrayList<>();
        source.forEachRemaining(target::add);
        return target;
    }

    @Override
    public void closeRecentOpened() {
        setMenuBar();
    }

    @Override
    public void onResume() {
        setMenuBar();
    }

    @Override
    public void close() {
        diagramWatcher.cancel(true);
        BackgroundService.cancelService();
        super.close();
    }

    private static String getFileExtension(Path path) {
        if (path == null) {
            return null;
        }
        String pathString = path.toString();
        int lastPos = pathString.lastIndexOf(File.pathSeparator);
        int extensionPos = pathString.lastIndexOf('.');
        int index = lastPos > extensionPos ? -1 : extensionPos;
        if (index == -1) {
            return "";
        } else {
            return pathString.substring(index + 1);
        }
    }
}
