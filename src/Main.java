import ea.Text;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    public static void createAndShowGUI() {
        GUINew gui = new GUINew();
        gui.setMinimumSize(new Dimension(400, 300));
        gui.setLocationRelativeTo(null);
        gui.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        gui.setVisible(true);
    }
}
