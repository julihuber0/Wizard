package view;

import javax.swing.*;
import java.awt.*;

public class Utility {

    public static ImageIcon resizeIcon(ImageIcon icon, int newWidth, int newHeight) {
        Image img = icon.getImage();
        Image resized = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }

    public static String askInput(String msg) {
        String input = JOptionPane.showInputDialog(msg);
        if(input == null || input.equals("")){
            return askInput(msg);
        }
        return input;
    }
}
