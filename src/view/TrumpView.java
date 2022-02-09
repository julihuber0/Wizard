package view;

import model.Card;
import model.ColorW;

import javax.swing.*;

public class TrumpView extends JPanel {

    private CardView trump;
    private JLabel trumpColor = new JLabel();

    public TrumpView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void setTrumpCard(Card c) {
        if(c!=null) {
            trump.setVisible(true);
            trump = new CardView(c);
            add(trump);
        } else {
            trump.setVisible(false);

        }
    }

    public void setTrumpColor(ColorW c) {
        if(c != null) {
            trumpColor.setText("Trumpf: "+ColorW.toString(c));
            add(trumpColor);
        } else {
            trumpColor.setText("Trumpf: -");
        }
    }
}
