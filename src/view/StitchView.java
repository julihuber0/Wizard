package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StitchView extends JPanel {

    private ArrayList<CardView> cards = new ArrayList<>();

    public StitchView() {
        setLayout(new FlowLayout());
        for(int i = 0; i<6; i++) {
            CardView cv = new CardView(null);
            cards.add(cv);
            add(cv);
        }
    }

    public ArrayList<CardView> getStitch() {
        return cards;
    }
}
