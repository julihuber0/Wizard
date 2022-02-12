package test;

import javax.swing.*;
import java.awt.*;

public class Testclass {

    public static void main(String[] args) {
        new Testclass();
    }

    public Testclass() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPanel master = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JPanel left = new JPanel();
                left.setBackground(Color.RED);
                left.add(new JLabel("Lefty"));

                JPanel right = new JPanel();
                right.setBackground(Color.BLUE);
                right.add(new JLabel("Righty"));

                master.add(left);
                master.add(right);

                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(master);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}