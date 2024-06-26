package diagrams.flow;

import context.Context;
import diagram.Diagram;
import data.Data;

import java.awt.*;

public class FlowchartDiagram extends Diagram<Void> {

    private Component exportableComponent;

    protected FlowchartDiagram(Context<?, ?> context) {
        super(context);
    }

    @Override
    public void onCreate(Data data) {
        setLayout(new BorderLayout());

        DiagramViewPanel viewPanel = new DiagramViewPanel();
        exportableComponent = viewPanel;
        add(viewPanel, BorderLayout.CENTER);
    }

    @Override
    public Component getExportableComponent() {
        return exportableComponent;
    }
}
