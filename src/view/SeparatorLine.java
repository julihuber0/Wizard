package view;

import javax.swing.*;
import java.awt.*;

public class SeparatorLine extends JPanel {

    public SeparatorLine() {}

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.drawLine(0,0, 500, 0);
    }
}
