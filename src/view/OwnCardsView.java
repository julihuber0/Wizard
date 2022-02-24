package view;

import model.Card;
import model.ColorW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class OwnCardsView extends JPanel {

    private ArrayList<CardPanel> cards;
    private int cardCount;
    private GUINew mainGUI;
    private CardPanel emptyCard;

    public OwnCardsView(ArrayList<CardPanel> cards, GUINew mainGUI) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        this.mainGUI = mainGUI;
        emptyCard = new CardPanel(new Card(20, ColorW.YELLOW), mainGUI.getInitScale());
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
            c.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(mainGUI.getInputAllowed()) {
                        CardPanel cp = (CardPanel) e.getSource();
                        if(cp.getPlayable()) {
                            mainGUI.lockInput();
                            mainGUI.getCClient().playCard(cp.getCard());
                            cp.mouseExited();
                            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            removeCard(cp);
                            mainGUI.resetPlayableCards();
                        } else if(!mainGUI.getMuted()){
                            Toolkit.getDefaultToolkit().beep();
                        }
                    } else if(!mainGUI.getMuted()){
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    if(mainGUI.getInputAllowed()) {
                        CardPanel cp = (CardPanel) e.getSource();
                        if(cp.getPlayable()) {
                            setCursor(new Cursor(Cursor.HAND_CURSOR));
                            cp.mouseEntered();
                        }
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if(mainGUI.getInputAllowed()) {
                        CardPanel cp = (CardPanel) e.getSource();
                        if(cp.getPlayable()) {
                            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            cp.mouseExited();
                        }
                    }
                }
            });
            add(c);
        }
    }
}
