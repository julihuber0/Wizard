package view;

import model.Card;
import model.ColorW;

import javax.swing.*;

public class TrumpView extends JPanel {

    private CardView trump = new CardView(null);
    private JLabel trumpColor = new JLabel();

    public TrumpView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(trump);
        add(trumpColor);
    }

    public void setTrumpCard(Card c) {
        if(c!=null) {
            trump.setVisible(true);
            trump.setCard(c);
            add(trump);
        } else {
            trump.setVisible(false);

        }
    }

    public void setTrumpColor(ColorW c) {
        if(c != null) {
            trumpColor.setText("Trumpf: "+ColorW.toString(c));
        } else {
            trumpColor.setText("Trumpf: -");
        }
    }
}
