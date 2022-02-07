import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlayerView extends JPanel {

    private ImageIcon avatar;

    private JLabel av = new JLabel();
    private JLabel name = new JLabel();
    private JLabel saidStitches = new JLabel();
    private JLabel madeStitches = new JLabel();
    private JLabel points = new JLabel();

    private Player p;

    public PlayerView(Player p) {
        setLayout(new BoxLayout(null, BoxLayout.Y_AXIS));

        this.p = p;
        setImage(false);
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
            path = "./Resources/avatar2.png";
        } else {
            path = "./Resources/avatar.png";
        }
        try {
            avatar = new ImageIcon(getClass().getResource(path));
        } catch (Exception e) {
            System.out.println("Image not found!");
        }
    }

    public void setHisTurn(boolean hisTurn) {
        setImage(hisTurn);
    }
}
