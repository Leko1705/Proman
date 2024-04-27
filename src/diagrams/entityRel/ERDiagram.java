package diagrams.entityRel;

import context.Context;
import diagram.Diagram;
import data.Data;

import java.awt.*;

public class ERDiagram extends Diagram<Void> {

    protected ERDiagram(Context<?, ?> context) {
        super(context);
    }

    @Override
    public void onCreate(Data data) {
        setLayout(new BorderLayout());

        DiagramViewPanel viewPanel = new DiagramViewPanel();
        add(viewPanel, BorderLayout.CENTER);
    }
}
