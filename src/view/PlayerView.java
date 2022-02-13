package view;

import javax.swing.*;

import model.*;

import java.awt.*;

public class PlayerView extends JPanel {

    private AvatarView avatar = new AvatarView(MarkerColor.NONE);
    private JLabel name = new JLabel();
    private JLabel saidStitches = new JLabel();
    private JLabel madeStitches = new JLabel();
    private JLabel points = new JLabel();

    private Player p;

    public PlayerView(Player p) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.p = p;
        name.setText(p.getName());
        saidStitches.setText("Angesagt: ");
        madeStitches.setText("Gemacht: -");
        points.setText("Punkte: " + p.getPoints());

        add(avatar);
        add(name);
        add(saidStitches);
        add(madeStitches);
        add(points);
    }

    public void resetStats() {
        saidStitches.setText("Angesagt: -");
        madeStitches.setText("Gemacht: -");
    }

    public void updatePoints() {
        points.setText("Punkte: " + p.getPoints());
    }

    public void updateMadeStitches(int stitches) {
        if (stitches != -1) {
            madeStitches.setText("Gemacht: " + stitches);
        } else {
            madeStitches.setText("Gemacht: -");
        }
    }

    public void updateSaidStitches(int stitches) {
        if (stitches != -1) {
            saidStitches.setText("Angesagt: " + stitches);
        } else {
            saidStitches.setText("Angesagt: -");
        }
    }

    public void setImage(MarkerColor mc) {
        avatar.setMarkerColor(mc);
    }

    public void setOnTurn(boolean onTurn) {
        if (onTurn) {
            setImage(MarkerColor.BLUE);
        } else {
            setImage(MarkerColor.NONE);
        }
    }
}
