package diagrams.state.graph.node;

import diagrams.utils.RoundRectTextNode;

import java.awt.*;

public class SimpleState extends RoundRectTextNode {

    public SimpleState() {
        super(50, 50);
        setSize(150, 100);
        setFillColor(new Color(255, 251, 240));
        setOutline(Color.BLACK);
    }

}
