package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonBar extends JPanel{

    private JButton join = new JButton("Beitreten");
    private JButton exit = new JButton("Beenden");

    public ButtonBar() {
        setLayout(new FlowLayout());

        join.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Join.");
                join.setVisible(false);
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Frame frame : Frame.getFrames()) {
                    frame.dispose();
                }
            }
        });

        add(join);
        add(exit);
    }
}
