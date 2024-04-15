package diagram;

import javax.swing.*;

public record DiagramOpenIntent(Diagram<?> diagram, String title, Icon icon) {
}
