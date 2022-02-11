package view;

import javax.swing.*;

import model.*;

public class CardView extends JLabel {

    private Card c;
    private boolean playable;

    public CardView(Card c) {
        super();
        this.c = c;
        if(c!=null) {
            ImageIcon image = new ImageIcon("./Resources/" + c.getValue() + "_in_" + c.getColor() + ".png");
            setIcon(image);
        }
    }

    public Card getCard() {
        return c;
    }

    public void setPlayable(boolean playable) {
        this.playable = playable;
    }

    public boolean getPlayable() {
        return playable;
    }

    public void setCard(Card c) {
        this.c = c;
        if(c!=null) {
            ImageIcon image = new ImageIcon("./Resources/" + c.getValue() + "_in_" + c.getColor() + ".png");
            setIcon(image);
        } else {
            setIcon(null);
        }
    }
}
