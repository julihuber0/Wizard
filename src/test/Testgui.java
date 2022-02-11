package test;

import view.GUINew;

import javax.swing.*;
import java.awt.*;

public class Testgui {


    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    public static void createAndShowGUI() {
        TestFrame gui = new TestFrame();
        gui.setSize(new Dimension(1300, 690));
        gui.setLocationRelativeTo(null);
        gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("./Resources/icon.png");
        gui.setIconImage(icon.getImage());
        gui.setTitle("Wizard");
        gui.setVisible(true);
    }

}
