import ea.*;

import java.util.ArrayList;
import java.util.Collections;

public class GUIClient extends Game implements MausReagierbar {
    //Grafische Elemente
    private Maus maus;
    private Bild join;      //Join-Button im Hauptmenü
    private Bild exit;      //Exit-Button im Hauptmenü
    private Bild bg;        //Haupthintergrundbild
    private Bild logo;      //Wizard-Logo im Hauptmenü
    private Text credits;   //Namen der Entwicker im Hauptmenü
    private Text version;   //Spielversion im Hauptmenü
    private Bild[] s = new Bild[5];     //Spieleravatare der anderen Spieler
    private Rechteck[] marker = new Rechteck[5];    //Marker der anderen Spieler
    private Text[] names = new Text[5];     //Spielernamen der anderen Spieler
    private Text[] saidStitches = new Text[5];      //Angesagte Stiche der anderen Spieler
    private Text[] madeStitches = new Text[5];      //Gemachte Stiche der anderen Spieler
    private Text[] points = new Text[5];        //Punkte der anderen Spieler
    private Bild[] stitchImage = new Bild[6];       //Aktueller Stich
    private Bild[] ownHand = new Bild[20];      //Eigene Hand
    private Text[] playerList = new Text[6];    //Spielerliste in der "Lobby"
    private Rechteck ownMarker;     //Eigener Spielermarker
    private Bild ownAvatar;     //Eigener Avatar
    private Text name;      //Eigener Name
    private Text ownSaidStitches;       //Eigene angesagte Stiche
    private Text ownMadeStitches;       //Eigene gemachte Stiche
    private Text ownPoints;     //Eigene Punkte
    private Text t;     //Aktuelle Trumpffarbe
    private Bild trumpCard;     //Aktuelle Trumpfkarte
    private Text cRound;        //Aktuelle Runde
    private Text stitchSum;     //Summe der (bereits) angesagten Stiche
    private Bild exit2;     //Exit-Button im Spiel
    private Rechteck lineSeparator;     //Trennlinie zwischen eigener Hand und aktuellem Stich
    private Rechteck sbBG;      //Hintergrund für das Scoreboard für den Game-Over-Screen
    private Text winner;        //Gewinner
    private Text[] scoreboard = new Text[6];    //Scoreboard für den Game-Over-Screen
    private Bild scoreboardButton;      //Scoreboard-Button
    private Bild bg2;       //Hintergrund für das Hauptscoreboard

        //Elemente des Hauptscoreboards
    private Text[][] eScoreboard = new Text[13][21];
    private Rechteck eScoreboard_line1;
    private Rechteck eScoreboard_line2;
    private Text lastStitch;

    private boolean inputAllowed = false;       //Legt fest, ob der Spieler eine Karte legen darf
    private boolean[] allowedCards = new boolean[20];       //Speichert, welche Karten aktuell gespielt werden dürfen
    private int relativeID[] = new int[6];      //Speichert die relativen IDs zur Zuordnung der Spieler im UI

    private CClient cClient;

    //Nur zur Datenhaltung
    public ArrayList<Player> players = new ArrayList<>(); //currentPoints, hand, clienthandler sind nicht ausgefüllt
    public ArrayList<Card> hand = new ArrayList<>();    //Karten auf der Hand
    public ArrayList<Card> stitch = new ArrayList<>(); //Karten, die aktuell auf dem Tisch liegen
    public ColorW currentTrump = null;      //aktuelle Trumpffarbe
    public Card currentTrumpCard = null;    //aktuelle Trumpfkarte
    public int currentRound = 0;       //aktuelle Runde
    public int currentPlayerID = 0; //ID des Spielers, der gerade an der Reihe ist
    public int idSelf = 0; //ID des Spielers, der man selbst ist
    public String lastStitchString = "";


