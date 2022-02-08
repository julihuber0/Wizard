package view;

import model.Player;

import javax.swing.*;
import java.util.ArrayList;

public class LobbyView extends JPanel {

    ArrayList<JLabel> names = new ArrayList<>();

    public LobbyView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void addPlayerName(Player p) {
        JLabel name = new JLabel(p.getName());
        names.add(name);
        add(name);
    }
}
