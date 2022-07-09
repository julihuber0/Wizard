package view;

import model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LobbyView extends JPanel {

    ArrayList<JLabel> names = new ArrayList<>();

    JLabel blank = new JLabel(" ");
    JLabel header = new JLabel("<HTML><U>Zauberlehrlinge</U></HTML>");

    public LobbyView() {
        setOpaque(false);
        blank.setFont(new Font("Candara", Font.PLAIN, 80));
        header.setFont(new Font("Candara", Font.BOLD, 50));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel h = new JPanel();
        JPanel l = new JPanel();
        l.setLayout(new VerticalFlowLayout());
        l.setOpaque(false);
        h.setLayout(new FlowLayout());
        h.setOpaque(false);
        add(blank);
        h.add(header);
        add(h);
        for(int i = 0; i<6; i++) {
            JLabel name = new JLabel();
            name.setOpaque(false);
            name.setFont(new Font("Candara", Font.PLAIN, 30));
            name.setForeground(Color.BLACK);
            names.add(name);
            l.add(name);
        }
        add(l);
    }

    public void addPlayerName(Player p, int index) {
        String name = p.getName();
        if(name.length()>20) {
            name = name.substring(0, 21) + "...";
        }
        names.get(index).setText("<html><strong><i>Lehrling "+(index+1)+": </i></strong>"+name);
    }
}
