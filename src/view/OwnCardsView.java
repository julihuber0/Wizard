package view;

import model.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class OwnCardsView extends JPanel {

    private ArrayList<CardView> cards;
    private GUINew mainGUI;

    public OwnCardsView(ArrayList<CardView> cards, GUINew mainGUI) {
        setLayout(new FlowLayout());
        this.mainGUI = mainGUI;
        this.cards = cards;

        for(CardView c: cards) {
            c.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(mainGUI.getInputAllowed()) {
                        CardView cv = (CardView) e.getSource();
                        if(cv.getPlayable()) {
                            mainGUI.layCard(cv);
                            mainGUI.getCClient().playCard(cv.getCard());
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

    public void removeCard(CardView cv) {
        cv.setVisible(false);
    }

    public void setPlayableCards(boolean[] playableCards) {
        for(int i = 0; i<cards.size(); i++) {
            cards.get(i).setPlayable(playableCards[i]);
            cards.get(i).repaint();
        }
    }
}
