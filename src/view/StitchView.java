package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StitchView extends JPanel {

    private ArrayList<CardPanel> cards = new ArrayList<>();

    public StitchView() {
        setOpaque(false);
        setLayout(new FlowLayout());
        for(int i = 0; i<6; i++) {
            CardPanel cv = new CardPanel(null, 1, true);
            cv.setOpaque(false);
            cards.add(cv);
            add(cv);
        }
    }

    public ArrayList<CardPanel> getStitch() {
        return cards;
    }
}
