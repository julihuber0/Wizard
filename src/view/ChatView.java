package view;

import javax.swing.*;
import java.util.ArrayList;

public class ChatView extends JPanel {

    JLabel[] msg = new JLabel[6];

    public ChatView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for(int i = 0; i<6; i++) {
            msg[i] = new JLabel();
            add(msg[i]);
        }
    }

    public void updateChat(ArrayList<String> chat) {
        for(int i = 5; i>=6-chat.size(); i--) {
            msg[i].setText(chat.get(6-i));
        }
    }
}
