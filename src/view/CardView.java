package view;

import ea.internal.io.ImageLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CardView extends JPanel {

    private Card card;
    private BufferedImage im;

    public CardView(Card c) {
        card = c;
        setImage();
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.fillOval(10,10,0,0);
        g2.setColor(Color.BLACK);
        g2.drawImage(im, 50, 50, null);
    }

    public void setImage() {
        String path =
                "./Resources/" + card.getValue() + "_in_" + card.getColor() + ".png";
        try {
            im = ImageIO.read(new File(path));
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
