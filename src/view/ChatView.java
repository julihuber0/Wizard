package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChatView extends JPanel {

    JLabel[] msg = new JLabel[10];
    ChatSender sender;

    public ChatView(GUINew gui) {
        setLayout(new BoxLayoutLeft(this, BoxLayout.Y_AXIS));
        setAlignmentY(0.0f);
        for(int i = 0; i<10; i++) {
            msg[i] = new JLabel(" ");
            msg[i].setFont(msg[i].getFont().deriveFont(Font.PLAIN));
            add(msg[i]);
        }
        sender = new ChatSender(gui);
        add(sender);
    }

    public void updateChat(ArrayList<String> chat) {
        for(int i = 9; i>=10-chat.size(); i--) {
            msg[i].setText(chat.get(i-10+chat.size()));
        }
    }
}
