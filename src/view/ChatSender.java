package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatSender extends JPanel {

    private JButton send = new JButton("Senden");
    private JTextField msg = new JTextField();
    private boolean textFocused = false;

    public ChatSender(GUINew gui) {
        setLayout(new FlowLayout());
        msg.setPreferredSize(new Dimension(250, 25));
        send.addActionListener(e -> {
            if(!msg.getText().isEmpty()) {
                gui.getCClient().sendChatmessage(msg.getText());
                msg.setText("");
            }
        });

        msg.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(textFocused && e.getKeyCode() == 10) {
                    if(!msg.getText().isEmpty()) {
                        gui.getCClient().sendChatmessage(msg.getText());
                        msg.setText("");
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        msg.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textFocused = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                textFocused = false;
            }
        });

        add(msg);
        add(send);
    }
}
