package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatSender extends JPanel {

    private JButton send = new JButton("Senden");
    private JTextField msg = new JTextField();

    public ChatSender(GUINew gui) {
        setLayout(new FlowLayout());
        Frame f = Frame.getFrames()[1];
        msg.setPreferredSize(new Dimension(f.getWidth()-200, 40));
        send.addActionListener(e -> {
            if(!msg.getText().isEmpty()) {
                gui.getCClient().sendChatmessage(msg.getText());
            }
        });
        add(msg);
        add(send);
    }
}
