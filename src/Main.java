import view.GUINew;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    public static void createAndShowGUI() {
        GUINew gui = new GUINew();
        gui.setSize(new Dimension(1300, 690));
        gui.setMinimumSize(new Dimension(600, 400));
        gui.setLocationRelativeTo(null);
        gui.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ImageIcon icon = new ImageIcon("./Resources/icon.png");
        gui.setIconImage(icon.getImage());
        gui.setTitle("Wizard");
        gui.setVisible(true);
    }
}
