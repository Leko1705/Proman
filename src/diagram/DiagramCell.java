package diagram;

import context.Context;
import export.DiagExporter;
import export.Exporter;
import export.PNGExporter;
import image.DefaultImageProvider;
import image.ImageProvider;
import mylib.format.Content;
import diagram.store.DiagramData;
import mylib.format.Format;
import mylib.swingx.JComponentRecycler;
import project.SimpleProject;
import utils.FileManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.nio.file.Path;

public class DiagramCell extends JPanel {

    private final Context<?, ?> context;
    private final JComponentRecycler diagramRecycler;

    public Path path;

    private DiagramData data;

    private final JLabel label;

    public DiagramCell(Context<?, ?> context, Path path, JComponentRecycler diagramRecycler) {
        this.context = context;
        this.diagramRecycler = diagramRecycler;
        this.path = path;

        FileManager fileManager = FileManager.getManager();
        String fileName = fileManager.getFileName(path, false);

        setLayout(new FlowLayout());
        setBorder(new EmptyBorder(0, 40, 0, 0));
        add(new JLabel(getDiagramFactory().getIcon(), JLabel.LEFT));
        add(label = new JLabel(fileName, JLabel.CENTER));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)){
                    new DiagramCellPopUpMenu().show(e.getComponent(), e.getX(), e.getY());
                }
                else {
                    openDiagram();
                }
            }
        });

        setMaximumSize(new Dimension(getMaximumSize().width, 40));
    }

    private DiagramData getDiagramData() {
        if (data != null) return data;

        FileManager manager = FileManager.getManager();
        Format format = manager.getDataFactory().getFormat();

        try {
            Content content = format.read(new FileInputStream(path.toFile()));
            data = new DiagramData(content);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    private DiagramFactory getDiagramFactory(){
        DiagramData diagramData = getDiagramData();
        return Diagrams.getDiagramFactory(diagramData.getKind());
    }

    private void openDiagram(){
        DiagramFactory factory = getDiagramFactory();
        DiagramData diagramData = getDiagramData();
        Diagram<?> diagram = factory.createEmptyDiagram(context, diagramData.getVersion());
        diagram.setPath(path);
        diagram.onCreate(diagramData);
        openDiagram(diagram, factory);
    }


    private class DiagramCellPopUpMenu extends JPopupMenu {

        public DiagramCellPopUpMenu(){
            JMenuItem removeItem = new JMenuItem("remove");
            removeItem.addActionListener(e -> {
                path.toFile().delete();
                diagramRecycler.remove(DiagramCell.this);
                diagramRecycler.repaint();
            });
            add(removeItem);

            JMenuItem renameItem = new JMenuItem("rename");
            renameItem.addActionListener(e -> {
                // TODO rename file
            });
            add(renameItem);


            DiagramFactory factory = getDiagramFactory();
            DiagramData diagramData = getDiagramData();
            Diagram<?> diagram = factory.createEmptyDiagram(context, diagramData.getVersion());

            JMenu exportMenu = getExportMenu(diagram, factory, diagramData);
            add(exportMenu);

            diagram.installPopUpMenu(this);
        }

        private JMenu getExportMenu(Diagram<?> diagram, DiagramFactory factory, DiagramData diagramData) {
            JMenu exportMenu = new JMenu("export");

            JMenuItem asPng = new JMenuItem("as png");
            asPng.addActionListener(e -> SwingUtilities.invokeLater(() -> {
                Diagram<?> exportableDiagram = getOpened(diagram, factory, diagramData);
                SwingUtilities.invokeLater(()
                        -> exportImage(exportableDiagram.getExportableComponent(), new PNGExporter()));
            }));
            exportMenu.add(asPng);

            JMenuItem asDiag = new JMenuItem("as diag");
            asDiag.addActionListener(e -> {
                Exporter<Path> diagExporter = new DiagExporter();
                diagExporter.export(context, path);
            });
            exportMenu.add(asDiag);
            return exportMenu;
        }

    }

    private Diagram<?> getOpened(Diagram<?> diagram, DiagramFactory factory, DiagramData data){
        Diagram<?> mutableDiagram;
        SimpleProject project = (SimpleProject) context;
        String name = FileManager.getManager().getFileName(path, false);
        if (project.isOpen(name))
            mutableDiagram = project.getOpenDiagram(name);
        else {
            mutableDiagram = diagram;
            diagram.setPath(path);
            diagram.onCreate(data);
            openDiagram(diagram, factory);
        }

        return mutableDiagram;
    }

    private void exportImage(Component component, Exporter<Image> exporter){
        ImageProvider imageProvider = new DefaultImageProvider();
        Image image = imageProvider.provide(component);

        exporter.export(context, image);
    }

    private void openDiagram(Diagram<?> diagram, DiagramFactory factory){
        ((SimpleProject)context).open(
                new DiagramOpenIntent(diagram, FileManager.getManager().getFileName(path, false), factory.getIcon())
        );
    }

}


