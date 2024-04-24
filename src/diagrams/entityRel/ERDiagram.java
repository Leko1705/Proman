package diagrams.entityRel;

import context.Context;
import diagram.Diagram;
import diagram.store.DiagramData;

import java.awt.*;

public class ERDiagram extends Diagram<Void> {

    protected ERDiagram(Context<?, ?> context) {
        super(context);
    }

    @Override
    public void onCreate(DiagramData data) {
        setLayout(new BorderLayout());

        DiagramViewPanel viewPanel = new DiagramViewPanel();
        add(viewPanel, BorderLayout.CENTER);
    }
}
