import javax.swing.*;
import java.awt.*;

public class Utility {

    public static ImageIcon resizeIcon(ImageIcon icon, int newWidth, int newHeight) {
        Image img = icon.getImage();
        Image resized = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }
}
