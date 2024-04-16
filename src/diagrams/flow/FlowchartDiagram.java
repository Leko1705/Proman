package diagrams.flow;

import context.Context;
import diagram.Diagram;
import diagram.store.DiagramData;

import java.awt.*;

public class FlowchartDiagram extends Diagram<Void> {

    protected FlowchartDiagram(Context<?, ?> context) {
        super(context);
    }

    @Override
    public void onCreate(DiagramData data) {
        setLayout(new BorderLayout());

        DiagramViewPanel viewPanel = new DiagramViewPanel();
        add(viewPanel, BorderLayout.CENTER);
    }

}