    public GUIClient() {
        super(1300, 690, "Wizard", false, false);

        maus = new Maus(new Bild(0, 0, "Resources/pointer.png"), new Punkt(0, 0));
        mausAnmelden(maus);

        bg = new Bild(0, 0, "Resources/BG2.jpg");
        sichtbarMachen(bg);

        //Main Menu
        join = new Bild(300, 245, "Resources/joinButton.png");
        sichtbarMachen(join);
        maus.anmelden(this, join, 0);
        exit = new Bild(300, 395, "Resources/exitButton.png");
        sichtbarMachen(exit);
        maus.anmelden(this, exit, 2);
        logo = new Bild(700, 145, "Resources/wizardgame.png");
        sichtbarMachen(logo);
        credits = new Text("Coded by Tobias Eder & Julian Huber", 5, 670, "Segoe UI", 15);
        sichtbarMachen(credits);
        version = new Text("v1.4", 1265, 670, "Segoe UI", 15);
        sichtbarMachen(version);

        //Mitspieler-Liste
        for (int i = 0; i < 6; i++) {
            playerList[i] = new Text("empty", 700, 150 + i * 20, "Segoe UI" ,15);
            sichtbarMachen(playerList[i]);
            playerList[i].sichtbarSetzen(false);
        }

        //Trumpfanzeige
        t = new Text("Trumpf: ", 10, 460,"Segoe UI", 20);
        sichtbarMachen(t);
        t.sichtbarSetzen(false);
    }

    //Zeigt im Lobbyscreen einen neuen Spieler an
    public void showPlayerInList(String name, int id) {
        playerList[id].setzeInhalt(name);
        playerList[id].sichtbarSetzen(true);
    }

    public void resetPlayableCards()
    {
        for(int i = 0;  i<allowedCards.length; i++)
        {
            allowedCards[i] = true;
        }
    }

    //Legt fest, welche Karten in der eigenen Hand gespielt werden dürfen. Übernimmt dabei eine Liste aller erlaubten Karten
    public void setPlayableCards(ArrayList<Card> cards) {
        for (int i = 0; i < allowedCards.length; i++) {
            allowedCards[i] = false;
        }
        for (int i = 0; i < currentRound; i++) {
            for (Card c : cards) {
                if (c.isEqual(hand.get(i))) {
                    allowedCards[i] = true;
                }
            }
        }
    }

    //Erzeugt die Darstellung der eigenen Hand
    public void showOwnHand() {
        Collections.sort(hand);
        for (int i = 0; i < currentRound; i++) {
            if(i<10) {
                ownHand[i] = new Bild(200 + i * 95, 390, "Resources/" + hand.get(i).getValue() + "_in_" + hand.get(i).getColor() + ".png");
                sichtbarMachen(ownHand[i]);
                ownHand[i].sichtbarSetzen(true);
                maus.anmelden(this, ownHand[i], 100 + i);
            }
            else
            {
                ownHand[i] = new Bild(200 + (i-10) * 95, 540, "Resources/" + hand.get(i).getValue() + "_in_" + hand.get(i).getColor() + ".png");
                sichtbarMachen(ownHand[i]);
                ownHand[i].sichtbarSetzen(true);
                maus.anmelden(this, ownHand[i], 100 + i);
            }
        }
    }

    //Pop-Up-Dialog zur Abfrage der gewünschten Stiche
    public String askForStitches(int forbiddenNumber) {
        if (forbiddenNumber <= -1) {
            return eingabeFordern("Gewünschte Stichanzahl eingeben:");
        }
        return eingabeFordern("Gewünschte Stichanzahl eingeben. Nicht: " + forbiddenNumber + ": ");
    }

    //Interpretiert die Eingabe von vorgehender Methode
    public int validateStitches(int forbiddenNumber) {
        setSichtbarEScoreboard(false);
        String stitchesCount = askForStitches(forbiddenNumber);
        int sCount = 0;
        try {
            sCount = Integer.parseInt(stitchesCount);
        } catch (Exception e) {
            return validateStitches(forbiddenNumber);
        }

        if (sCount == forbiddenNumber || sCount < 0) {
            return validateStitches(forbiddenNumber);
        }
        return sCount;
    }

    //Pop-Up-Dialog zur Abfrage der gewünschten Trumpffarbe (falls Zauberer als Trumpfkarte aufgedeckt)
    public String askForTrumpColor() {
        return eingabeFordern("Gewünschte Trumpffarbe eingeben (Grün, Blau, Rot, Gelb)");
    }

