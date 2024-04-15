package diagrams.clazz;

import diagram.Diagram;
import diagram.store.DiagramData;
import diagrams.clazz.graph.node.Attribute;
import diagrams.clazz.graph.node.ClassNode;
import diagrams.clazz.graph.node.Method;
import diagrams.clazz.graph.node.Parameter;
import graph.GraphPanel;
import graph.Node;
import mylib.format.Content;
import mylib.format.Format;
import utils.FileManager;
import utils.ProxyPanel;

import javax.swing.*;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class DiagramManager {

    private static final double SPLIT_WEIGHT = 0.75;

    private final Path path;
    private final Diagram<?> diagram;
    private final DiagramData data;
    private final GraphPanel graphPanel;
    private final ProxyPanel proxyPanel;
    private final JSplitPane splitPane;

    private Node<?, ?> focused;

    public DiagramManager(Path path, Diagram<?> diagram, DiagramData data, DiagramViewPanel graphPanel, ProxyPanel proxyPanel, JSplitPane splitPane) {
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
            FileManager fileManager = FileManager.getManager();
            Format format = fileManager.getDataFactory().getFormat();

            data.clear("diagram", "content");
            
            int n = 0;
            for (Node<?, ?> node : graphPanel.getNodes()) {
                if (node instanceof ClassNode cNode) {
                    String classTag = "class_" + n++;
                    data.set(cNode.getID(), "diagram", "content", classTag, "id");
                    data.set(cNode.getClassType().getTextFormat(), "diagram", "content", classTag, "type");
                    data.set(cNode.getClassName(), "diagram", "content", classTag, "name");

                    int i = 0;
                    for (Attribute attribute : cNode.getAttributes()) {
                        String attributeTag = "attribute_" + i++;
                        data.set(attribute.getType(), "diagram", "content", classTag, attributeTag, "type");
                        data.set(attribute.getName(), "diagram", "content", classTag, attributeTag, "name");
                        data.set(attribute.getVisibility().getTextFormat(), "diagram", "content", classTag, attributeTag, "visibility");
                        data.set(attribute.hasGetter(), "diagram", "content", classTag, attributeTag, "getter");
                        data.set(attribute.hasSetter(), "diagram", "content", classTag, attributeTag, "setter");
                        data.set(attribute.isStatic(), "diagram", "content", classTag, attributeTag, "static");
                    }

                    i = 0;
                    for (Method method : cNode.getMethods()) {
                        String methodTag = "method_" + i++;

                        data.set(method.getType(), "diagram", "content", classTag, methodTag, "type");
                        data.set(method.getName(), "diagram", "content", classTag, methodTag, "name");
                        data.set(method.getVisibility().toString(), "diagram", "content", classTag, methodTag, "visibility");
                        data.set(method.isStatic(), "diagram", "content", classTag, methodTag, "static");
                        data.set(method.isAbstract(), "diagram", "content", classTag, methodTag, "abstract");

                        int j = 0;
                        for (Parameter parameter : method.getParameters()) {
                            String parameterTag = "parameter_" + j++;
                            data.set(parameter.getType(), "diagram", "content", classTag, methodTag, parameterTag, "type");
                            data.set(parameter.getName(), "diagram", "content", classTag, methodTag, parameterTag, "name");
                        }
                    }

                }
            }

            // System.out.println(data);

        });
    }

}
