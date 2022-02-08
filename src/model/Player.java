package model;

import java.util.ArrayList;
import java.util.Collections;

public class Player implements Comparable<Player>{

    private String name;
    private int id;
    public ArrayList<StitchHistory> sh = new ArrayList<>();
    private int points;
    public int saidStitches;   //angesagte Stiche
    public int currentStitches;    //schon gemachte Stiche
    public ArrayList<Card> hand = new ArrayList<>();    //Karten auf der Hand
    private ClientHandler ch;
    public Card played;
    public int dynNumber = -1;

    public ColorW selectedTrump = null; //nur bei dem Sonderfall befüllt, dass der Spieler einen Farbe wählen kann

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
        points = 0;
        saidStitches = -1;
        currentStitches = 0;
    }

    public void sendHand() {
        //sendet die Arraylist hand an den zugehörigen Client
        String cards = "HA/";
        for (Card c : hand) {
            cards = cards + c.getValue() + ";" + c.getColor() + "/";
        }
        ch.sendeString(cards);
    }

    public void sendPlayableCards(ArrayList<Card> playable) {
        String cards = "HS/";
        for (Card c : playable) {
            cards = cards + c.getValue() + ";" + c.getColor() + "/";
        }
        ch.sendeString(cards);
    }

    public int getLastPoints() {
        int i = sh.size() - 1;
        if (i >= 0) {
            return sh.get(i).getPoints();
        }
        return 0;
    }

    public void clearHand() {
        hand.clear();
    }

    public void selectTrump() {
        ch.sendeString("TF/");
    }

    public void sayStitches(int forbiddenNumber) {
        ch.sendeString("SF/" + forbiddenNumber);
    }

    public ArrayList<StitchHistory> getSh() {
        return sh;
    }

    public void addToSH(int stitches, int points) {
        sh.add(new StitchHistory(stitches, points));
    }

    public void clearSH() {
        sh.clear();
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

    public void addStitch() {
        currentStitches++;
    }

    public void addPoints(int i) {
        points = points + i;
        sh.add(new StitchHistory(saidStitches, points));
    }

    //Sortiert die Hand des Spielers
    public void sortHand() {
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

    /*
        public model.Card requestCard() {

            return new model.Card(1, model.ColorW.BLUE);
        }
    */
    public void requestCard() {
        played = null;
        ch.sendeString("KS/");
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    //gibt die Farbe aus, von der der Spieler die meisten Karten hat
    public ColorW getBestColor() {
        int[] colors = new int[4]; //0: green; 1: yellow; 2: red; 3: blue
        for (Card c : hand) {
            if (c.getColor() == ColorW.GREEN) {
                colors[0]++;
            }
        }
        for (Card c : hand) {
            if (c.getColor() == ColorW.YELLOW) {
                colors[1]++;
            }
        }
        for (Card c : hand) {
            if (c.getColor() == ColorW.RED) {
                colors[2]++;
            }
        }
        for (Card c : hand) {
            if (c.getColor() == ColorW.BLUE) {
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
            return ColorW.GREEN;
        }
        if (maxPos == 1) {
            return ColorW.YELLOW;
        }
        if (maxPos == 2) {
            return ColorW.RED;
        }
        return ColorW.BLUE;
    }

    public void setThread(Thread t) {
        ch.setT(t);
    }


    @Override
    public int compareTo(Player o) {
        if(o.getId()>this.getId()){
            return -1;
        }
        if(o.getId()<this.getId()){
            return 1;
        }
        return 0;
    }
}
