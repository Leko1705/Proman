package diagrams.flow.graph.node;

import diagrams.utils.RoundRectTextNode;

import java.awt.*;

public class ProcessNode extends RoundRectTextNode {

    public ProcessNode() {
        super(0, 0);
        setSize(150, 100);
        setOutline(Color.BLACK);
        setFillColor(new Color(209, 225, 255));
    }

}
