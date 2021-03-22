import ea.*;

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

    private boolean inputAllowed = false;

    private CClient cClient;

    //Nur zur Datenhaltung
    public ArrayList<Player> players = new ArrayList<>(); //currentPoints, hand, clienthandler sind nicht ausgefüllt
    public ArrayList<Card> hand = new ArrayList<>();    //Karten auf der Hand
    public ArrayList<Card> stitch = new ArrayList<>(); //Karten, die aktuell auf dem Tisch liegen
    public ColorW currentTrump = null;      //aktuelle Trumpffarbe
    public int currentRound = 0;       //aktuelle Runde
    public int currentPlayerID = 0; //ID des Spielers, der gerade an der Reihe ist
    public int idSelf = 0; //ID des Spielers, der man selbst ist


    public GUIClient()
    {
        super(1200,650, "Wizard");

        maus = new Maus(new Bild(0,0, "Resources/pointer.png"), new Punkt(0, 0));
        mausAnmelden(maus);

        bg = new Bild(0,0,"Resources/BG2.jpg");
        sichtbarMachen(bg);

        //Test
        hand.add(new Card(3, ColorW.YELLOW));
        hand.add(new Card(2, ColorW.YELLOW));
        hand.add(new Card(1, ColorW.YELLOW));
        hand.add(new Card(0, ColorW.BLUE));

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
        for(int i = 0; i<6; i++)
        {
            playerList[i] = new Text("empty", 700, 150+i*20, 15);
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
        for(int i = 0; i<5; i++)
        {
            marker[i] = new Rechteck(100+i*200, 20, 150, 150);
            sichtbarMachen(marker[i]);
            marker[i].sichtbarSetzen(false);
        }
        ownMarker = new Rechteck(10, 430, 150, 150);
        sichtbarMachen(ownMarker);
        ownMarker.sichtbarSetzen(false);
        //Avatare erzeugen
        for(int i = 0; i<5; i++)
        {
            s[i] = new Bild(100+i*200, 20, "Resources/avatar.png");
            sichtbarMachen(s[i]);
            s[i].sichtbarSetzen(false);
        }
        ownAvatar = new Bild(10, 430, "Resources/avatar.png");
        sichtbarMachen(ownAvatar);
        ownAvatar.sichtbarSetzen(false);

        //Namen erzeugen
        for(int i = 0; i<5; i++)
        {
            names[i] = new Text("Spieler"+(i+2), 110+i*200, 180, 15);
            sichtbarMachen(names[i]);
            names[i].sichtbarSetzen(false);
        }
        name = new Text("Spieler1", 20, 590, 15);
        sichtbarMachen(name);
        name.sichtbarSetzen(true);

        //Angesagte Stiche anzeigen
        for(int i = 0 ; i<5; i++)
        {
            saidStitches[i] = new Text("Angesagt: ", 110+i*200, 200, 15);
            sichtbarMachen(saidStitches[i]);
            saidStitches[i].sichtbarSetzen(false);
        }
        ownSaidStitches = new Text("Angesagt: ", 20, 610, 15);
        sichtbarMachen(ownSaidStitches);
        ownSaidStitches.sichtbarSetzen(false);

        //Punkte anzeigen
        for(int i = 0; i<5; i++)
        {
            points[i] = new Text("Punkte: ", 110+i*200, 220, 15);
            sichtbarMachen(points[i]);
            points[i].sichtbarSetzen(false);
        }
        ownPoints = new Text("Punkte: ", 20, 630, 15);
        sichtbarMachen(ownPoints);
        ownPoints.sichtbarSetzen(false);
    }

    public void showPlayerInList(String name, int id)
    {
        playerList[id].setzeInhalt(name);
        playerList[id].sichtbarSetzen(true);
    }

    public void showOwnHand()
    {
        Collections.sort(hand);
        for(int i = 0; i<4; i++)
        {
            ownHand[i] = new Bild(200+i*95, 500, "Resources/"+hand.get(i).getValue()+"_in_"+hand.get(i).getColor()+".png");
            sichtbarMachen(ownHand[i]);
            ownHand[i].sichtbarSetzen(true);
            maus.anmelden(this, ownHand[i], 100+i);
        }
    }

    public String askForTrumpColor()
    {
        return eingabeFordern("Gewünschte Trumpffarbe eingeben (grün, blau, rot, gelb)");
    }

    public ColorW validateTrump()
    {
        String trumpColor = askForTrumpColor();
        if(trumpColor.equals("grün"))
        {
            return ColorW.GREEN;
        }
        if(trumpColor.equals("blau"))
        {
            return ColorW.BLUE;
        }
        if(trumpColor.equals("rot"))
        {
            return ColorW.RED;
        }
        if(trumpColor.equals("gelb"))
        {
            return ColorW.YELLOW;
        }
        return validateTrump();
    }

    public Player getPlayerByID(int id)
    {
        for(Player p:players)
        {
            if(p.getId()==id)
            {
                return p;
            }
        }
        return null;
    }

    @Override
    public void tasteReagieren(int i) {

    }

    public void sichtbarMachen(Raum m)
    {
        wurzel.add(m);
    }

    public String getInputName()
    {
        return eingabeFordern("Name eingeben:");
    }

    public String getInputIP()
    {
        return eingabeFordern("Server-IP-Adresse:");
    }

    public void karteLegen(Card c)
    {
        for(int i = 0; i<6; i++)
        {
            if(stitchImage[i]==null)
            {
                stitchImage[i] = new Bild(200+i*50, 300, "./Resources/"+c.getValue()+"_in_"+c.getColor()+".png");
                sichtbarMachen(stitchImage[i]);
                break;
            }
        }
    }

    public void clearOnwHand()
    {
        for(int i = 0; i<currentRound; i++)
        {
            wurzel.entfernen(ownHand[i]);
        }
    }

    public void clearCurrentStitch()
    {
        for(int i = 0; i<players.size(); i++)
        {
            wurzel.entfernen(stitchImage[i]);
        }
    }

    public void markPlayer(int p)
    {
        for(int i = 0; i<5; i++)
        {
            marker[i].sichtbarSetzen(false);
        }
        ownMarker.sichtbarSetzen(false);
        if(p!=0) {
            marker[p - 1].sichtbarSetzen(true);
        }
        else
        {
            ownMarker.sichtbarSetzen(true);
        }
    }

    public void requestCard() {
        //ToDo @Julian bitte implementieren -> soll die Kartenauswahl des Spielers zurückgeben

        inputAllowed = true;
    }

    public void gameOver(String nameWinner, int playerID) {
        //ToDo @Julian Gewinner anzeigen, scoreboard anzeigen, andere interessante Informationen

    }

    public void disconnected(String name) {
        //ToDo @Julian Fehlermeldung anzeigen, dass ein Spieler disconnected ist

    }

    @Override
    public void mausReagieren(int code)
    {
        if(inputAllowed) {
            switch (code) {
                case 0:     //Beitreten-Button
                    String ipAdress = getInputIP();
                    cClient = new CClient(ipAdress, this);
                    joinButton.sichtbarSetzen(false);
                    logo.sichtbarSetzen(false);
                    break;
                case 1:     //Zurück-Button
                    joinButton.sichtbarSetzen(true);
                    backButton.sichtbarSetzen(false);
                    logo.sichtbarSetzen(true);
                    break;
                case 2:     //Start-Button
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
                    break;
                case 3:     //Karten legen Button
                    markPlayer(2);
                    break;
                case 100:
                    ownHand[0].sichtbarSetzen(false);
                    karteLegen(hand.get(0));
                    cClient.playCard(hand.get(0));
                    break;
                case 101:
                    ownHand[1].sichtbarSetzen(false);
                    karteLegen(hand.get(1));
                    cClient.playCard(hand.get(1));
                    break;
                case 102:
                    ownHand[2].sichtbarSetzen(false);
                    karteLegen(hand.get(2));
                    cClient.playCard(hand.get(2));
                    break;
                case 103:
                    ownHand[3].sichtbarSetzen(false);
                    karteLegen(hand.get(3));
                    cClient.playCard(hand.get(3));
                    break;
                case 104:
                    ownHand[4].sichtbarSetzen(false);
                    karteLegen(hand.get(4));
                    cClient.playCard(hand.get(4));
                    break;
                case 105:
                    ownHand[5].sichtbarSetzen(false);
                    karteLegen(hand.get(5));
                    cClient.playCard(hand.get(5));
                    break;
                case 106:
                    ownHand[6].sichtbarSetzen(false);
                    karteLegen(hand.get(6));
                    cClient.playCard(hand.get(6));
                    break;
                case 107:
                    ownHand[7].sichtbarSetzen(false);
                    karteLegen(hand.get(7));
                    cClient.playCard(hand.get(7));
                    break;
                case 108:
                    ownHand[8].sichtbarSetzen(false);
                    karteLegen(hand.get(8));
                    cClient.playCard(hand.get(8));
                    break;
                case 109:
                    ownHand[9].sichtbarSetzen(false);
                    karteLegen(hand.get(9));
                    cClient.playCard(hand.get(9));
                    break;
                case 110:
                    ownHand[10].sichtbarSetzen(false);
                    karteLegen(hand.get(10));
                    cClient.playCard(hand.get(10));
                    break;
                case 111:
                    ownHand[11].sichtbarSetzen(false);
                    karteLegen(hand.get(11));
                    cClient.playCard(hand.get(11));
                    break;
                case 112:
                    ownHand[12].sichtbarSetzen(false);
                    karteLegen(hand.get(12));
                    cClient.playCard(hand.get(12));
                    break;
                case 113:
                    ownHand[13].sichtbarSetzen(false);
                    karteLegen(hand.get(13));
                    cClient.playCard(hand.get(13));
                    break;
                case 114:
                    ownHand[14].sichtbarSetzen(false);
                    karteLegen(hand.get(14));
                    cClient.playCard(hand.get(14));
                    break;
                case 115:
                    ownHand[15].sichtbarSetzen(false);
                    karteLegen(hand.get(15));
                    cClient.playCard(hand.get(15));
                    break;
                case 116:
                    ownHand[16].sichtbarSetzen(false);
                    karteLegen(hand.get(16));
                    cClient.playCard(hand.get(16));
                    break;
                case 117:
                    ownHand[17].sichtbarSetzen(false);
                    karteLegen(hand.get(17));
                    cClient.playCard(hand.get(17));
                    break;
                case 118:
                    ownHand[18].sichtbarSetzen(false);
                    karteLegen(hand.get(18));
                    cClient.playCard(hand.get(18));
                    break;
                case 119:
                    ownHand[19].sichtbarSetzen(false);
                    karteLegen(hand.get(19));
                    cClient.playCard(hand.get(19));
                    break;
                default:
                    break;
            }
        }
    }
}
