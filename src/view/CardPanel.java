package view;

import model.Card;

import javax.swing.*;
import java.awt.*;

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
        Frame f = Frame.getFrames()[0];

        if(c!=null) {
            g2.drawImage(getCardImage(), 0, 0, 100, 150, null);
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
}
