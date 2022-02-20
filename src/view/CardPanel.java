package view;

import model.Card;

import javax.swing.*;
import java.awt.*;

public class CardPanel extends JPanel {

    private Card c;
    private boolean playable;
    private double firstScaleFactor = 1;

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
        float alpha = 0.5f;
        Composite cs = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha);

        if (c != null) {
            if(c.getValue() == 20) {
                g2.drawImage(getEmptyImage(), 0, 0, (int) (91 * getScaleFactor()), (int) (150 * getScaleFactor()), null);
            } else {
                g2.drawImage(getCardImage(), 0, 0, (int) (91 * getScaleFactor()), (int) (150 * getScaleFactor()), null);
                if (!playable) {
                    g2.setComposite(cs);
                }
            }
        }
    }

    private Image getCardImage() {
        if (c != null) {
            ImageIcon ic = new ImageIcon("./Resources/" + c.getValue() + "_in_" + c.getColor() + ".png");
            return ic.getImage();
        }
        return null;
    }

    private Image getEmptyImage() {
        ImageIcon ic = new ImageIcon("./Resources/empty.png");
        return ic.getImage();
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
        return new Dimension((int) (91 * getScaleFactor()), (int) (150 * getScaleFactor()));
    }

    private double getScaleFactor() {

        Frame jf = Frame.getFrames()[0];
        double dim = Math.min(jf.getHeight(), jf.getWidth());
        double scale = dim / 690;
        if(scale<1) {
            return scale * firstScaleFactor;
        }
        return firstScaleFactor;
    }

    public void setHalfSize() {
        firstScaleFactor = 0.7;
    }

    public void setFullSize() {
        firstScaleFactor = 1;
    }
}
