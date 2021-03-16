import java.util.ArrayList;

public class CardDeck {

    private ArrayList<Card> deck;

    public CardDeck()
    {
        deck = getValidDeck();
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

}
