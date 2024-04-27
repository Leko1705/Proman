package diagrams.clazz;

import data.Content;
import data.DataFactory;
import diagram.Diagram;
import data.Data;
import diagrams.clazz.graph.edge.Connection;
import diagrams.clazz.graph.node.Attribute;
import diagrams.clazz.graph.node.ClassNode;
import diagrams.clazz.graph.node.Method;
import diagrams.clazz.graph.node.Parameter;
import graph.Edge;
import graph.GraphPanel;
import graph.Node;
import utils.FileManager;
import utils.ProxyPanel;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class DiagramManager {

    private static final double SPLIT_WEIGHT = 0.75;

    private final Path path;
    private final Diagram<?> diagram;
    private final Data data;
    private final GraphPanel graphPanel;
    private final ProxyPanel proxyPanel;
    private final JSplitPane splitPane;

    private Node<?, ?> focused;

    public DiagramManager(Path path, Diagram<?> diagram, Data data, DiagramViewPanel graphPanel, ProxyPanel proxyPanel, JSplitPane splitPane) {
        this.path = path;
        this.diagram = diagram;
        this.data = data;
        this.graphPanel = graphPanel;
        graphPanel.manager = this;
        this.proxyPanel = proxyPanel;
        this.splitPane = splitPane;
    }

    public Node<?, ?> getFocused(){
        return focused;
    }

    public void setFocused(Node<?, ?> focused) {
        this.focused = focused;
    }

    public void setSideBar(JPanel sideBarPanel){
        if (sideBarPanel instanceof ClassNodePropertyPanel p1){
            if (proxyPanel.get() instanceof  ClassNodePropertyPanel p2){
                if (p1.getClassNode().getID() == p2.getClassNode().getID()){
                    return;
                }
            }
        }
        proxyPanel.set(sideBarPanel);
    }

    public void foldInProperties(){
        splitPane.setEnabled(true);
        splitPane.setResizeWeight(SPLIT_WEIGHT);
        splitPane.setDividerLocation(SPLIT_WEIGHT);
        splitPane.setRightComponent(proxyPanel);
        proxyPanel.setVisible(false);
        splitPane.setEnabled(false);
    }

    public void foldOutProperties(){
        splitPane.setEnabled(true);
        splitPane.setResizeWeight(SPLIT_WEIGHT);
        splitPane.setDividerLocation(SPLIT_WEIGHT);
        splitPane.setRightComponent(proxyPanel);
        proxyPanel.setVisible(true);
        splitPane.setEnabled(false);
    }

    public void writeDataToFile(){
        diagram.updateDiagramFiles(() -> {
            data.getContent().getChildren().clear();

            int classTagID = 0;
            for (Node<?, ?> node : graphPanel.getNodes()) {
                if (node instanceof ClassNode cNode) {
                    Content classContent = data.getContent().getOrCreateChild("class_" + classTagID++);

                    classContent.getOrCreateChild("type").setValue(cNode.getClassType().getTextFormat());
                    classContent.getOrCreateChild("name").setValue(cNode.getClassName());
                    classContent.getOrCreateChild("id").setValue(Long.toString(cNode.getID()));
                    classContent.getOrCreateChild("description").setValue(cNode.getDescription());
                    classContent.getOrCreateChild("pos").getOrCreateChild("x").setValue(Integer.toString(cNode.getX()));
                    classContent.getChild("pos").getOrCreateChild("y").setValue(Integer.toString(cNode.getY()));
                    classContent.getOrCreateChild("desc").setValue(cNode.getDescription());

                    int attributeTagID = 0;
                    for (Attribute attribute : cNode.getAttributes()) {
                        Content attributeContent = classContent.getOrCreateChild("attribute_" + attributeTagID++);

                        attributeContent.getOrCreateChild("type").setValue(attribute.getType());
                        attributeContent.getOrCreateChild("name").setValue(attribute.getName());
                        attributeContent.getOrCreateChild("visibility").setValue(attribute.getVisibility().toString());
                        attributeContent.getOrCreateChild("static").setValue(Boolean.toString(attribute.isStatic()));
                        attributeContent.getOrCreateChild("getter").setValue(Boolean.toString(attribute.hasGetter()));
                        attributeContent.getOrCreateChild("setter").setValue(Boolean.toString(attribute.hasSetter()));
                    }

                    int methodTagID = 0;
                    for (Method method : cNode.getMethods()) {
                        Content methodContent = classContent.getOrCreateChild("method_" + methodTagID++);

                        methodContent.getOrCreateChild("name").setValue(method.getName());
                        methodContent.getOrCreateChild("type").setValue(method.getType());
                        methodContent.getOrCreateChild("visibility").setValue(method.getVisibility().toString());
                        methodContent.getOrCreateChild("static").setValue(Boolean.toString(method.isStatic()));
                        methodContent.getOrCreateChild("abstract").setValue(Boolean.toString(method.isAbstract()));

                        int parameterTagID = 0;
                        for (Parameter parameter : method.getParameters()) {
                            Content parameterContent = methodContent.getOrCreateChild("parameter_" + parameterTagID++);

                            parameterContent.getOrCreateChild("type").setValue(parameter.getType());
                            parameterContent.getOrCreateChild("name").setValue(parameter.getName());
                        }
                    }
                }
            }

            int edgeTagID = 0;
            for (Edge<?> edge : graphPanel.getEdges()) {
                Connection connection = (Connection) edge;

                Content edgeContent = data.getContent().getOrCreateChild("edge_" + edgeTagID++);
                edgeContent.getOrCreateChild("from").setValue(Long.toString(connection.getFromID()));
                edgeContent.getOrCreateChild("to").setValue(Long.toString(connection.getToID()));
                edgeContent.getOrCreateChild("left").setValue(connection.getLeftText());
                edgeContent.getOrCreateChild("right").setValue(connection.getRightText());
                edgeContent.getOrCreateChild("center").setValue(connection.getCenterText());
                edgeContent.getOrCreateChild("style").setValue(Integer.toString(connection.getStyleIndex()));
            }

            try {
                DataFactory factory = FileManager.getManager().getDataFactory();
                factory.save(data, new FileOutputStream(path.toFile()));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }

}
