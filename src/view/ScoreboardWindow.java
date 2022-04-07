package view;

import model.Player;
import model.StitchHistory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class ScoreboardWindow {
    protected ArrayList<Player> players;
    protected JFrame jFrame;
    protected JScrollPane jsp;

    public ScoreboardWindow(ArrayList<Player> players) {
        this.players = players;
        showScoreboard();
    }

    public void updateScoreboard(ArrayList<Player> players) {
        this.players = players;
        generateData();
    }

    public void addWindowListener(WindowListener l) {
        jFrame.addWindowListener(l);
    }

    protected void showScoreboard() {
        //JFrame erstellen

        jFrame = new JFrame();
        jFrame.setSize(new Dimension(450, 280));
        jFrame.setMinimumSize(new Dimension(1, 1));
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ImageIcon icon = new ImageIcon("./Resources/icon.png");
        jFrame.setIconImage(icon.getImage());
        jFrame.setTitle("Wizard-Chat");

        //Daten erstellen
        generateData();

    }

    protected void generateData() {
        String[] header = new String[players.size()+1];
        String[][] data = new String[60/players.size()][players.size()+1];

        //Alte Board clearen
        if(jsp != null) {
            jFrame.remove(jsp);
        }

        //Runde in der 1. Spalte anzeigen
        header[0] = "Runde";
        for(int i = 1; i <= 60/players.size();i++){
            data[i-1][0] = String.valueOf(i);
        }


        for(int p = 0; p < players.size(); p++){

            //Erste Zeile enthält den Namen des Spielers
            header[p+1] = players.get(p).getName();

            //Stichhistorie des Spielers holen und loopen
            ArrayList<StitchHistory> sh = players.get(p).getSh();

            for(int i = 0; i < sh.size();i++){
                data[i][p+1] = sh.get(i).stitches + " | " + sh.get(i).points;
            }
        }

        JTable jt = new JTable(data,header);
        jt.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 20));
        jt.setFont(new Font("SansSerif", Font.PLAIN, 18));
        jt.setDefaultEditor(Object.class, null);

        jt.getColumnModel().getColumn(0).setPreferredWidth(0);

        jsp = new JScrollPane(jt);
        jFrame.add(jsp);

        //Alles zeigen im JFrame
        jFrame.pack();
        jFrame.setVisible(true);
    }
}


