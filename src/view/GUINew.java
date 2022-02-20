package view;

import ea.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

import model.*;
import model.Sound;

public class GUINew extends JFrame {
    //Grafische Elemente
    //private Bild[] s = new Bild[5];     //Spieleravatare der anderen Spieler
    private OtherPlayersView opw;
    private PlayerView selfView;
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
    private StitchView stitchImage = new StitchView();
    //private Bild[] ownHand = new Bild[20];      //Eigene Hand
    private OwnCardsView ownHand;
    //private Text[] playerList = new Text[6];    //Spielerliste in der "Lobby"
    private JLabel[] playerList = new JLabel[6];
    private LobbyView lobby = new LobbyView();
    private Rechteck ownMarker;     //Eigener Spielermarker
    private Bild ownAvatar;     //Eigener Avatar
    private Text name;      //Eigener Name
    private Text ownSaidStitches;       //Eigene angesagte Stiche
    private Text ownMadeStitches;       //Eigene gemachte Stiche
    private Text ownPoints;     //Eigene Punkte
    private Text t;     //Aktuelle Trumpffarbe
    private Bild trumpCard;     //Aktuelle Trumpfkarte
    private TrumpView trump = new TrumpView();
    //private Text cRound;        //Aktuelle Runde
    private JLabel cRound = new JLabel();
    //private Text stitchSum;     //Summe der (bereits) angesagten Stiche
    private JLabel stitchSum = new JLabel();
    private Bild exit2;     //Exit-Button im Spiel
    private Rechteck lineSeparator;     //Trennlinie zwischen eigener Hand und aktuellem Stich
    private Rechteck sbBG;      //Hintergrund für das Scoreboard für den Game-Over-Screen
    private Text winner;        //Gewinner
    private Text[] scoreboard = new Text[6];    //Scoreboard für den Game-Over-Screen
    private Bild scoreboardButton;      //Scoreboard-Button
    private Bild bg2;       //Hintergrund für das Hauptscoreboard

    private JLabel title;
    ImageIcon cover = new ImageIcon("./Resources/wizardgame.png");
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

    private ArrayList<String> chat = new ArrayList<>();
    private ChatWindow cw;
    private JButton openChat = new JButton("Chat");
    private boolean isOpened = false;
    private JCheckBox mute = new JCheckBox();


    public GUINew() {
        setLayout(new BorderLayout());
        //setBackground(new Color(57, 62, 195));

        title = new JLabel(cover);
        mainButtons = new ButtonBar(this);

        add(title, BorderLayout.NORTH);
        add(mainButtons, BorderLayout.CENTER);
    }

    public void setCClient(String ipAddress) {
        cClient = new CClient(ipAddress,this);
        if (cClient.verbindungGescheitert()) {
            Utility.showInfoDialog("Verbindung zu der eingegebenen IP-Adresse fehlgeschlagen!");
        }
        else {
            joinGame();
        }
    }

    public CClient getCClient() {
        return cClient;
    }

    public boolean getInputAllowed() {
        return inputAllowed;
    }

    private void joinGame() {
        title.setVisible(false);
        mainButtons.setVisible(false);
        add(lobby, BorderLayout.CENTER);
    }

