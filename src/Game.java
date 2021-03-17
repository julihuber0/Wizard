import java.util.ArrayList;

public class Game {

    private int playerCount;        //Anzahl Mitspieler
    private int maxRounds;      //Maximale Rundenzahl
    private int currentRound = 0;       //aktuelle Runde
    private GameState gs = GameState.WAITING_FOR_NEXT_ROUND;        //aktueller Spielzustand
    private ArrayList<Player> players = new ArrayList<>();      //Liste, welche alle Spieler beinhaltet
    private ArrayList<Card> stitch = new ArrayList<>();     //Liste, die den aktuellen Stich hält
    private Color currentTrump = null;      //aktuelle Trumpffarbe
    private CardDeck deck = new CardDeck();     //Kardendeck

    public Game(int playerCount) {
        setPlayerCount(playerCount);
        setMaxRounds(playerCount);
    }

    //legt fest, wie viele Spieler mitspielen
    public void setPlayerCount(int pc) {
        if (pc > 2 && pc < 7) {
            this.playerCount = pc;
        } else {
            throw new IllegalArgumentException("Die Spielerzahl muss mindestens 3 und darf maximal 6 sein.");
        }
    }

    //setzt abhängig von der Spielerzahl die maximale Rundenzahl
    public void setMaxRounds(int pc) {
        if (pc > 2 && pc < 7) {
            this.maxRounds = 60 / pc;
        } else {
            throw new IllegalArgumentException("Die Spielerzahl muss mindestens 3 und darf maximal 6 sein.");
        }
    }

    //schaltet den Rundenzähler auf die nächste Runde, setzt GameState auf OVER, wenn letzte Runde
    public void NextRound() {
        if (currentRound < maxRounds) {
            currentRound++;
        } else {
            gs = GameState.OVER;
        }
    }

    //teilt rundenabhängig Karten an die Spieler aus
    public void distribute(int round)
    {
        for(Player p:players)
        {
            for(int i = 0; i<round; i++)
            {
                p.addCards(deck.removeCard());
            }
        }
    }

    //startet die nächste Runde
    public void startRound()
    {
        NextRound();
        if(gs != GameState.OVER)
        {
            distribute(currentRound);
            Card trumpCard = deck.removeCard();
            if(trumpCard.getValue().equals("N"))
            {
                currentTrump = null;
            }
            else if(trumpCard.getValue().equals("Z"))
            {
                currentTrump = players.get(0).getBestColor();
            }
            else
            {
                currentTrump = trumpCard.getColor();
            }
        }
    }
}
