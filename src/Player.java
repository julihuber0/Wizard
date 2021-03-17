import java.util.ArrayList;

public class Player {

    private String name;
    private int points;
    private int saidStitches;
    private int currentStitches;
    public ArrayList<Card> hand = new ArrayList<>();

    public Player(String name)
    {
        this.name=name;
        points = 0;
        saidStitches=0;
        currentStitches=0;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getSaidStitches() {
        return saidStitches;
    }

    public void setSaidStitches(int saidStitches) {
        this.saidStitches = saidStitches;
    }

    public int getCurrentStitches() {
        return currentStitches;
    }

    public void setCurrentStitches(int currentStitches) {
        this.currentStitches = currentStitches;
    }

    public void addCards(Card c)
    {
        hand.add(c);
    }
}
