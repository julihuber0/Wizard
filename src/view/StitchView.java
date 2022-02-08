package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StitchView extends JPanel {

    private ArrayList<CardView> cards = new ArrayList<>();

    public StitchView() {
        setLayout(new FlowLayout());
    }

    public void addCard(CardView cv) {
        cards.add(cv);
        add(cv);
    }

    public void clearCards() {
        for(CardView cv:cards) {
            cv.setVisible(false);
        }
        cards.clear();
    }
}
