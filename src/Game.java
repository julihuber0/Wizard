import java.util.ArrayList;

public class Game {

    private int playerCount;
    private int maxRounds;
    private int currentRound = 0;
    private GameState gs = GameState.WAITING_FOR_NEXT_ROUND;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Card> stitch = new ArrayList<>();
    private Color currentTrump = null;
    private CardDeck deck = new CardDeck();

    public Game(int playerCount) {
        setPlayerCount(playerCount);
        setMaxRounds(playerCount);
    }

    public void setPlayerCount(int pc) {
        if (pc > 2 && pc < 7) {
            this.playerCount = pc;
        } else {
            throw new IllegalArgumentException("Die Spielerzahl muss mindestens 3 und darf maximal 6 sein.");
        }
    }

    public void setMaxRounds(int pc) {
        if (pc > 2 && pc < 7) {
            this.maxRounds = 60 / pc;
        } else {
            throw new IllegalArgumentException("Die Spielerzahl muss mindestens 3 und darf maximal 6 sein.");
        }
    }

    public void NextRound() {
        if (currentRound < maxRounds) {
            gs = GameState.RUNNING;
            currentRound++;
        } else {
            gs = GameState.OVER;
        }
    }

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
