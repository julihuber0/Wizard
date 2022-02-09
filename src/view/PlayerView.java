package view;

import javax.swing.*;

import model.*;

public class PlayerView extends JPanel {

    private ImageIcon avatar;

    private JLabel av = new JLabel();
    private JLabel name = new JLabel();
    private JLabel saidStitches = new JLabel();
    private JLabel madeStitches = new JLabel();
    private JLabel points = new JLabel();

    private Player p;

    public PlayerView(Player p) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.p = p;
        setImage(MarkerColor.NONE);
        av.setIcon(avatar);
        name.setText(p.getName());
        saidStitches.setText("Angesagt: ");
        madeStitches.setText("Gemacht: -");
        points.setText("Punkte: " + p.getPoints());

        add(av);
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
        av.setIcon(null);
        av.setVisible(false);
        String path;
        if (mc == MarkerColor.BLUE) {
            path = "./Resources/avatarMarked.png";
        } else if (mc == MarkerColor.NONE) {
            path = "./Resources/avatar.png";
        } else if (mc == MarkerColor.GREEN) {
            path = "./Resources/avatarRight.png";
        } else {
            path = "./Resources/avatarWrong.png";
        }
        ImageIcon ic = new ImageIcon(path);
        avatar = Utility.resizeIcon(ic, 100, 100);
        av.setIcon(avatar);
        av.setVisible(true);
    }

    public void setOnTurn(boolean onTurn) {
        if (onTurn) {
            setImage(MarkerColor.BLUE);
        } else {
            setImage(MarkerColor.NONE);
        }
    }
}