    public void resetPlayableCards() {
        ArrayList<CardPanel> hand = ownHand.getOwnHand();
        for (CardPanel cv : hand) {
            cv.setPlayable(false);
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

    //Pop-Up-Dialog zur Abfrage der gewünschten Stiche
    private String askForStitches(int forbidden) {
        if (forbidden <= -1) {
            return Utility.askInput("Gewünschte Stichzahl eingeben.");
        }
        return Utility.askInput("Gewünschte Stichzahl eingeben (nicht " + forbidden + "):");
    }

    public void requestCard() {
        inputAllowed = true;
        updatePlayableCards();
    }

    private void updatePlayableCards() {
        ownHand.setPlayableCards(allowedCards);
    }

    //Interpretiert die Eingabe von vorgehender Methode
    public int validateStitches(int forbiddenNumber) {
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
    private String askForTrumpColor() {
        return Utility.askInput("Gewünschte Trumpffarbe eingeben (Grün, Blau, Rot, Gelb):");
    }

    //Interpretiert die Eingabe von vorgehender Methode
    public ColorW validateTrump() {
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

    //Setzt relative IDs, dass bei jedem individuellen Client die Position der Spieler im UI bestimmt werden kann
    private void setRelativeIDs() {
        relativeID[0] = idSelf;
        for (int i = 0; i < players.size() - 1; i++) {
            relativeID[i + 1] = (idSelf + 1 + i) % players.size();
        }
    }

    public void disconnected(String name) {
        Utility.showInfoDialog(name + " hat das Spiel verlassen!");
    }

    public void startGame() {
        lobby.setVisible(false);
        setRelativeIDs();
        opw = new OtherPlayersView(createSortedPlayerView());
        selfView = new PlayerView(players.get(relativeID[0]));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new VerticalFlowLayout());
        add(leftPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new VerticalFlowLayout(FlowLayout.CENTER, FlowLayout.CENTER));
        add(centerPanel, BorderLayout.CENTER);

        add(opw, BorderLayout.NORTH);
        leftPanel.add(trump);
        leftPanel.add(selfView);
        centerPanel.add(stitchImage);
        //centerPanel.add(new SeparatorLine());
        Collections.sort(hand);
        ownHand = new OwnCardsView(createCardView(), this);
        centerPanel.add(ownHand);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new VerticalFlowLayout());
        add(rightPanel, BorderLayout.EAST);

        openChat.setFocusable(false);

        openChat.addActionListener(e -> {
            if (!isOpened) {
                isOpened = true;
                openChat.setIcon(null);
                javax.swing.SwingUtilities.invokeLater(() -> createAndShowChat());
            }
        });

        mute.setIcon(Utility.resizeIcon(new ImageIcon("./Resources/speaker.png"), 35, 35));
        mute.setSelectedIcon(Utility.resizeIcon(new ImageIcon("./Resources/speakerMuted.png"), 35, 35));

        rightPanel.add(mute);
        rightPanel.add(openChat);
        rightPanel.add(cRound);
        rightPanel.add(stitchSum);
    }

    private void createAndShowChat() {
        cw = new ChatWindow(this);
        cw.setSize(new Dimension(450, 280));
        cw.setMinimumSize(new Dimension(500, 350));
        cw.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ImageIcon icon = new ImageIcon("./Resources/icon.png");
        cw.setIconImage(icon.getImage());
        cw.setTitle("Wizard-Chat");
        cw.setVisible(true);
        cw.updateChat(chat);

        cw.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                resetChatWindow();
                isOpened = false;
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    private ArrayList<PlayerView> createSortedPlayerView() {
        ArrayList<PlayerView> playerView = new ArrayList<>();
        for (int i = 0; i < players.size() - 1; i++) {
            playerView.add(new PlayerView(players.get(relativeID[i + 1])));
        }
        return playerView;
    }

    private ArrayList<CardPanel> createCardView() {
        ArrayList<CardPanel> cards = new ArrayList<>();
        for (Card c : hand) {
            cards.add(new CardPanel(c));
        }
        return cards;
    }

    private ArrayList<CardView> createStitchView() {
        ArrayList<CardView> cards = new ArrayList<>();
        for (Card c : stitch) {
            cards.add(new CardView(c));
        }
        return cards;
    }

    public void showOwnHand() {
        Collections.sort(hand);
        ownHand.initHand(createCardView());
    }

    public void layCard() {
        inputAllowed = false;

    }

    public void addPlayer(Player p) {
        players.add(p);
        updateNames();
    }

    public void updateNames() {
        for (int i = 0; i < players.size(); i++) {
            lobby.addPlayerName(players.get(i), i);
        }
    }

    public void updateTrump() {
        if (currentTrumpCard != null && currentRound != (60 / players.size())) {
            trump.setTrumpCard(currentTrumpCard);
        }
    }

    public void setTrumpColor(ColorW c) {
        trump.setTrumpColor(c);
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stitchMarker(int id) {
        if (id == relativeID[0]) {
            selfView.setImage(MarkerColor.GREEN);
            sleep(2200);
            selfView.setImage(MarkerColor.NONE);
        } else {
            for (int i = 0; i < players.size() - 1; i++) {
                if (id == relativeID[i + 1]) {
                    opw.getPlayerView(i).setImage(MarkerColor.GREEN);
                    sleep(2200);
                    opw.getPlayerView(i).setImage(MarkerColor.NONE);
                    break;
                }
            }
        }
    }

    public void markPlayers() {
        for (Player p : players) {
            if (p.getId() == idSelf) {
                if (p.getSaidStitches() == p.getCurrentStitches()) {
                    selfView.setImage(MarkerColor.GREEN);
                } else {
                    selfView.setImage(MarkerColor.RED);
                }
            }
        }
        for (int i = 0; i < players.size() - 1; i++) {
            for (Player p : players) {
                if (p.getId() == relativeID[i + 1]) {
                    if (p.getSaidStitches() == p.getCurrentStitches()) {
                        opw.getPlayerView(i).setImage(MarkerColor.GREEN);
                    } else {
                        opw.getPlayerView(i).setImage(MarkerColor.RED);
                    }
                }
            }
        }
        sleep(2400);
        selfView.setImage(MarkerColor.NONE);
        for (int i = 0; i < players.size() - 1; i++) {
            opw.getPlayerView(i).setImage(MarkerColor.NONE);
        }
    }

    public void updateRoundCounter() {
        cRound.setText("Runde: " + currentRound);
    }

    public void updateStitchSum(int sum) {
        if (sum != -1) {
            stitchSum.setText("Stiche: " + sum);
        } else {
            stitchSum.setText("Stiche: -");
        }
    }

    public void updatePoints() {
        for (Player p : players) {
            if (p.getId() == idSelf) {
                selfView.updatePoints();
            }
        }
        for (int i = 0; i < players.size() - 1; i++) {
            for (Player p : players) {
                if (p.getId() == relativeID[i + 1]) {
                    opw.getPlayerView(i).updatePoints();
                }
            }
        }
    }

    public void updateMadeStitches() {
        for (Player p : players)
            if (p.getId() == idSelf) {
                selfView.updateMadeStitches(p.getCurrentStitches());
            }
        for (int i = 0; i < players.size() - 1; i++) {
            for (Player p : players) {
                if (p.getId() == relativeID[i + 1]) {
                    opw.getPlayerView(i).updateMadeStitches(p.getCurrentStitches());
                }
            }
        }
    }

    public void updateSaidStitches() {
        for (Player p : players)
            if (p.getId() == idSelf) {
                selfView.updateSaidStitches(p.getSaidStitches());
            }
        for (int i = 0; i < players.size() - 1; i++) {
            for (Player p : players) {
                if (p.getId() == relativeID[i + 1]) {
                    opw.getPlayerView(i).updateSaidStitches(p.getSaidStitches());
                }
            }
        }
    }

    public void updateStitch() {
        for (int i = 0; i < 6; i++) {
            stitchImage.getStitch().get(i).setCard(null);
        }
        for (int i = 0; i < stitch.size(); i++) {
            stitchImage.getStitch().get(i).setCard(stitch.get(i));
        }

    }

    public void updateCurrentPlayerMarker() {
        selfView.setOnTurn(false);
        for (int i = 0; i < players.size() - 1; i++) {
            opw.getPlayerView(i).setOnTurn(false);
        }
        if (currentPlayerID == idSelf) {
            selfView.setOnTurn(true);
        } else {
            for (int i = 0; i < players.size() - 1; i++) {
                if (relativeID[i + 1] == currentPlayerID) {
                    opw.getPlayerView(i).setOnTurn(true);
                } else {
                    opw.getPlayerView(i).setOnTurn(false);
                }
            }
        }
    }

    public void resetStats() {
        selfView.resetStats();
        for (int i = 0; i < players.size() - 1; i++) {
            opw.getPlayerView(i).resetStats();
        }
        trump.setTrumpCard(null);
    }

    public void resetTrump() {
        trump.setTrumpColor(null);
        trump.setTrumpCard(null);
    }

    public void gameOver(String nameWinner, int playerID) {
        int winningPoints = 0;
        for (Player p : players) {
            if (p.getId() == playerID) {
                winningPoints = p.getPoints();
            }
        }
        Utility.showInfoDialog(nameWinner + " hat mit " + winningPoints + "Punkten gewonnen.");
    }

    public String getInputName() {
        return Utility.askInput("Name eingeben:");
    }

    public void shutdown() {
        if (cClient != null) {
            cClient.verbindungSchliessen();
        }
    }

    public void addMessage(String s) {
        if (chat.size() > 9) {
            chat.remove(0);
        }
        chat.add(s);
        if (cw != null) {
            cw.updateChat(chat);
        } else {
            openChat.setIcon(new ImageIcon("./Resources/dot.png"));
            if (!mute.isSelected()) {
                Utility.playPingSound();
            }
        }
    }

    public void resetChatWindow() {
        cw = null;
    }

    public String getDisplayName() {
        return players.get(relativeID[0]).getName();
    }

    public void playSound(String filename) {
        if(!mute.isSelected()) {
            Utility.playSound(filename);
        }
    }
}
