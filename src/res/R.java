package res;

import javax.swing.*;
import java.util.Objects;

public final class R {

    public static Icon loadImage(String file){
        return new ImageIcon(Objects.requireNonNull(R.class.getResource(file)));
    }

}
