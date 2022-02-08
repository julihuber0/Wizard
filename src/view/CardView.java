package view;

import ea.internal.io.ImageLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import model.*;

public class CardView extends JPanel {

    private Card card;
    private BufferedImage im;
    private boolean isPlayable;

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

    public void setPlayable(boolean playable) {
        isPlayable = playable;
    }

    public boolean getPlayable() {
        return isPlayable;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.fillOval(10,10,0,0);
        g2.setColor(Color.BLACK);
        if(isPlayable) {
            g2.drawImage(im, 50, 50, null);
        } else {
            float alpha = 0.5f;
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g2.setComposite(ac);
            g2.drawImage(im, 50, 50, null);
        }
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
