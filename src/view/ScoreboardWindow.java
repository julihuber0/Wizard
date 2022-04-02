package view;

import model.Player;
import model.StitchHistory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ScoreboardWindow {
    protected ArrayList<Player> players;
    protected JFrame jFrame;

    public ScoreboardWindow(ArrayList<Player> players) {
        updateScoreboard(players);
    }

    public void updateScoreboard(ArrayList<Player> players) {
        this.players = players;
        showScoreboard();
    }

    protected void showScoreboard() {
        //JFrame erstellen
        jFrame = new JFrame();
        jFrame.setSize(new Dimension(450, 280));
        jFrame.setMinimumSize(new Dimension(500, 350));
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ImageIcon icon = new ImageIcon("./Resources/icon.png");
        jFrame.setIconImage(icon.getImage());
        jFrame.setTitle("Wizard-Chat");

        //Daten erstellen
        generateData();

        //Alles zeigen im JFrame
        jFrame.pack();
        jFrame.setVisible(true);
    }

    protected void generateData() {
        String[] header = new String[players.size()];
        String[][] data = new String[60/players.size()][players.size()];

        for(int p = 0; p < players.size(); p++){

            //Erste Zeile enthält den Namen des Spielers
            header[p] = players.get(p).getName();

            //Stichhistore des Spielers holen und loopen
            ArrayList<StitchHistory> sh = players.get(p).getSh();

            for(int i = 0; i < sh.size();i++){
                data[i][p] = sh.get(i).stitches + " | " + sh.get(i).points;
            }
        }

        JTable jt = new JTable(data,header);
        jt.setDefaultEditor(Object.class, null);
        jFrame.add(new JScrollPane(jt));
    }
}


