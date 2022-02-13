import view.GUINew;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ServerMain {

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    public static void createAndShowGUI() {
        ServerFrame gui = new ServerFrame();
        gui.setSize(new Dimension(500, 200));
        gui.setMinimumSize(new Dimension(500, 200));
        gui.setLocationRelativeTo(null);
        gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("./Resources/icon.png");
        gui.setIconImage(icon.getImage());
        gui.setTitle("Wizard-Server");
        gui.setVisible(true);
    }
}
