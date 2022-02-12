package view;

import model.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CardPanel extends JPanel {

    private Card c;
    private boolean playable;

    public CardPanel(Card c) {
        setCard(c);
    }

    public void setCard(Card c) {
        this.c = c;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if(c!=null) {
            g2.drawImage(getCardImage(), 0, 0, 91*getScaleFactor(), 150+getScaleFactor(), null);
        }
    }

    private Image getCardImage() {
        if(c!=null) {
            ImageIcon ic = new ImageIcon("./Resources/" + c.getValue() + "_in_" + c.getColor() + ".png");
            return ic.getImage();
        }
        return null;
    }

    public Card getCard() {
        return c;
    }

    public void setPlayable(boolean playable) {
        this.playable = playable;
    }

    public boolean getPlayable() {
        return playable && c != null;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(91, 150);
    }

    private int getScaleFactor() {
        JFrame jf = (JFrame) getTopLevelAncestor();
        int dim = Math.min(jf.getHeight(), jf.getWidth());
        return dim/1300;
    }
}
