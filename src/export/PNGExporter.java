package export;

import context.Context;
import mylib.io.FileWindow2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PNGExporter implements Exporter<Image> {

    @Override
    public void export(Context<?, ?> context, Image image) {
        File outputfile = new File("saved.png");;
        boolean success = new FileWindow2().save(outputfile);
        if (!success) return;
        try {
            BufferedImage bi = toBufferedImage(image);  // retrieve image
            ImageIO.write(bi, "png", outputfile);
        } catch (IOException ignored) {
        }
    }

    private static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}
