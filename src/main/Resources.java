package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public final class Resources {

    private static Image applicationIcon = null;

    public static Image getApplicationIcon(){
        if (applicationIcon != null) return applicationIcon;
        try {
            return (applicationIcon = ImageIO.read(new File("images/application_icon.png")));
        }catch (Exception e){
            return null;
        }
    }

}
