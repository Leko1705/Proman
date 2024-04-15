package graph;

import java.awt.event.MouseEvent;

public interface EdgeClickListener {

    void edgeClicked(Edge<?> edge, MouseEvent e);
}
