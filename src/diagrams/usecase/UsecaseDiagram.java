package diagrams.usecase;

import context.Context;
import data.Data;
import diagram.Diagram;

import java.awt.*;

public class UsecaseDiagram extends Diagram<Void> {

    private Component exportableComponent;

    protected UsecaseDiagram(Context<?, ?> context) {
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
