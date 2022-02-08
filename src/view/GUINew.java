package view;

import ea.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import model.*;

public class GUINew extends JFrame {
    //Grafische Elemente
    //private Bild[] s = new Bild[5];     //Spieleravatare der anderen Spieler
    private OtherPlayersView opw;
    private Rechteck[] marker = new Rechteck[5];    //Marker der anderen Spieler
    //private Text[] names = new Text[5];     //Spielernamen der anderen Spieler
    private JLabel[] names = new JLabel[5];
    //private Text[] saidStitches = new Text[5];      //Angesagte Stiche der anderen Spieler
    private JLabel[] saidStitches = new JLabel[5];
    //private Text[] madeStitches = new Text[5];      //Gemachte Stiche der anderen Spieler
    private JLabel[] madeStitches = new JLabel[5];
    //private Text[] points = new Text[5];        //Punkte der anderen Spieler
    private JLabel[] points = new JLabel[5];
    //private Bild[] stitchImage = new Bild[6];       //Aktueller Stich
    private CardView[] stitchImage = new CardView[6];
    //private Bild[] ownHand = new Bild[20];      //Eigene Hand
    private CardView[] ownHand = new CardView[20];
    //private Text[] playerList = new Text[6];    //Spielerliste in der "Lobby"
    private JLabel[] playerList = new JLabel[6];
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

    private JLabel title = new JLabel("WIZARD");
    private ButtonBar mainButtons;

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


    public GUINew() {
        setLayout(new BorderLayout());
        setBackground(new Color(57, 62, 195));

        Player[] ps = new Player[3];

        ps[0] = new Player("Julian", 0);
        ps[1] = new Player("Julian2", 1);
        ps[2] = new Player("Julian3", 2);
        opw = new OtherPlayersView(ps);
        add(opw, BorderLayout.NORTH);

        mainButtons = new ButtonBar();

        add(mainButtons, BorderLayout.CENTER);
    }
}
