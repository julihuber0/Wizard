package model;

import java.util.ArrayList;
import java.util.Collections;

public class CardDeck {

    private ArrayList<Card> deck;

    //erstellt ein Kartendeck mit allen validen Kombinationsmöglichkeiten
    public CardDeck()
    {
        deck = getValidDeck();
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    //gibt eine Liste zurück, die alle möglichen Kartenkombinationen enthält
    public ArrayList<Card> getValidDeck()
    {
        ArrayList<Card> newDeck = new ArrayList<>();
        for(Integer s:Card.getValidValues())
        {
            for(ColorW c:Card.getValidColors())
            {
                newDeck.add(new Card(s,c));
            }
        }
        return newDeck;
    }

    //entfert die erste Karte vom Deck und gibt diese zurück
    public Card removeCard()
    {
        return deck.remove(0);
    }

    //Ausgabe des Kartendecks als String auf der Konsole
    public String toString()
    {
        String s = "";
        for(Card c:deck)
        {
            s = s+c.getValue()+"_in_"+c.getColor()+"; ";
        }
        return s;
    }

    public boolean isEmpty()
    {
        return deck.isEmpty();
    }

    //Mischt das Kartendeck
    public void shuffleDeck()
    {
        Collections.shuffle(deck);
    }

}
