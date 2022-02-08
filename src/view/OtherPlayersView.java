package view;

import javax.swing.*;
import java.awt.*;
import model.Player;

public class OtherPlayersView extends JPanel {

    private PlayerView[] playerView;

    public OtherPlayersView(Player[] players) {
        playerView = new PlayerView[players.length];
        setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));
        for(int i = 0; i<players.length; i++) {
            playerView[i] = new PlayerView(players[i]);
            add(playerView[i]);
        }
    }
}
