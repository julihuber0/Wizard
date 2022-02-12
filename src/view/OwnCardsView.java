package view;

import model.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class OwnCardsView extends JPanel {

    private ArrayList<CardPanel> cards;
    private GUINew mainGUI;

    public OwnCardsView(ArrayList<CardPanel> cards, GUINew mainGUI) {
        //setLayout(new FlowLayout());
        setLayout(new GridLayout(2,10));
        this.mainGUI = mainGUI;
        this.cards = cards;

        for(CardPanel c: cards) {
            c.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(mainGUI.getInputAllowed()) {
                        CardPanel cv = (CardPanel) e.getSource();
                        if(cv.getPlayable()) {
                            mainGUI.layCard();
                            mainGUI.getCClient().playCard(cv.getCard());
                            removeCard(cv);
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            add(c);
        }
    }

    public void removeCard(CardPanel cv) {
        cv.setVisible(false);
    }

    public void setPlayableCards(boolean[] playableCards) {
        for(int i = 0; i<cards.size(); i++) {
            cards.get(i).setPlayable(playableCards[i]);
        }
    }

    public ArrayList<CardPanel> getOwnHand() {
        return cards;
    }
}
