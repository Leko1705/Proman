package diagram;

import context.Context;
import data.DataFactory;
import utils.FileManager;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class NewDiagramPanel extends JPanel {

    private final Path path;

    private final FileListener creationListener;

    public NewDiagramPanel(Path path, FileListener creationListener){
        this.path = path;
        this.creationListener = creationListener;
    }

    public void init(Context<?, ?> context){
        setMenuBar(context);
        initGUI(context);
    }

    private void initGUI(Context<?, ?> context){
        DiagramFactory[] factories = Diagrams.getDiagramFactories();
        wrapDiagramFactories(factories);
        JComboBox<DiagramFactory> kindSelector = new JComboBox<>(factories);
        add(kindSelector);

        JTextField nameField = new JTextField("MyDiagram");
        add(nameField);

        JButton createButton = new JButton("create");
        createButton.addActionListener(e -> {
            DiagramFactory factory = (DiagramFactory) kindSelector.getSelectedItem();
            if (factory == null) throw new AssertionError();

            String name = nameField.getText();
            String diagramPath = path + File.separator + name + ".diag";
            if (Files.exists(Path.of(diagramPath))){
                JOptionPane.showMessageDialog(
                        null,
                        "The diagram '" + name + "' already exist in this project",
                        "Diagram already exist",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }


            try {
                Path newPath = Path.of(diagramPath);
                DataFactory format = FileManager.getManager().getDataFactory();
                format.createFile(newPath, factory.getDiagramKey(), 0);
                creationListener.onAction(newPath.toFile());
            }
            catch (Exception ex){
                throw new RuntimeException(ex);
            }

            context.close();
        });
        add(createButton);
    }

    private void wrapDiagramFactories(DiagramFactory[] factories){
        for (int i = 0; i < factories.length; i++){
            factories[i] = new StringableDiagramFactory(factories[i]);
        }
    }

    private void setMenuBar(Context<?, ?> context) {
        JMenuBar menuBar = new JMenuBar();
        JMenuItem backMenu = new JMenuItem("back");
        backMenu.addActionListener(e -> context.close());
        menuBar.add(backMenu);
        ((JFrame)SwingUtilities.getWindowAncestor(this)).setJMenuBar(menuBar);
    }


    private record StringableDiagramFactory(DiagramFactory factory) implements DiagramFactory {

        @Override
        public String getDiagramKey() {
            return factory.getDiagramKey();
        }

        @Override
        public Diagram<?> createEmptyDiagram(Context<?, ?> context, int version) {
            return factory.createEmptyDiagram(context, version);
        }

        @Override
        public String getName() {
            return factory.getName();
        }

        @Override
        public Path getIconPath() {
            return factory.getIconPath();
        }

        @Override
        public String toString() {
            return getName();
        }
    }

}
