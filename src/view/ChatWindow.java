package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChatWindow extends JFrame {

    private ChatView cv;

    public ChatWindow(GUINew gui) {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        cv = new ChatView(gui);
        add(cv);
    }

    public void updateChat(ArrayList<String> chat) {
        cv.updateChat(chat);
    }
}
