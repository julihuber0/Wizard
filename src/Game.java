public class Game {

    private int playerCount;
    private int maxRounds;
    private int currentRound = 1;
    private GameState gs = GameState.RUNNING;

    public Game(int playerCount)
    {
        setPlayerCount(playerCount);
        setMaxRounds(playerCount);
    }

    public void setPlayerCount(int pc)
    {
        if(pc>2&&pc<7)
        {
            this.playerCount=pc;
        }
        else
        {
            throw new IllegalArgumentException("Die Spielerzahl muss mindestens 3 und darf maximal 6 sein.");
        }
    }

    public void setMaxRounds(int pc)
    {
        if(pc>2&&pc<7)
        {
            this.maxRounds=60/pc;
        }
        else
        {
            throw new IllegalArgumentException("Die Spielerzahl muss mindestens 3 und darf maximal 6 sein.");
        }
    }

    public void NextRound()
    {
        if(currentRound<maxRounds)
        {
            currentRound++;
        }
        else
        {
            gs = GameState.OVER;
        }
    }
}
