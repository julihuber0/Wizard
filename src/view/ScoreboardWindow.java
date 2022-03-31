package view;

import model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ScoreboardWindow extends JFrame {
    private ScoreboardView sbview;

    public ScoreboardWindow(ArrayList<Player> players) {
        setSize(new Dimension(450, 280));
        setMinimumSize(new Dimension(500, 350));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ImageIcon icon = new ImageIcon("./Resources/icon.png");
        setIconImage(icon.getImage());
        setTitle("Wizard-Chat");

        sbview = new ScoreboardView(players);

        pack();
        setVisible(true);
    }

    public void updateScoreboard(ArrayList<Player> players) {
        sbview.updateScoreboard(players);
    }
}
