package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import model.Player;

public class OtherPlayersView extends JPanel {

    private ArrayList<PlayerView> playerView;

    public OtherPlayersView(ArrayList<PlayerView> playerView) {
        this.playerView = playerView;
        setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));
        for(PlayerView pv: playerView) {
            add(pv);
        }
    }
}
