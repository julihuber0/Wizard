import java.util.ArrayList;
import java.util.Collections;

public class Player {

    private String name;
    private int id;
    private ArrayList<StitchHistory> hs = new ArrayList<>();
    private int points;
    private int saidStitches;   //angesagte Stiche
    private int currentStitches;    //schon gemachte Stiche
    public ArrayList<Card> hand = new ArrayList<>();    //Karten auf der Hand
    private ClientHandler ch;

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
        points = 0;
        saidStitches = 0;
        currentStitches = 0;
    }

    public void setCh(ClientHandler ch) {
        this.ch = ch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public void addStitch()
    {
        currentStitches++;
    }

    public void addPoints(int i)
    {
        points = points + i;
        hs.add(new StitchHistory(saidStitches, points));
    }

    //Sortiert die Hand des Spielers
    public void sortHand()
    {
        Collections.sort(hand);
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

    public void addCards(Card c) {
        hand.add(c);
    }

    public Card requestCard() {

        return new Card(1,Color.BLUE);
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    //gibt die Farbe aus, von der der Spieler die meisten Karten hat
    public Color getBestColor() {
        int[] colors = new int[4]; //0: green; 1: yellow; 2: red; 3: blue
        for (Card c : hand) {
            if (c.getColor() == Color.GREEN) {
                colors[0]++;
            }
        }
        for (Card c : hand) {
            if (c.getColor() == Color.YELLOW) {
                colors[1]++;
            }
        }
        for (Card c : hand) {
            if (c.getColor() == Color.RED) {
                colors[2]++;
            }
        }
        for (Card c : hand) {
            if (c.getColor() == Color.BLUE) {
                colors[3]++;
            }
        }
        int max = 0;
        int maxPos = 0;
        for (int i = 0; i < 4; i++) {
            if (colors[i] > max) {
                max = colors[i];
                maxPos = i;
            }
        }

        if (maxPos == 0) {
            return Color.GREEN;
        }
        if (maxPos == 1) {
            return Color.YELLOW;
        }
        if (maxPos == 2) {
            return Color.RED;
        }
        return Color.BLUE;
    }
}
