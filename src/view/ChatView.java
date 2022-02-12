package view;

import javax.swing.*;

public class ChatView extends JPanel {

    JLabel[] msg = new JLabel[6];

    public ChatView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for(int i = 0; i<6; i++) {
            msg[i] = new JLabel();
            add(msg[i]);
        }
    }
}
