package graph;

import java.awt.*;

public interface EdgeModelPainter<M extends EdgeModel> {

    void paintModel(Graphics g, M model);

}
