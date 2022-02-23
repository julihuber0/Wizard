import view.GUINew;
import view.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Main {
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    public static void createAndShowGUI() {
        GUINew gui = new GUINew();
        gui.setSize(new Dimension(1300, 750));
        gui.setMinimumSize(new Dimension(1300, 750));
        gui.setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon("./Resources/icon.png");
        gui.setIconImage(icon.getImage());
        gui.setTitle("Wizard");
        gui.setVisible(true);

        gui.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                int result = Utility.showConfirmDialog("Beenden", "Wirklich beenden?");
                if(result == JOptionPane.YES_OPTION) {
                    gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    gui.shutdown();
                } else if (result == JOptionPane.NO_OPTION) {
                    gui.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }
}