    //Interpretiert die Eingabe von vorgehender Methode
    public ColorW validateTrump() {
        setSichtbarEScoreboard(false);
        String trumpColor = askForTrumpColor().toLowerCase();
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

    //Gibt den Spieler mit der übergebenen ID zurück
    public Player getPlayerByID(int id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    //Update-Methoden werden vom Server aufgerufen und aktualisieren die Spieloberfläche entsprechend
    public void updateTrump() {
        if(currentTrumpCard!=null && currentRound != (60/players.size())) {
            if(trumpCard!=null) {
                trumpCard.sichtbarSetzen(false);
                wurzel.entfernen(trumpCard);
                trumpCard = null;
            }
            trumpCard = new Bild(10, 300, "Resources/" + currentTrumpCard.getValue() + "_in_" + currentTrumpCard.getColor()+".png");
            sichtbarMachen(trumpCard);
            trumpCard.sichtbarSetzen(true);
        }
        if (currentRound == (60/players.size()))
        {
            trumpCard.sichtbarSetzen(false);
        }

    }

    public void updateNames() {
        for (Player p : players) {
            showPlayerInList(p.getName(), p.getId());
        }
    }

    public void updateSaidStitches() {
        for (Player p : players)
            if (p.getId() == idSelf) {
                if (p.getSaidStitches() == -1) {
                    ownSaidStitches.setzeInhalt("Angesagt: -");
                } else {
                    ownSaidStitches.setzeInhalt("Angesagt: " + p.getSaidStitches());
                }
            }
        for (int i = 0; i < players.size() - 1; i++) {
            for (Player p : players) {
                if (p.getId() == relativeID[i + 1]) {
                    if (p.getSaidStitches() == -1) {
                        saidStitches[i].setzeInhalt("Angesagt: -");
                    } else {
                        saidStitches[i].setzeInhalt("Angesagt: " + p.getSaidStitches());
                    }
                }
            }
        }
    }

    public void updateMadeStitches() {
        for (Player p : players)
            if (p.getId() == idSelf) {
                ownMadeStitches.setzeInhalt("Gemacht: " + p.getCurrentStitches());
            }
        for (int i = 0; i < players.size() - 1; i++) {
            for (Player p : players) {
                if (p.getId() == relativeID[i + 1]) {
                    madeStitches[i].setzeInhalt("Gemacht: " + p.getCurrentStitches());
                }
            }
        }
    }

    public void updateRoundCounter()
    {
        cRound.setzeInhalt("Runde: "+currentRound);
    }

    public void updateStitchSum(int sum)
    {
        if(sum!=-1) {
            stitchSum.setzeInhalt("Stiche: " + sum);
        }
        else
        {
            stitchSum.setzeInhalt("Stiche: -");
        }
    }

    public void updatePoints() {
        for (Player p : players) {
            if (p.getId() == idSelf) {
                ownPoints.setzeInhalt("Punkte: " + p.getLastPoints());
            }
        }
        for (int i = 0; i < players.size() - 1; i++) {
            for (Player p : players) {
                if (p.getId() == relativeID[i + 1]) {
                    points[i].setzeInhalt("Punkte: " + p.getLastPoints());
                }
            }
        }
    }

    public void updateStitch() {
        if (!stitch.isEmpty()) {
            for (int i = 0; i < stitch.size(); i++) {
                if (stitchImage[i] == null) {
                    karteLegen(stitch.get(i));
                }
            }
        } else {
            for (Bild b : stitchImage) {
                if (b != null) {
                    b.sichtbarSetzen(false);
                }
            }
            for (int i = 0; i < stitchImage.length; i++) {
                stitchImage[i] = null;
            }
        }
    }

    public void updateLastStitch(String s)
    {
        lastStitchString = s;
    }

    public void updateCurrentPlayerMarker() {
        ownMarker.sichtbarSetzen(false);
        for (int i = 0; i < players.size() - 1; i++) {
            marker[i].sichtbarSetzen(false);
        }
        if (currentPlayerID == idSelf) {
            ownMarker.sichtbarSetzen(true);
            for (int i = 0; i < players.size() - 1; i++) {
                marker[i].sichtbarSetzen(false);
            }
        } else {
            for (int i = 0; i < players.size() - 1; i++) {
                if (relativeID[i + 1] == currentPlayerID) {
                    marker[i].sichtbarSetzen(true);
                } else {
                    marker[i].sichtbarSetzen(false);
                }
            }
        }
    }

    //Setzt die Werte bei den Spielern zurück
    public void resetStats() {
        for (int i = 0; i < players.size() - 1; i++) {
            saidStitches[i].inhaltSetzen("Angesagt: -");
            madeStitches[i].inhaltSetzen("Gemacht: -");
        }
        ownSaidStitches.inhaltSetzen("Angesagt: -");
        ownMadeStitches.inhaltSetzen("Gemacht: -");
        if(trumpCard!=null) {
            trumpCard.sichtbarSetzen(false);
            wurzel.entfernen(trumpCard);
            trumpCard = null;
        }
    }

    //Setzt die Trumpffarbenanzeige zurück (wahrscheinlich unnötig, zu faul zum entfernen)
    public void resetTrump()
    {
        t.setzeInhalt("Trumpf:");
        if(trumpCard!=null) {
            trumpCard.sichtbarSetzen(false);
        }
    }

    public void resetLastStitch()
    {
        lastStitchString = "";
    }

    //Zeigt die richtige Trumpffarbe an, bei Zauberer die vom Spieler Gewählte
    public void setTrumpColor(ColorW c)
    {
        if(c!=null) {
            t.setzeInhalt("Trumpf: " + ColorW.toString(c));
        }
        else
        {
            t.setzeInhalt("Trumpf: -");
        }
    }

    //Setzt relative IDs, dass bei jedem individuellen Client die Position der Spieler im UI bestimmt werden kann
    public void setRelativeIDs() {
        relativeID[0] = idSelf;
        for (int i = 0; i < players.size() - 1; i++) {
            relativeID[i + 1] = (idSelf + 1 + i) % players.size();
        }
    }

    public void clearStitchImage() {
        for (int i = 0; i < 6; i++) {
            if (stitchImage[i] != null) {
                stitchImage[i].sichtbarSetzen(false);
                wurzel.entfernen(stitchImage[i]);
            }
            stitchImage[i] = null;
        }
    }

    @Override
    public void tasteReagieren(int i) {

    }

    //Markiert den Spieler, der gestochen hat durch ein grünes Rechteck hinter seinem Avatar
    public void stitchMarker(int id)
    {
        if(id == relativeID[0]) {
            ownMarker.farbeSetzen("Grün");
            ownMarker.sichtbarSetzen(true);
            warten(2200);
            ownMarker.sichtbarSetzen(false);
            ownMarker.farbeSetzen("Weiß");
        }
        for(int i = 0; i<players.size()-1; i++)
        {
            if(id == relativeID[i+1])
            {
                marker[i].farbeSetzen("Grün");
                marker[i].sichtbarSetzen(true);
                warten(2200);
                marker[i].sichtbarSetzen(false);
                marker[i].farbeSetzen("Weiß");
            }
        }
    }

    //Markiert Spieler entsprechend in grün bzw. rot, ob die Stichansage am Ende der Runde richtig war oder nicht
    public void markPlayers()
    {
        for(Player p:players)
        {
            if(p.getId()==idSelf)
            {
                if(p.getSaidStitches()==p.getCurrentStitches())
                {
                    ownMarker.farbeSetzen(new Farbe(0,200,0));
                    ownMarker.sichtbarSetzen(true);
                }
                else
                {
                    ownMarker.farbeSetzen("Rot");
                    ownMarker.sichtbarSetzen(true);
                }
            }
        }
        for(int i = 0; i<players.size()-1; i++)
        {
            for(Player p:players)
            {
                if(p.getId()==relativeID[i+1])
                {
                    if(p.getSaidStitches()==p.getCurrentStitches())
                    {
                        marker[i].farbeSetzen(new Farbe(0,200,0));
                        marker[i].sichtbarSetzen(true);
                    }
                    else
                    {
                        marker[i].farbeSetzen("Rot");
                        marker[i].sichtbarSetzen(true);
                    }
                }
            }
        }
        warten(2400);
        ownMarker.farbeSetzen("Weiß");
        ownMarker.sichtbarSetzen(false);
        for(int i = 0; i<players.size()-1; i++)
        {
            marker[i].farbeSetzen("Weiß");
            marker[i].sichtbarSetzen(false);
        }
    }

    //Meldet Objekte an der Wurzel an
    public void sichtbarMachen(Raum m) {
        wurzel.add(m);
    }

    public String getInputName() {
        String inName = eingabeFordern("Name eingeben:");
        if(inName.equals(""))
        {
            return eingabeFordern("Name eingeben:");
        }
        else return inName;
    }

    public String getInputIP() {
        String inIP = eingabeFordern("Server-IP-Adresse:");
        if(inIP.equals(""))
        {
            return eingabeFordern("Server-IP-Adresse:");
        }
        else
        {
            return inIP;
        }
    }

    public void karteLegen(Card c) {
        inputAllowed = false;
        for (int i = 0; i < 6; i++) {
            if (stitchImage[i] == null) {
                stitchImage[i] = new Bild(200 + i * 50, 200, "./Resources/" + c.getValue() + "_in_" + c.getColor() + ".png");
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

    //Wird vom Server aufgerufen, wenn der Spieler eine Karte spielen darf, hier wird die Eingabe freigegeben
    public void requestCard() {
        greyOutCards();
        inputAllowed = true;
    }

    //Graut nicht spielbare Karten aus
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
            sbBG.farbeSetzen("Dunkelgrau");
            sbBG.sichtbarSetzen(false);
            scoreboard[i] = new Text(players.get(i).getName() + ": " + players.get(i).getPoints(), 260, 300 + i * 20, "Segoe UI", 15);
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
        winner = new Text("Gewinner: " + nameWinner + ", Punkte: " + winningPoints, 260, 260, "Segoe UI", 30);
        for (int i = 0; i < players.size(); i++) {
            scoreboard[i].inhaltSetzen(players.get(i).getName() + ": " + players.get(i).getPoints());
            scoreboard[i].sichtbarSetzen(true);
        }
    }

    //Erzeugt ein neues Scoreboard
    public void getEScoreboard() {
        //Runde
        eScoreboard[0][0] = new Text("Runde", 100, 0, "Segoe UI", 20);
        for (int i = 1; i <= currentRound; i++) {
            eScoreboard[0][i] = new Text("" + i, 100, 20 + i * 30, "Segoe UI");
        }

        int xpos = 0;

        for (int i = 1; i < 13; i++) {
            if (i % 2 != 0) {
                int p = (i - 1) / 2;
                if (players.size() > p) {
                    if (i < 2) { //erster Playername, braucht nicht auf vorherigen Namen zu achten
                        eScoreboard[i][0] = new Text(players.get(p).getName(), 200, 0, "Segoe UI", 20);
                        ArrayList<StitchHistory> sh = players.get(p).getSh();
                        for (int y = 0; y < sh.size(); y++) {
                            StitchHistory s = sh.get(y);
                            eScoreboard[i][y + 1] = new Text("" + s.getStitches(), 200, 50 + y * 30, "Segoe UI", 20);
                            eScoreboard[i + 1][y + 1] = new Text("" + s.getPoints(), 250, 50 + y * 30, "Segoe UI", 20);
                        }
                    } else {
                        int breite = (int) (eScoreboard[i - 2][0].getBreite()) + 30;
                        if (breite < 150) {
                            breite = 150;
                        }

                        xpos = (int) (eScoreboard[i - 2][0].getX() + breite);

                        eScoreboard[i][0] = new Text(players.get(p).getName(), xpos, 0, "Segoe UI", 20);
                        ArrayList<StitchHistory> sh = players.get(p).getSh();
                        for (int y = 0; y < sh.size(); y++) {
                            StitchHistory s = sh.get(y);
                            eScoreboard[i][y + 1] = new Text("" + s.getStitches(), xpos, 50 + y * 30, "Segoe UI", 20);
                            eScoreboard[i + 1][y + 1] = new Text("" + s.getPoints(), xpos + 50, 50 + y * 30, "Segoe UI", 20);
                        }
                    }
                }
            }
        }

        //Linie:
        xpos = xpos + 30;
        int ypos = 50 + currentRound * 30;
        eScoreboard_line1 = new Rechteck(70, 40, xpos, 1);
        eScoreboard_line2 = new Rechteck(175, 0, 1, ypos);

        //Letzter stich
        lastStitch = new Text(lastStitchString, 30, 670, "Segoe UI", 15);
    }

    //Blendet das Scoreboard ein bzw. aus
    public void setSichtbarEScoreboard(boolean b) {
        if (b) {
            sichtbarMachen(bg2);
            bg2.sichtbarSetzen(true);
        } else {
            bg2.sichtbarSetzen(false);
            wurzel.entfernen(bg2);
        }
        sichtbarMachen(scoreboardButton);
        for (Text[] lines : eScoreboard) {
            for (Text t : lines) {
                if (t != null) {
                    if (b) {
                        wurzel.add(t);
                        t.sichtbarSetzen(true);
                    } else {
                        t.sichtbarSetzen(false);
                        wurzel.entfernen(t);
                    }
                }
            }
        }
        if(eScoreboard_line1 != null && eScoreboard_line2 != null) {
            if (b) {
                wurzel.add(eScoreboard_line1);
                wurzel.add(eScoreboard_line2);
                eScoreboard_line1.sichtbarSetzen(true);
                eScoreboard_line2.sichtbarSetzen(true);
        }
            else {
                eScoreboard_line1.sichtbarSetzen(false);
                eScoreboard_line2.sichtbarSetzen(false);
                wurzel.entfernen(eScoreboard_line1);
                wurzel.entfernen(eScoreboard_line2);
            }
        }
        if(lastStitch != null) {
            if (b) {
                sichtbarMachen(lastStitch);
                lastStitch.sichtbarSetzen(true);
            } else {
                lastStitch.sichtbarSetzen(false);
                wurzel.entfernen(lastStitch);
            }
        }
    }

    //Zeigt an, wenn ein Spieler disconnected
    public void disconnected(String name) {
        Bild bg3 = new Bild(0, 0, "Resources/BG.jpg");
        sichtbarMachen(bg3);
        Text dcMsg = new Text(name + " ist nicht mehr.", 260, 260, "Segoe UI", 20);
        sichtbarMachen(dcMsg);
    }

    //Wird vom Server aufgerufen, wenn auf Starte Spiel geklickt wurde; blendet die Ingame-Oberfläche ein
    public void startGame() {
        setRelativeIDs();
        for (Text t : playerList) {
            t.sichtbarSetzen(false);
            wurzel.entfernen(t);
        }

        //Marker erzeugen
        for (int i = 0; i < players.size() - 1; i++) {
            marker[i] = new Rechteck(100 + i * 200, 0, 80, 80);
            sichtbarMachen(marker[i]);
            marker[i].sichtbarSetzen(false);
        }
        ownMarker = new Rechteck(10, 520, 80, 80);
        sichtbarMachen(ownMarker);
        ownMarker.sichtbarSetzen(false);

        //Avatare erzeugen
        for (int i = 0; i < players.size() - 1; i++) {
            s[i] = new Bild(100 + i * 200, 0, "Resources/avatar2.png");
            sichtbarMachen(s[i]);
            s[i].sichtbarSetzen(false);
        }
        ownAvatar = new Bild(10, 520, "Resources/avatar2.png");
        sichtbarMachen(ownAvatar);
        ownAvatar.sichtbarSetzen(false);

        //Namen erzeugen
        for (int i = 0; i < players.size() - 1; i++) {
            names[i] = new Text(players.get(relativeID[i + 1]).getName(), 110 + i * 200, 90, "Segoe UI", 15);
            sichtbarMachen(names[i]);
            names[i].sichtbarSetzen(false);
        }
        name = new Text(players.get(idSelf).getName(), 20, 610, "Segoe UI", 15);
        sichtbarMachen(name);
        name.sichtbarSetzen(false);

        //Angesagte Stiche anzeigen
        for (int i = 0; i < players.size() - 1; i++) {
            saidStitches[i] = new Text("Angesagt: -", 110 + i * 200, 110, "Segoe UI", 15);
            sichtbarMachen(saidStitches[i]);
            saidStitches[i].sichtbarSetzen(false);
        }
        ownSaidStitches = new Text("Angesagt: -", 20, 630, "Segoe UI", 15);
        sichtbarMachen(ownSaidStitches);
        ownSaidStitches.sichtbarSetzen(false);

        //Gemachte Stiche anzeigen
        for (int i = 0; i < players.size() - 1; i++) {
            madeStitches[i] = new Text("Gemacht: -", 110 + i * 200, 130, "Segoe UI", 15);
            sichtbarMachen(madeStitches[i]);
            madeStitches[i].sichtbarSetzen(false);
        }
        ownMadeStitches = new Text("Gemacht: -", 20, 650, "Segoe UI", 15);
        sichtbarMachen(ownMadeStitches);
        ownMadeStitches.sichtbarSetzen(false);

        //Punkte anzeigen
        for (int i = 0; i < players.size() - 1; i++) {
            points[i] = new Text("Punkte: 0", 110 + i * 200, 150, "Segoe UI", 15);
            sichtbarMachen(points[i]);
            points[i].sichtbarSetzen(false);
        }
        ownPoints = new Text("Punkte: 0", 20, 670, "Segoe UI", 15);
        sichtbarMachen(ownPoints);
        ownPoints.sichtbarSetzen(false);

        //Trennlinie anzeigen
        lineSeparator = new Rechteck(150, 370, 1100, 1);
        sichtbarMachen(lineSeparator);

        //Aktuelle Runde anzeigen
        cRound = new Text("Runde: "+currentRound, 5, 5, "Segoe UI", 15);
        sichtbarMachen(cRound);

        //Stichsumme anzeigen
        stitchSum = new Text("Stiche: -", 5, 25, "Segoe UI", 15);
        sichtbarMachen(stitchSum);

        //Scoreboard-BG
        bg2 = new Bild(0, 0, "Resources/BG2.jpg");
        sichtbarMachen(bg2);
        bg2.sichtbarSetzen(false);

        //Scoreboard-Button
        scoreboardButton = new Bild(1075, 10, "Resources/sb.png");
        sichtbarMachen(scoreboardButton);
        scoreboardButton.sichtbarSetzen(false);
        maus.anmelden(this, scoreboardButton, 1);

        //Ingame-Exit Button
        exit2 = new Bild(1075, 80, "Resources/exitButton.png");
        sichtbarMachen(exit2);
        maus.anmelden(this, exit2, 3);

        join.sichtbarSetzen(false);
        logo.sichtbarSetzen(false);
        for (int i = 0; i < players.size() - 1; i++) {
            s[i].sichtbarSetzen(true);
        }
        for (int i = 0; i < players.size() - 1; i++) {
            names[i].sichtbarSetzen(true);
        }
        for (int i = 0; i < players.size() - 1; i++) {
            saidStitches[i].sichtbarSetzen(true);
        }
        for (int i = 0; i < players.size() - 1; i++) {
            madeStitches[i].sichtbarSetzen(true);
        }
        for (int i = 0; i < players.size() - 1; i++) {
            points[i].sichtbarSetzen(true);
        }
        name.sichtbarSetzen(true);
        ownAvatar.sichtbarSetzen(true);
        ownSaidStitches.sichtbarSetzen(true);
        ownMadeStitches.sichtbarSetzen(true);
        ownPoints.sichtbarSetzen(true);
        marker[0].sichtbarSetzen(true);
        t.sichtbarSetzen(true);
        showOwnHand();
        scoreboardButton.sichtbarSetzen(true);
    }

    @Override
    public void mausReagieren(int code) {
        //Verhindern, dass schon gespielte Karten nochmal gespielt werden können
        if (code >= 100 && code <= 119) {
            if (!ownHand[code - 100].sichtbar()) {
                return;
            }
        }
        System.out.println("Code: " + code);

        switch (code) {
            case 0:     //Beitreten-Button
                if (join.sichtbar()) {
                    String ipAdress = getInputIP();
                    cClient = new CClient(ipAdress, this);
                    join.sichtbarSetzen(false);
                    exit.sichtbarSetzen(false);
                    logo.sichtbarSetzen(false);
                    credits.sichtbarSetzen(false);
                    version.sichtbarSetzen(false);
                }
                break;
            case 1:     //Scoreboard-Button
                if (scoreboardButton.sichtbar()) {
                    if (bg2.sichtbar()) {
                        setSichtbarEScoreboard(false);
                    } else {
                        getEScoreboard();
                        setSichtbarEScoreboard(true);
                    }
                }
                break;
            case 2:     //Exit-Button im Hauptmenü
                if(exit.sichtbar())
                {
                    if(frage("Spiel wirklich beenden?"))
                    {
                        System.exit(0);
                    }
                }
                break;
            case 3:     //Exit-Button im Spiel
                if(exit2.sichtbar() && !bg2.sichtbar())
                {
                    if(frage("Spiel wirklich beenden?"))
                    {
                        System.exit(0);
                    }
                }
                break;
            case 100:   //100-119: Karten in der eigenen Hand
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[0].sichtbarSetzen(false);
                    karteLegen(hand.get(0));
                    cClient.playCard(hand.get(0));
                }

                break;
            case 101:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[1].sichtbarSetzen(false);
                    karteLegen(hand.get(1));
                    cClient.playCard(hand.get(1));
                }
                break;
            case 102:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[2].sichtbarSetzen(false);
                    karteLegen(hand.get(2));
                    cClient.playCard(hand.get(2));
                }
                break;
            case 103:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[3].sichtbarSetzen(false);
                    karteLegen(hand.get(3));
                    cClient.playCard(hand.get(3));
                }
                break;
            case 104:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[4].sichtbarSetzen(false);
                    karteLegen(hand.get(4));
                    cClient.playCard(hand.get(4));
                }
                break;
            case 105:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[5].sichtbarSetzen(false);
                    karteLegen(hand.get(5));
                    cClient.playCard(hand.get(5));
                }
                break;
            case 106:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[6].sichtbarSetzen(false);
                    karteLegen(hand.get(6));
                    cClient.playCard(hand.get(6));
                }
                break;
            case 107:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[7].sichtbarSetzen(false);
                    karteLegen(hand.get(7));
                    cClient.playCard(hand.get(7));
                }
                break;
            case 108:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[8].sichtbarSetzen(false);
                    karteLegen(hand.get(8));
                    cClient.playCard(hand.get(8));
                }
                break;
            case 109:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[9].sichtbarSetzen(false);
                    karteLegen(hand.get(9));
                    cClient.playCard(hand.get(9));
                }
                break;
            case 110:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[10].sichtbarSetzen(false);
                    karteLegen(hand.get(10));
                    cClient.playCard(hand.get(10));
                }
                break;
            case 111:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[11].sichtbarSetzen(false);
                    karteLegen(hand.get(11));
                    cClient.playCard(hand.get(11));
                }
                break;
            case 112:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[12].sichtbarSetzen(false);
                    karteLegen(hand.get(12));
                    cClient.playCard(hand.get(12));
                }
                break;
            case 113:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[13].sichtbarSetzen(false);
                    karteLegen(hand.get(13));
                    cClient.playCard(hand.get(13));
                }
                break;
            case 114:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[14].sichtbarSetzen(false);
                    karteLegen(hand.get(14));
                    cClient.playCard(hand.get(14));
                }
                break;
            case 115:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[15].sichtbarSetzen(false);
                    karteLegen(hand.get(15));
                    cClient.playCard(hand.get(15));
                }
                break;
            case 116:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[16].sichtbarSetzen(false);
                    karteLegen(hand.get(16));
                    cClient.playCard(hand.get(16));
                }
                break;
            case 117:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[17].sichtbarSetzen(false);
                    karteLegen(hand.get(17));
                    cClient.playCard(hand.get(17));
                }
                break;
            case 118:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
                    ownHand[18].sichtbarSetzen(false);
                    karteLegen(hand.get(18));
                    cClient.playCard(hand.get(18));
                }
                break;
            case 119:
                if (allowedCards[code - 100] && inputAllowed && !bg2.sichtbar()) {
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
