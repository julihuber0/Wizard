package view;

import javax.swing.*;
import java.util.ArrayList;

public class ChatWindow extends JFrame {

    private ArrayList<String> chat;

    public ChatWindow(ArrayList<String> chat) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.chat = chat;
    }
}
