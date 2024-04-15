package image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DefaultImageProvider implements ImageProvider {

    @Override
    public Image provide(Component c) {
        BufferedImage bImg =
                new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D cg = bImg.createGraphics();
        c.paintAll(cg);
        return bImg;
    }

}
