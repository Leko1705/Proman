package diagrams.clazz;

import context.Context;
import data.Content;
import diagram.Diagram;
import data.Data;
import diagrams.clazz.graph.edge.Connection;
import diagrams.clazz.graph.node.*;
import diagrams.utils.TextLabelNode;
import utils.ProxyPanel;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;

public class ClassDiagram extends Diagram<Void> {

    private Component exportableComponent;

    protected ClassDiagram(Context<?, ?> context) {
        super(context);
    }

    @Override
    public void onCreate(Data data) {
        setLayout(new BorderLayout());

        DiagramViewPanel graphPanel = new DiagramViewPanel();
        exportableComponent = graphPanel;
        ProxyPanel proxyPanel = new ProxyPanel(this);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(graphPanel);
        splitPane.setRightComponent(proxyPanel);

        add(splitPane, BorderLayout.CENTER);

        DiagramManager manager =
                new DiagramManager(getPath(), this, data, graphPanel, proxyPanel, splitPane);
        manager.foldInProperties();

        loadData(data, graphPanel);
    }

    @Override
    public Component getExportableComponent() {
        return exportableComponent;
    }

    private void loadData(Data data, DiagramViewPanel graph){
        Content dataContent = data.getContent().getOrCreateChild("content");

        for (Content child : dataContent.getChildren()) {
            String tag = child.getTag();

            if (tag != null && tag.startsWith("class")){

                String classType = child.getChild("type").getValue();
                String className = child.getChild("name").getValue();
                long id = Long.parseLong(child.getChild("id").getValue());
                int x = Integer.parseInt(child.getChild("pos").getChild("x").getValue());
                int y = Integer.parseInt(child.getChild("pos").getChild("y").getValue());
                String desc = child.getChild("desc").getValue();

                ClassNode classNode = graph.createClassNode(ClassType.fromTextFormat(classType), className);
                classNode.setLocation(x, y);
                classNode.setID(id);
                classNode.setDescription(desc);

                for (Content classElement : child.getChildren()){
                    tag = classElement.getTag();

                    if (tag.startsWith("attribute")){
                        String attributeType = classElement.getChild("type").getValue();
                        String attributeName = classElement.getChild("name").getValue();
                        Visibility visibility = Visibility.fromTextFormat(classElement.getChild("visibility").getValue());
                        boolean isStatic = Boolean.parseBoolean(classElement.getChild("static").getValue());
                        boolean hasGetter = Boolean.parseBoolean(classElement.getChild("getter").getValue());
                        boolean hasSetter = Boolean.parseBoolean(classElement.getChild("setter").getValue());

                        Attribute attribute = new Attribute(visibility, attributeType, attributeName, isStatic);
                        attribute.setGetter(hasGetter);
                        attribute.setSetter(hasSetter);

                        classNode.getAttributes().add(attribute);
                    }

                    if (tag.startsWith("method")){
                        String name = classElement.getChild("name").getValue();
                        String type = classElement.getChild("type").getValue();
                        Visibility visibility = Visibility.fromTextFormat(classElement.getChild("visibility").getValue());
                        boolean isStatic = Boolean.parseBoolean(classElement.getChild("static").getValue());
                        boolean isAbstract = Boolean.parseBoolean(classElement.getChild("abstract").getValue());

                        Method method = new Method(visibility, name, type, isStatic, isAbstract);

                        for (Content methodElement : classElement.getChildren()){
                            tag = methodElement.getTag();

                            if (tag.startsWith("parameter")){
                                name = methodElement.getChild("name").getValue();
                                type = methodElement.getChild("type").getValue();

                                method.getParameters().add(new Parameter(type, name));
                            }
                        }

                        classNode.getMethods().add(method);
                    }
                }

                graph.addNode(classNode, false);
            }
            else if (tag != null && tag.startsWith("text")){
                String content = child.getChild("content").getValue();
                int x = Integer.parseInt(child.getChild("pos").getChild("x").getValue());
                int y = Integer.parseInt(child.getChild("pos").getChild("y").getValue());
                TextLabelNode labelNode = new TextLabelNode(graph, content);
                labelNode.setLocation(x, y);
                graph.addNode(labelNode, false);
            }
        }

        for (Content child : dataContent.getChildren()) {
            String tag = child.getTag();

            if (tag != null && tag.startsWith("edge")){
                long from = Long.parseLong(child.getChild("from").getValue());
                long to = Long.parseLong(child.getChild("to").getValue());
                String left = child.getChild("left").getValue();
                String right = child.getChild("right").getValue();
                String center = child.getChild("center").getValue();
                int style = Integer.parseInt(child.getChild("style").getValue());

                Connection connection = new Connection(graph.getNode(from), graph.getNode(to), EdgePropertyPanel.styles.get(style));
                connection.setStyleIndex(style);
                connection.setLeftText(left);
                connection.setRightText(right);
                connection.setCenterText(center);

                graph.addEdge(connection);
            }

        }

    }


