import java.util.ArrayList;

public class GameW{

    private int playerCount;        //Anzahl Mitspieler
    private int maxRounds;      //Maximale Rundenzahl
    private int currentRound = 0;       //aktuelle Runde
    private GameState gs = GameState.WAITING_FOR_NEXT_ROUND;        //aktueller Spielzustand
    private ArrayList<Player> players = new ArrayList<>();      //Liste, welche alle Spieler beinhaltet
    private ArrayList<Card> stitch = new ArrayList<>();     //Liste, die den aktuellen Stich hält.
    private Color currentTrump = null;      //aktuelle Trumpffarbe
    private CardDeck deck = new CardDeck();     //Kardendeck
    private Player currentPlayer = null;

    public GameW() {

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

    //gibt den Spieler zurück, dem der Stich gehört
    public Player calculateStitch()
    {
        Player p = players.get(0);      //Hier: player(0) kommt raus
        Card highestCard = stitch.get(0);
        int highCardPos = 0;
        for(int i = 1; i<playerCount; i++)
        {
            if(stitch.get(0).getValue()==14)    //Erste Karte Zauberer: Höchste Karte sofort gefunden
            {
                break;
            }
            if(stitch.get(i).getValue()==14)    //sobald ein Zauberer auftritt: Höchste Karte gefunden
            {
                highestCard = stitch.get(i);
                highCardPos = i;
                break;
            }
            if(stitch.get(i).getValue()==0) {   //Narren werden übergangen, bei nur Narren sticht der Erste
                continue;
            }
            if(stitch.get(i).getValue()>highestCard.getValue()&&stitch.get(i).getColor()==highestCard.getColor())
            {
                highestCard = stitch.get(i);    //Wenn Kartenwert größer, als der der aktuellen höchsten Karte und gleiche Farbe: Neue hächste Karte
                highCardPos = i;
                continue;
            }
            if(highestCard.getColor()!=currentTrump&&stitch.get(i).getColor()==currentTrump)    //Trumpf sticht Nicht-Trumpf
            {
                highestCard = stitch.get(i);
                highCardPos = i;
                continue;
            }
        }
        return players.get(highCardPos);
    }

    public Player getWinner()
    {
        if(gs==GameState.OVER)
        {
            int maxPoints = Integer.MIN_VALUE;
            int bestPlayer = 0;
            for(int i = 0; i<playerCount; i++)
            {
                if(players.get(i).getPoints()>maxPoints)
                {
                    maxPoints = players.get(i).getPoints();
                    bestPlayer = i;
                }
            }
            return players.get(bestPlayer);
        }
        return null;
    }

    //gibt eine neue Liste mit Spielern zurück, bei der der übergebene Spieler an erster Stelle ist
    public ArrayList<Player> getNewFirstPlayer(Player p)
    {
        ArrayList<Player> newPlayers = new ArrayList<>();
        newPlayers.add(p);
        boolean behind = false;
        for(int i = 0; i<playerCount; i++)
        {
            if(p.getId()==players.get(i).getId())
            {
                behind = true;
            }
            if(behind == true)
            {
                newPlayers.add(players.get(i));
            }
        }
        Player f = players.get(0);
        for(int i = 1; f.getId()!=p.getId(); i++)
        {
            newPlayers.add(f);
            f = players.get(i);
        }
        return newPlayers;
    }

    //gibt die in diesem Stich erlaubte Farbe zurück
    public Color getAllowed()
    {
        Color allowed = null;
        if(stitch.get(0)!=null)
        {
            if(stitch.get(0).getValue()==14)
            {
                return null;
            }
            for(int i = 0; i<playerCount&&stitch.get(i)!=null; i++)
            {
                if(stitch.get(i).getValue()!=0)
                {
                    allowed = stitch.get(i).getColor();
                    break;
                }
            }
        }
        return allowed;
    }

    //gibt eine Liste mit allen erlaubten/spielbaren Karten eines Spielers zurück
    public ArrayList<Card> getAllowedCards(Player p)
    {
        Color allowed = getAllowed();
        ArrayList<Card> allowedCards = new ArrayList<>();
        if(allowed==null)
        {
            return p.getHand();
        }
        else
        {
            for(Card c:p.getHand())
            {
                if(c.getColor()==allowed)
                {
                    allowedCards.add(c);
                }
            }
        }
        if(allowedCards.isEmpty())
        {
            return p.getHand();
        }
        for(Card c:p.getHand())
        {
            if(c.getValue()==0||c.getValue()==14)
            {
                allowedCards.add(c);
            }
        }
        return allowedCards;
    }

    public void start()
    {
        setPlayerCount(players.size());
        setMaxRounds(players.size());
        startNextRound();
    }

    //startet die nächste Runde
    public void startNextRound()
    {
        gs = GameState.RUNNING;
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
        for(int i = 0; i<currentRound; i++)
        {
            for(int j = 0; j<playerCount; j++)
            {
                stitch.add(players.get(i).requestCard());
            }
            Player p = calculateStitch();
            p.addStitch();
            stitch.clear();
            players = getNewFirstPlayer(p);
        }
        for(int i = 0; i<playerCount; i++)
        {
            if(players.get(i).getSaidStitches()==players.get(i).getCurrentStitches())
            {
                players.get(i).addPoints(20+(players.get(i).getCurrentStitches()*10));
            }
            else
            {
                players.get(i).addPoints(-(10*Math.abs((players.get(i).getSaidStitches()-players.get(i).getCurrentStitches()))));
            }
        }
        gs = GameState.WAITING_FOR_NEXT_ROUND;
    }
}
