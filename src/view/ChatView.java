package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChatView extends JPanel {

    JLabel[] msg = new JLabel[10];
    ChatSender sender;
    QuickReactionBar reactionBar;

    public ChatView(GUINew gui) {
        setLayout(new VerticalFlowLayout(FlowLayout.LEFT, FlowLayout.LEFT));
        for(int i = 0; i<10; i++) {
            msg[i] = new JLabel(" ");
            msg[i].setFont(msg[i].getFont().deriveFont(Font.PLAIN));
            add(msg[i]);
        }
        sender = new ChatSender(gui);
        reactionBar = new QuickReactionBar(gui);
        add(sender);
        add(reactionBar);
    }

    public void updateChat(ArrayList<String> chat) {
        for(int i = 9; i>=10-chat.size(); i--) {
            msg[i].setText(chat.get(i-10+chat.size()));
        }
    }
}