    @Override
    public void installPopUpMenu(JPopupMenu menu) {
        menu.addSeparator();
        JMenuItem open = new JMenuItem("help");
        open.addActionListener(e -> {
            JScrollPane scrollPane = new JScrollPane(new HelpViewPanel());
            scrollPane.setPreferredSize( new Dimension( 500, 500 ) );
            JOptionPane.showMessageDialog(null, scrollPane, "",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        menu.add(open);
    }

    private static class HelpViewPanel extends JPanel {

        private static final String TEXT = """
                <html>
                    <h1>How to use the<br>UML-Class Diagram Editor</h1>
                    <p>
                        The UML-Class Diagram Editor allows you to easily create <br>
                        and model UML-Class diagrams. <br><br>
                        <b>UML-Class</b> Diagrams are Diagrams representing
                        Class structures <br> with their relationships and hierarchies. <br><br>
                        See also: <a href="https://en.wikipedia.org/wiki/Class_diagram">class diagrams</a>"
                        <br><br>
                        In general a class diagram consist of multiple <b>elements</b>.<br>
                        There are two types of elements:
                    </p>
                    <h2>Class nodes</h2>
                    <p>
                        Class nodes represent a class, containing multiple Attributes <br>
                        and methods, which can be added and removed (see below).<br>
                        Nodes can be connected via Connections. (see below)
                    </p>
                    <br>
                    <h2>Connections</h2>
                    <p>
                        A Connection connects two nodes. Often nodes represent a <br>
                        specific relation between them.
                    </p>
                    <br><hr>
                    <h2>Adding elements</h2>
                    <p>
                        In order to add elements to the diagram simply <br>
                         <b>right click</b> on the diagram where you want the element to appear. <br>
                        Then select <b>add</b> and then the element you want to add. <br>
                    </p>
                    <br>
                    <h2>Removing elements</h2>
                    <p>
                        To remove an element <b>right click</b> on the <br>
                        element and select <b>remove</b>.
                    </p>
                    <br>
                    <h2>editing elements</h2>
                    <p>
                        In order to edit elements simply <b>click them</b>.<br>
                        If clicked a sidebar on the right side will open up, <br>
                        which allows you to modify the selected element. <br>
                        The elements view will adapt automatically to the change.
                    </p>
                    <h2>connecting Class nodes</h2>
                    <p>
                        To create a relationship between two clas Nodes simply <br>
                        <b>right click</b> the element you want to connect <br>
                        and select <b>connect</b>.<br>
                        Then click on the second element for this connection.<br>
                        In order to cancel the connection process simply click <br>
                        on some free space.
                    </p>
                </html>
                """;


        public HelpViewPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JEditorPane ep = new JEditorPane();
            ep.setContentType("text/html");
            ep.setText(TEXT);
            ep.setEditable(false);
            ep.addHyperlinkListener(e -> {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(e.getURL().toURI());
                    } catch (Exception ignored) {
                        Runtime runtime = Runtime.getRuntime();
                        try {
                            runtime.exec(new String[]{"xdg-open", e.getURL().toString()});
                        } catch (Exception ignored2) {
                        }
                    }
                }
            });

            add(ep);
        }
    }
}
