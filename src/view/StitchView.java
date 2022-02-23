package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StitchView extends JPanel {

    private ArrayList<CardPanel> cards = new ArrayList<>();

    public StitchView() {
        setLayout(new FlowLayout());
        for(int i = 0; i<6; i++) {
            CardPanel cv = new CardPanel(null, 1, true);
            cards.add(cv);
            add(cv);
        }
    }

    public ArrayList<CardPanel> getStitch() {
        return cards;
    }
}
