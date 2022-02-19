package view;

import model.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuickReactionBar extends JPanel {

    private JButton faster = new JButton("Schneller!");
    private JButton why = new JButton("Warum!?");
    private JButton swear = new JButton("Zefix!");

    public QuickReactionBar(GUINew gui) {
        setLayout(new FlowLayout());

        faster.addActionListener(e -> gui.getCClient().playSound(Sound.SCHNELLER));
        why.addActionListener(e -> gui.getCClient().playSound(Sound.WARUM));
        swear.addActionListener(e -> gui.getCClient().playSound(Sound.ZEFIX));

        add(faster);
        add(why);
        add(swear);
    }
}
