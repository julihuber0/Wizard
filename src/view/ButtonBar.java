package view;

import model.CClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonBar extends JPanel{

    private JButton join = new JButton("Beitreten");
    private JButton exit = new JButton("Beenden");

    private GUINew mainGUI;

    public ButtonBar(GUINew parent) {
        mainGUI = parent;
        setLayout(new FlowLayout());

        join.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ipAddress = Utility.askInput("Server-IP-Adresse");
                mainGUI.setCClient(ipAddress);
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
