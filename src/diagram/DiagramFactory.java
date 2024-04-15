package diagram;

import context.Context;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public interface DiagramFactory {

    String getDiagramKey();

    Diagram<?> createEmptyDiagram(Context<?, ?> context, int version);

    String getName();

    Path getIconPath();

    default Icon getIcon(){
        Path iconPath = getIconPath();
        if (iconPath == null) return null;
        ImageIcon imageIcon = new ImageIcon(iconPath.toAbsolutePath().toString());
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        return imageIcon;
    }

}
