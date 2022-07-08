package view;

import model.Card;

import javax.swing.*;
import java.awt.*;

public class CardPanel extends JPanel {

    private Card c;
    private boolean playable;
    private double initScale;
    private double firstScaleFactor = 1;
    private double mouseZoom = 1;

    public CardPanel(Card c, double scale) {
        setOpaque(false);
        setCard(c);
        initScale = scale;
    }

    public CardPanel(Card c, double scale, boolean playable) {
        this.playable = playable;
        setCard(c);
        initScale = scale;
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
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        AlphaComposite ac2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
        if(!playable) {
            g2.setComposite(ac);
        } else {
            g2.setComposite(ac2);
        }

        if (c != null) {
            if(c.getValue() == 20) {
                g2.drawImage(getEmptyImage(), 0, 0, (int) (91 * getScaleFactor()), (int) (150 * getScaleFactor()), null);
            } else {
                g2.drawImage(getCardImage(), 0, 0, (int) (91 * getScaleFactor()), (int) (150 * getScaleFactor()), null);
            }
        }
        setOpaque(false);
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
        if(this.playable != playable) {
            this.playable = playable;
            repaint();
        }
    }

    public boolean getPlayable() {
        return playable && c != null;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int) (91 * getScaleFactor()), (int) (150 * getScaleFactor()));
    }

    private double getScaleFactor() {
        return initScale * firstScaleFactor * mouseZoom;
    }

    public void setHalfSize() {
        firstScaleFactor = 0.85;
    }

    public void mouseEntered() {
        mouseZoom = 0.95;
        repaint();
    }

    public void mouseExited() {
        mouseZoom = 1.0;
        repaint();
    }
}
