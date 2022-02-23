package view;

import model.Card;
import model.ColorW;

import javax.swing.*;

public class TrumpView extends JPanel {

    private CardPanel trump = new CardPanel(null, 1, true);
    private JLabel trumpColor = new JLabel();

    public TrumpView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(trump);
        add(trumpColor);
    }

    public void setTrumpCard(Card c) {
        trump.setCard(c);
    }

    public void setTrumpColor(ColorW c) {
        if(c != null) {
            trumpColor.setText("Trumpf: "+ColorW.toString(c));
        } else {
            trumpColor.setText("Trumpf: -");
        }
    }
}
