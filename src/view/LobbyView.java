package view;

import model.Player;

import javax.swing.*;
import java.util.ArrayList;

public class LobbyView extends JPanel {

    ArrayList<JLabel> names = new ArrayList<>();

    public LobbyView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for(int i = 0; i<6; i++) {
            JLabel name = new JLabel();
            names.add(name);
            add(name);
        }
    }

    public void addPlayerName(Player p) {
        for(int i = 0; i<6; i++) {
            if(names.get(i).getText().equals("")) {
                names.get(i).setText(p.getName());
                break;
            }
        }
    }
}
