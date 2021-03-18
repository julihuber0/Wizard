import java.util.ArrayList;

public class Game {

    private int playerCount;        //Anzahl Mitspieler
    private int maxRounds;      //Maximale Rundenzahl
    private int currentRound = 0;       //aktuelle Runde
    private GameState gs = GameState.WAITING_FOR_NEXT_ROUND;        //aktueller Spielzustand
    private ArrayList<Player> players = new ArrayList<>();      //Liste, welche alle Spieler beinhaltet
    private ArrayList<Card> stitch = new ArrayList<>();     //Liste, die den aktuellen Stich hält.
    private Color currentTrump = null;      //aktuelle Trumpffarbe
    private CardDeck deck = new CardDeck();     //Kardendeck
    private Player currentPlayer = null;

    public Game(int playerCount) {
        setPlayerCount(playerCount);
        setMaxRounds(playerCount);
    }

    public void addPlayer(Player p)
    {
        players.add(p);
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
    public void nextRound() {
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

    public Player calculateStitch()
    {
        Player p = players.get(0);
        Card highestCard = stitch.get(0);
        int highCardPos = 0;
        for(int i = 1; i<playerCount; i++)
        {
            if(stitch.get(0).getValue()==14)
            {
                break;
            }
            if(stitch.get(i).getValue()==14)
            {
                highestCard = stitch.get(i);
                highCardPos = i;
                break;
            }
            if(stitch.get(i).getValue()==0) {
                continue;
            }
            if(stitch.get(i).getValue()>highestCard.getValue()&&stitch.get(i).getColor()==highestCard.getColor())
            {
                highestCard = stitch.get(i);
                highCardPos = i;
                continue;
            }
            if(highestCard.getColor()!=currentTrump&&stitch.get(i).getColor()==currentTrump)
            {
                highestCard = stitch.get(i);
                highCardPos = i;
                continue;
            }
        }
        return null;
    }

    //startet die nächste Runde
    public void startRound()
    {
        nextRound();
        if(gs != GameState.OVER)
        {
            distribute(currentRound);
            Card trumpCard = deck.removeCard();
            if(trumpCard.getValue()==0)
            {
                currentTrump = null;
            }
            else if(trumpCard.getValue()==14)
            {
                currentTrump = players.get((currentRound-1)%playerCount).getBestColor();
            }
            else
            {
                currentTrump = trumpCard.getColor();
            }
        }
    }
}
