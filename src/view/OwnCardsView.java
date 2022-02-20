package view;

import model.Card;
import model.ColorW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class OwnCardsView extends JPanel {

    private ArrayList<CardPanel> cards;
    private int cardCount;
    private GUINew mainGUI;
    private CardPanel emptyCard = new CardPanel(new Card(20, ColorW.YELLOW));

    public OwnCardsView(ArrayList<CardPanel> cards, GUINew mainGUI) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        this.mainGUI = mainGUI;
        add(emptyCard);
        initHand(cards);

    }

    public void removeCard(CardPanel cp) {
        cp.setVisible(false);
        cardCount--;
        if(cardCount == 0) {
            emptyCard.setVisible(true);
        }
    }

    public void setPlayableCards(boolean[] playableCards) {
        for(int i = 0; i<cards.size(); i++) {
            cards.get(i).setPlayable(playableCards[i]);
        }
    }

    public ArrayList<CardPanel> getOwnHand() {
        return cards;
    }

    public void initHand(ArrayList<CardPanel> cards) {
        emptyCard.setVisible(false);
        this.cards = cards;
        cardCount = cards.size();

        for(CardPanel c: cards) {
            if(cards.size()>9) {
                c.setHalfSize();
            }
            c.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(mainGUI.getInputAllowed()) {
                        CardPanel cp = (CardPanel) e.getSource();
                        if(cp.getPlayable()) {
                            mainGUI.layCard();
                            mainGUI.getCClient().playCard(cp.getCard());
                            removeCard(cp);
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
}
