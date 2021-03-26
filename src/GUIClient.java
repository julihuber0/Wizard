import ea.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class GUIClient extends Game implements MausReagierbar {
    private Maus maus;
    private Text joinButton;
    private Text playButton;
    private Text backButton;
    private Bild bg;
    private Bild logo;
    private Bild[] s = new Bild[5];
    private Rechteck[] marker = new Rechteck[5];
    private Text[] names = new Text[5];
    private Text[] saidStitches = new Text[5];
    private Text[] points = new Text[5];
    private Bild[] stitchImage = new Bild[6];
    private Bild[] ownHand = new Bild[20];
    private Text[] playerList = new Text[6];
    private Rechteck ownMarker;
    private Bild ownAvatar;
    private Text name;
    private Text ownSaidStitches;
    private Text ownPoints;
    private Text l;
    private Rechteck sbBG;
    private Text winner;
    private Text[] scoreboard = new Text[6];


    private Text[][] eScoreboard = new Text[13][21];


    private boolean inputAllowed = false;
    private boolean[] allowedCards = new boolean[20];

    private CClient cClient;

    //Nur zur Datenhaltung
    public ArrayList<Player> players = new ArrayList<>(); //currentPoints, hand, clienthandler sind nicht ausgefüllt
    public ArrayList<Card> hand = new ArrayList<>();    //Karten auf der Hand
    public ArrayList<Card> stitch = new ArrayList<>(); //Karten, die aktuell auf dem Tisch liegen
    public ColorW currentTrump = null;      //aktuelle Trumpffarbe
    public int currentRound = 0;       //aktuelle Runde
    public int currentPlayerID = 0; //ID des Spielers, der gerade an der Reihe ist
    public int idSelf = 0; //ID des Spielers, der man selbst ist


    public GUIClient() {
        super(1200, 650, "Wizard", false, false);

        maus = new Maus(new Bild(0, 0, "Resources/pointer.png"), new Punkt(0, 0));
        mausAnmelden(maus);

        bg = new Bild(0, 0, "Resources/BG2.jpg");
        sichtbarMachen(bg);

        //Test
        inputAllowed = true;
        hand.add(new Card(3, ColorW.YELLOW));
        hand.add(new Card(2, ColorW.YELLOW));
        hand.add(new Card(1, ColorW.YELLOW));
        hand.add(new Card(0, ColorW.BLUE));
        allowedCards[0] = false;
        allowedCards[1] = true;
        allowedCards[2] = false;
        allowedCards[3] = true;

        //Main Menu
        joinButton = new Text("Beitreten", 200, 200, 20);
        sichtbarMachen(joinButton);
        backButton = new Text("Zurück", 200, 300, 20);
        sichtbarMachen(backButton);
        backButton.sichtbarSetzen(false);
        maus.anmelden(this, joinButton, 0);
        maus.anmelden(this, backButton, 1);
        playButton = new Text("Start", 200, 250, 20);
        sichtbarMachen(playButton);
        maus.anmelden(this, playButton, 2);
        logo = new Bild(700, 100, "Resources/wizardgame.png");
        sichtbarMachen(logo);

        //Mitspieler-Liste
        for (int i = 0; i < 6; i++) {
            playerList[i] = new Text("empty", 700, 150 + i * 20, 15);
            sichtbarMachen(playerList[i]);
            playerList[i].sichtbarSetzen(false);
        }

        //Game-GUI
        l = new Text("Karte legen", 10, 350, 20);
        sichtbarMachen(l);
        l.sichtbarSetzen(false);
        maus.anmelden(this, l, 3);

        //TODO: @Julian Schleifenbedingungen an Spielerzahl etc. anpassen

        //Marker erzeugen
        for (int i = 0; i < 5; i++) {
            marker[i] = new Rechteck(100 + i * 200, 20, 150, 150);
            sichtbarMachen(marker[i]);
            marker[i].sichtbarSetzen(false);
        }
        ownMarker = new Rechteck(10, 430, 150, 150);
        sichtbarMachen(ownMarker);
        ownMarker.sichtbarSetzen(false);
        //Avatare erzeugen
        for (int i = 0; i < 5; i++) {
            s[i] = new Bild(100 + i * 200, 20, "Resources/avatar.png");
            sichtbarMachen(s[i]);
            s[i].sichtbarSetzen(false);
        }
        ownAvatar = new Bild(10, 430, "Resources/avatar.png");
        sichtbarMachen(ownAvatar);
        ownAvatar.sichtbarSetzen(false);

        //Namen erzeugen
        for (int i = 0; i < 5; i++) {
            names[i] = new Text("Spieler" + (i + 2), 110 + i * 200, 180, 15);
            sichtbarMachen(names[i]);
            names[i].sichtbarSetzen(false);
        }
        name = new Text("Spieler1", 20, 590, 15);
        sichtbarMachen(name);
        name.sichtbarSetzen(true);

        //Angesagte Stiche anzeigen
        for (int i = 0; i < 5; i++) {
            saidStitches[i] = new Text("Angesagt: ", 110 + i * 200, 200, 15);
            sichtbarMachen(saidStitches[i]);
            saidStitches[i].sichtbarSetzen(false);
        }
        ownSaidStitches = new Text("Angesagt: ", 20, 610, 15);
        sichtbarMachen(ownSaidStitches);
        ownSaidStitches.sichtbarSetzen(false);

        //Punkte anzeigen
        for (int i = 0; i < 5; i++) {
            points[i] = new Text("Punkte: ", 110 + i * 200, 220, 15);
            sichtbarMachen(points[i]);
            points[i].sichtbarSetzen(false);
        }
        ownPoints = new Text("Punkte: ", 20, 630, 15);
        sichtbarMachen(ownPoints);
        ownPoints.sichtbarSetzen(false);
    }

    public void showPlayerInList(String name, int id) {
        playerList[id].setzeInhalt(name);
        playerList[id].sichtbarSetzen(true);
    }

    public void setPlayableCards(ArrayList<Card> cards) {
        for (int i = 0; i < currentRound; i++) {
            if (cards.contains(hand.get(i))) {
                allowedCards[i] = true;
            } else {
                allowedCards[i] = false;
            }
        }
    }

    public void showOwnHand() {
        Collections.sort(hand);
        for (int i = 0; i < 4; i++) {
            ownHand[i] = new Bild(200 + i * 95, 500, "Resources/" + hand.get(i).getValue() + "_in_" + hand.get(i).getColor() + ".png");
            sichtbarMachen(ownHand[i]);
            ownHand[i].sichtbarSetzen(true);
            maus.anmelden(this, ownHand[i], 100 + i);
        }
    }

    public String askForTrumpColor() {
        return eingabeFordern("Gewünschte Trumpffarbe eingeben (grün, blau, rot, gelb)");
    }

    //ToDo @Tobi diesen Fall in die Kommunikation einbinden
    public ColorW validateTrump() {
        String trumpColor = askForTrumpColor();
        if (trumpColor.equals("grün")) {
            return ColorW.GREEN;
        }
        if (trumpColor.equals("blau")) {
            return ColorW.BLUE;
        }
        if (trumpColor.equals("rot")) {
            return ColorW.RED;
        }
        if (trumpColor.equals("gelb")) {
            return ColorW.YELLOW;
        }
        return validateTrump();
    }

    public Player getPlayerByID(int id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void tasteReagieren(int i) {

    }

    public void sichtbarMachen(Raum m) {
        wurzel.add(m);
    }

    public String getInputName() {
        return eingabeFordern("Name eingeben:");
    }

    public String getInputIP() {
        return eingabeFordern("Server-IP-Adresse:");
    }

    public void karteLegen(Card c) {
        for (int i = 0; i < 6; i++) {
            if (stitchImage[i] == null) {
                stitchImage[i] = new Bild(200 + i * 50, 300, "./Resources/" + c.getValue() + "_in_" + c.getColor() + ".png");
                sichtbarMachen(stitchImage[i]);
                break;
            }
        }
    }

    public void clearOnwHand() {
        for (int i = 0; i < currentRound; i++) {
            wurzel.entfernen(ownHand[i]);
        }
    }

    public void clearCurrentStitch() {
        for (int i = 0; i < players.size(); i++) {
            wurzel.entfernen(stitchImage[i]);
        }
    }

    public void markPlayer(int p) {
        for (int i = 0; i < 5; i++) {
            marker[i].sichtbarSetzen(false);
        }
        ownMarker.sichtbarSetzen(false);
        if (p != 0) {
            marker[p - 1].sichtbarSetzen(true);
        } else {
            ownMarker.sichtbarSetzen(true);
        }
    }

    public void requestCard() {
        greyOutCards();
        inputAllowed = true;
    }

    public void greyOutCards() {
        for (int i = 0; i < currentRound; i++) {
            if (allowedCards[i]) {
                ownHand[i].setOpacity(1f);
            } else {
                ownHand[i].setOpacity(0.5f);
            }
        }
    }

    public void gameOver(String nameWinner, int playerID) {
        //Scoreboard (Endscreen)
        for (int i = 0; i < 5; i++) {
            sbBG = new Rechteck(250, 250, 300, 300);
            sichtbarMachen(sbBG);
            sbBG.farbeSetzen(Color.GRAY);
            sbBG.sichtbarSetzen(false);
            scoreboard[i] = new Text(players.get(i).getName() + ": " + players.get(i).getPoints(), 260, 300 + i * 20, 15);
            sichtbarMachen(scoreboard[i]);
            scoreboard[i].sichtbarSetzen(false);
        }

        sbBG.sichtbarSetzen(true);
        int winningPoints = 0;
        for (Player p : players) {
            if (p.getId() == playerID) {
                winningPoints = p.getPoints();
            }
        }
        winner = new Text("Gewinner: " + nameWinner + ", Punkte: " + winningPoints, 260, 260, 30);
        for (int i = 0; i < players.size(); i++) {
            scoreboard[i].inhaltSetzen(players.get(i).getName() + ": " + players.get(i).getPoints());
            scoreboard[i].sichtbarSetzen(true);
        }
    }

    //ToDo @Tobi oje Linien, Format
    public void getEScoreboard() {
        //Runde
        eScoreboard[0][0] = new Text("Runde",98,100,20);
        for(int i = 1;i<21;i++) {
            eScoreboard[0][i] = new Text(""+i,98,100+i*30);
        }

        //for(int i = 1; i < 13;i = i+2){
          //  eScoreboard[i][0] = new Text("Stiche",120 + i*10,100,20);
        //}
        Text t = new Text("999",0,0,20);
        System.out.println(t.getBreite());
        System.out.println(t.getHoehe());

        for(int i = 1;i<13;i++) {
            if (i % 2 != 0) {
                int p = (i-1) / 2;
                if (players.size() > p) {
                    eScoreboard[i][0] = new Text(players.get(p).getName(), 130 + i * 10, 100, 20);
                    ArrayList<StitchHistory> sh = players.get(p).getSh();
                    for (int y = 1; y <= sh.size(); y++) {
                        if (sh.size() > y) {
                            StitchHistory s = sh.get(y - 1);
                            eScoreboard[i - 1][y] = new Text("" + s.getStitches(), 120 + i * 10, 100 + y * 5, 20);
                            eScoreboard[i - 1][y] = new Text("" + s.getPoints(), 130 + i * 10, 100 + y * 5, 20);
                        }
                    }
                }
            }
        }
    }

    //ToDo @Julian Button zum Anzeigen/Verbergen des Scoreboards, wenn nach Trumpffarbe gefragt wird auch automatisch ausblenden
    public void setSichtbardEScoreboard(boolean b) {
        for(Text[] lines:eScoreboard){
            for (Text t:lines){
                if(t != null) {
                    if(b) {
                        wurzel.add(t);
                    }
                    else {
                        wurzel.entfernen(t);
                    }
                    t.sichtbarSetzen(b);
                }
            }
        }
    }

    public void disconnected(String name) {
        Rechteck bg = new Rechteck(250, 250, 200, 40);
        sichtbarMachen(bg);
        Text t = new Text(name + " ist nicht mehr.", 260, 260, 20);
        sichtbarMachen(t);
    }

    //TODO: Unsichtbare Objekte können angeklickt werden, wie beheben?
    //ToDo: if Abfragen sind wahrscheinlich die beste Methode
    //hab des mal umgesetzt, außer code 3, kenne ich nicht

    @Override
    public void mausReagieren(int code) {

        //falls code einer Karte hier anstatt in dem switch immer abfragen
        if (code >= 100 && code <=119 ) {
            if (!ownHand[code-100].sichtbar()) {
                return;
            }
        }

        switch (code) {
            case 0:     //Beitreten-Button
                if (joinButton.sichtbar()) {
                    String ipAdress = getInputIP();
                    cClient = new CClient(ipAdress, this);
                    joinButton.sichtbarSetzen(false);
                    logo.sichtbarSetzen(false);
                }
                break;
            case 1:     //Zurück-Button
                if(backButton.sichtbar()) {
                    joinButton.sichtbarSetzen(true);
                    backButton.sichtbarSetzen(false);
                    logo.sichtbarSetzen(true);
                }
                break;
            case 2:     //Start-Button
                if(playButton.sichtbar()) {
                    joinButton.sichtbarSetzen(false);
                    playButton.sichtbarSetzen(false);
                    logo.sichtbarSetzen(false);
                    for (int i = 0; i < 5; i++) {
                        s[i].sichtbarSetzen(true);
                    }
                    for (int i = 0; i < 5; i++) {
                        names[i].sichtbarSetzen(true);
                    }
                    for (int i = 0; i < 5; i++) {
                        saidStitches[i].sichtbarSetzen(true);
                    }
                    for (int i = 0; i < 5; i++) {
                        points[i].sichtbarSetzen(true);
                    }
                    name.sichtbarSetzen(true);
                    ownAvatar.sichtbarSetzen(true);
                    ownSaidStitches.sichtbarSetzen(true);
                    ownPoints.sichtbarSetzen(true);
                    marker[0].sichtbarSetzen(true);
                    l.sichtbarSetzen(true);
                    showOwnHand();
                }
                break;
            case 3:     //Karten legen Button
                markPlayer(2);
                break;
            case 100:
                if (allowedCards[code - 100]) {
                    //ownHand[0].sichtbarSetzen(false);
                    wurzel.entfernen(ownHand[0]);
                    karteLegen(hand.get(0));
                    cClient.playCard(hand.get(0));
                }

                break;
            case 101:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[1].sichtbarSetzen(false);
                    karteLegen(hand.get(1));
                    cClient.playCard(hand.get(1));
                }
                break;
            case 102:
                if (allowedCards[code - 100] && inputAllowed) {
                    //ownHand[2].sichtbarSetzen(false);
                    wurzel.entfernen(ownHand[2]);
                    karteLegen(hand.get(2));
                    cClient.playCard(hand.get(2));
                }
                break;
            case 103:
                if (allowedCards[code - 100] && inputAllowed) {
                    //ownHand[3].sichtbarSetzen(false);
                    wurzel.entfernen(ownHand[3]);
                    karteLegen(hand.get(3));
                    cClient.playCard(hand.get(3));
                }
                break;
            case 104:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[4].sichtbarSetzen(false);
                    karteLegen(hand.get(4));
                    cClient.playCard(hand.get(4));
                }
                break;
            case 105:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[5].sichtbarSetzen(false);
                    karteLegen(hand.get(5));
                    cClient.playCard(hand.get(5));
                }
                break;
            case 106:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[6].sichtbarSetzen(false);
                    karteLegen(hand.get(6));
                    cClient.playCard(hand.get(6));
                }
                break;
            case 107:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[7].sichtbarSetzen(false);
                    karteLegen(hand.get(7));
                    cClient.playCard(hand.get(7));
                }
                break;
            case 108:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[8].sichtbarSetzen(false);
                    karteLegen(hand.get(8));
                    cClient.playCard(hand.get(8));
                }
                break;
            case 109:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[9].sichtbarSetzen(false);
                    karteLegen(hand.get(9));
                    cClient.playCard(hand.get(9));
                }
                break;
            case 110:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[10].sichtbarSetzen(false);
                    karteLegen(hand.get(10));
                    cClient.playCard(hand.get(10));
                }
                break;
            case 111:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[11].sichtbarSetzen(false);
                    karteLegen(hand.get(11));
                    cClient.playCard(hand.get(11));
                }
                break;
            case 112:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[12].sichtbarSetzen(false);
                    karteLegen(hand.get(12));
                    cClient.playCard(hand.get(12));
                }
                break;
            case 113:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[13].sichtbarSetzen(false);
                    karteLegen(hand.get(13));
                    cClient.playCard(hand.get(13));
                }
                break;
            case 114:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[14].sichtbarSetzen(false);
                    karteLegen(hand.get(14));
                    cClient.playCard(hand.get(14));
                }
                break;
            case 115:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[15].sichtbarSetzen(false);
                    karteLegen(hand.get(15));
                    cClient.playCard(hand.get(15));
                }
                break;
            case 116:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[16].sichtbarSetzen(false);
                    karteLegen(hand.get(16));
                    cClient.playCard(hand.get(16));
                }
                break;
            case 117:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[17].sichtbarSetzen(false);
                    karteLegen(hand.get(17));
                    cClient.playCard(hand.get(17));
                }
                break;
            case 118:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[18].sichtbarSetzen(false);
                    karteLegen(hand.get(18));
                    cClient.playCard(hand.get(18));
                }
                break;
            case 119:
                if (allowedCards[code - 100] && inputAllowed) {
                    ownHand[19].sichtbarSetzen(false);
                    karteLegen(hand.get(19));
                    cClient.playCard(hand.get(19));
                }
                break;
            default:
                break;
        }

    }
}
