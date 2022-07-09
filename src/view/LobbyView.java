package view;

import model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LobbyView extends JPanel {

    ArrayList<JLabel> names = new ArrayList<>();

    JLabel blank = new JLabel(" ");

    public LobbyView() {
        setOpaque(false);
        blank.setFont(new Font("Candara", Font.PLAIN, 100));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(blank);
        for(int i = 0; i<6; i++) {
            JLabel name = new JLabel();
            name.setOpaque(false);
            name.setFont(new Font("Candara", Font.PLAIN, 35));
            name.setForeground(Color.BLACK);
            names.add(name);
            add(name);
        }
    }

    public void addPlayerName(Player p, int index) {
        names.get(index).setText("<html><strong><i>Spieler "+(index+1)+": </i></strong>"+p.getName());
    }
}
