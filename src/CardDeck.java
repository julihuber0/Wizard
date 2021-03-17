import java.util.ArrayList;

public class CardDeck {

    private ArrayList<Card> deck;

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

    public ArrayList<Card> getValidDeck()
    {
        ArrayList<Card> newDeck = new ArrayList<>();
        for(String s:Card.getValidValues())
        {
            for(Color c:Card.getValidColors())
            {
                newDeck.add(new Card(s,c));
            }
        }
        return newDeck;
    }

    public Card removeCard()
    {
        return deck.remove(0);
    }

    public String toString()
    {
        String s = "";
        for(Card c:deck)
        {
            s = s+c.getValue()+"_in_"+c.getColor()+"; ";
        }
        return s;
    }

}
