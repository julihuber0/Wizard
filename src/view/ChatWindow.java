package view;

import javax.swing.*;
import java.util.ArrayList;

public class ChatWindow extends JFrame {

    private ArrayList<String> chat;
    private ChatView cw;

    public ChatWindow(GUINew gui) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        cw = new ChatView(gui);
    }

    public void updateChat(ArrayList<String> chat) {
        cw.updateChat(chat);
    }
}
