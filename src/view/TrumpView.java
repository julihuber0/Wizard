package view;

import model.Card;
import model.ColorW;

import javax.swing.*;
import java.awt.*;

public class TrumpView extends JPanel {

    private CardPanel trump = new CardPanel(null, 1, true);
    private JLabel trumpColor = new JLabel();
    private JLabel blank = new JLabel(" ");

    public TrumpView() {
        blank.setFont(new Font("Candara", Font.PLAIN, 40));
        blank.setSize(2,40);
        setOpaque(false);
        trump.setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        trumpColor.setForeground(Color.WHITE);
        trumpColor.setFont(new Font("Candara", Font.PLAIN, 15));
        add(trump);
        add(trumpColor);
        add(blank);
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
