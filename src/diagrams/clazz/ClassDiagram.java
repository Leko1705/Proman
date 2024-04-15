package diagrams.clazz;

import context.Context;
import diagram.Diagram;
import diagram.store.DiagramData;
import utils.ProxyPanel;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;

public class ClassDiagram extends Diagram<Void> {

    protected ClassDiagram(Context<?, ?> context) {
        super(context);
    }

    @Override
    public void onCreate(DiagramData data) {
        setLayout(new BorderLayout());

        DiagramViewPanel graphPanel = new DiagramViewPanel();
        ProxyPanel proxyPanel = new ProxyPanel(this);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(graphPanel);
        splitPane.setRightComponent(proxyPanel);

        add(splitPane, BorderLayout.CENTER);

        DiagramManager manager =
                new DiagramManager(getPath(), this, data, graphPanel, proxyPanel, splitPane);
        manager.foldInProperties();
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
