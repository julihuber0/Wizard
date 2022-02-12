package view;

import javax.swing.*;
import java.util.ArrayList;

public class ChatView extends JPanel {

    JLabel[] msg = new JLabel[6];
    ChatSender sender;

    public ChatView(GUINew gui) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for(int i = 0; i<6; i++) {
            msg[i] = new JLabel();
            add(msg[i]);
        }
        sender = new ChatSender(gui);
        add(sender);
    }

    public void updateChat(ArrayList<String> chat) {
        for(int i = 0; i<6; i++) {
            msg[i].setText("");
        }
        for(int i = 5; i>=6-chat.size(); i--) {
            msg[i].setText(chat.get(i-6+chat.size()));
        }
    }
}
