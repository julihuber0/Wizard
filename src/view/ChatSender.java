package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatSender extends JPanel {

    private JButton send = new JButton("Senden");
    private JTextField msg = new JTextField();
    private GUINew gui;

    public ChatSender(GUINew gui) {
        this.gui = gui;
        setLayout(new FlowLayout());
        send.addActionListener(e -> {
            if(!msg.getText().isEmpty()) {
                gui.getCClient().sendChatmessage(msg.getText());
            }
        });
    }
}
