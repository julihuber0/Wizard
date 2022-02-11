package test;

import javax.swing.*;
import java.awt.*;

public class TestPanel extends JPanel {

    ImageIcon ic = new ImageIcon("./Resources/avatar.png");

    public TestPanel() {
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLUE);
        Frame f = Frame.getFrames()[0];
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(ic.getImage(), 0, 0, f.getWidth()/5, f.getWidth()/5, null);
    }
}
