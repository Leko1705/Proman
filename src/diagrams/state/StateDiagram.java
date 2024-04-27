package diagrams.state;

import context.Context;
import diagram.Diagram;
import data.Data;

import java.awt.*;

public class StateDiagram extends Diagram<Void> {

    private Component exportableComponent;

    protected StateDiagram(Context<?, ?> context) {
        super(context);
    }

    @Override
    public void onCreate(Data data) {
        setLayout(new BorderLayout());

        DiagramViewPanel viewPanel = new DiagramViewPanel();
        exportableComponent = viewPanel;
        add(viewPanel, BorderLayout.CENTER);

        loadData(data, viewPanel);
    }

    private void loadData(Data data, DiagramViewPanel graph) {

    }

    @Override
    public Component getExportableComponent() {
        return exportableComponent;
    }
}
