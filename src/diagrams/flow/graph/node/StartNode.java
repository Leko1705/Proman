package diagrams.flow.graph.node;

import diagrams.utils.RoundRectTextNode;

import java.awt.*;

public class StartNode extends RoundRectTextNode {

    public StartNode() {
        super(50, 50);
        setSize(150, 50);
        setOutline(Color.BLACK);
        setFillColor(new Color(246, 209, 255));
    }

}
