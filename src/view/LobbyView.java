package view;

import model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LobbyView extends JPanel {

    ArrayList<JLabel> names = new ArrayList<>();

    public LobbyView() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for(int i = 0; i<6; i++) {
            JLabel name = new JLabel();
            name.setOpaque(false);
            name.setFont(new Font("Candara", Font.PLAIN, 35));
            name.setForeground(Color.WHITE);
            names.add(name);
            add(name);
        }
    }

    public void addPlayerName(Player p, int index) {
        names.get(index).setText("<html><strong><i>Spieler "+(index+1)+": </i></strong>"+p.getName());
    }
}
