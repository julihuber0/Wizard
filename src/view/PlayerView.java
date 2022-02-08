package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
        setImage(false);
        av.setSize(10, 50);
        av.setIcon(avatar);
        name.setText(p.getName());
        saidStitches.setText("Angesagt: "+p.getSaidStitches());
        madeStitches.setText("Gemacht: "+p.getSaidStitches());
        points.setText("Punkte: "+p.getPoints());

        add(av);
        add(name);
        add(saidStitches);
        add(madeStitches);
        add(points);
    }

    public void updateStats() {
        name.setText(p.getName());
        saidStitches.setText("Angesagt: "+p.getSaidStitches());
        madeStitches.setText("Gemacht: "+p.getSaidStitches());
        points.setText("Punkte: "+p.getPoints());
    }

    public void setImage(boolean t) {
        String path;
        if(t) {
            path = "./Resources/avatarMarked.png";
        } else {
            path = "./Resources/avatar.png";
        }
        ImageIcon ic = new ImageIcon(path);
        avatar = Utility.resizeIcon(ic, 100, 100);
    }

    public void markPlayer() {
        Thread marker = new Thread(new Runnable() {
            @Override
            public void run() {
                String path;
                if(p.getSaidStitches()==p.getCurrentStitches()) {
                    path = "./Resources/avatarRight.png";
                } else {
                    path = "./Resources/avatarWrong.png";
                }
                ImageIcon ic = new ImageIcon(path);
                avatar = Utility.resizeIcon(ic, 100, 100);
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted.");
                }
                setImage(false);
            }
        });
        marker.start();
    }

    public void setHisTurn(boolean hisTurn) {
        setImage(hisTurn);
    }
}
