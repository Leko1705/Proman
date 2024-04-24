package diagrams.entityRel.graph.node;

import diagrams.utils.RoundRectTextNode;

import java.awt.*;

public class EntityNode extends RoundRectTextNode {

    public EntityNode() {
        super(0, 0);
        setSize(150, 80);
        setOutline(Color.BLACK);
        setFillColor(new Color(209, 225, 255));
    }
}
