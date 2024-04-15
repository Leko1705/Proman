package diagrams.state;

import context.Context;
import diagram.Diagram;
import diagram.store.DiagramData;

import java.awt.*;

public class StateDiagram extends Diagram<Void> {

    protected StateDiagram(Context<?, ?> context) {
        super(context);
    }

    @Override
    public void onCreate(DiagramData data) {
        setLayout(new BorderLayout());

        DiagramViewPanel viewPanel = new DiagramViewPanel();
        add(viewPanel, BorderLayout.CENTER);
    }

}
