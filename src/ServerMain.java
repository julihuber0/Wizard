import view.GUINew;

import javax.swing.*;
import java.awt.*;

public class ServerMain {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    public static void createAndShowGUI() {
        ServerFrame gui = new ServerFrame();
        gui.setSize(new Dimension(500, 200));
        gui.setMinimumSize(new Dimension(500, 200));
        gui.setLocationRelativeTo(null);
        gui.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ImageIcon icon = new ImageIcon("./Resources/icon.png");
        gui.setIconImage(icon.getImage());
        gui.setTitle("Wizard-Server");
        gui.setVisible(true);
    }
}
