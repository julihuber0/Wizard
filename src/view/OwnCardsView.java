package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OwnCardsView extends JPanel {

    private ArrayList<CardView> cards = new ArrayList<>();

    public OwnCardsView() {
        setLayout(new FlowLayout());
    }
}
