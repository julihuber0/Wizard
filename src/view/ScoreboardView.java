package view;

import model.Player;
import model.StitchHistory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ScoreboardView extends JPanel {
    ArrayList<Player> players;

    public ScoreboardView(ArrayList<Player> players) {
        super(new GridLayout(1, 0, 5, 5));
        this.players = players;
        showScoreboard();
    }


    public void updateScoreboard(ArrayList<Player> players) {
        this.players = players;
        showScoreboard();
    }
    protected void showScoreboard() {
        JTable jt = new JTable((int)(60/players.size() + 1),players.size());

        for(int p = 0; p < players.size(); p++){

            //Erste Zeile enthält den Namen des Spielers
            jt.setValueAt(players.get(p).getName(),0,p);

            //Stichhistore des Spielers holen und loopen
            ArrayList<StitchHistory> sh = players.get(p).getSh();

            for(int i = 0; i < sh.size();i++){
                jt.setValueAt(sh.get(i).stitches + " | " + sh.get(i).points, i+1, p);
            }
        }

        super.add(new JScrollPane(jt), BorderLayout.CENTER);
    }
}
