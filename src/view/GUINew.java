package view;

import com.sun.tools.javac.Main;
import ea.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

import model.*;

public class GUINew extends JFrame {
    public static final String VERSION = "2.0.0-alpha";
    public static final String DOWNLOAD_URL = "https://github.com/julihuber0/Wizard/releases";

    //Grafische Elemente

    private OtherPlayersView opw;
    private PlayerView selfView;
    private StitchView stitchImage = new StitchView();
    private OwnCardsView ownHand;
    private LobbyView lobby = new LobbyView();
    private TrumpView trump = new TrumpView();
    private JLabel cRound = new JLabel();
    private JLabel stitchSum = new JLabel();

    private JLabel title;
    private ImageIcon cover = new ImageIcon("./Resources/wizardgame.png");
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
    private double initScale = 1;
    private JLabel credits = new JLabel("by Tobias Eder & Julian Huber");
    private JLabel space = new JLabel("   -   ");
    private JLabel version = new JLabel(VERSION);
    private JPanel bottom = new JPanel();
    private JPanel lobbyPanel = new JPanel();

    /**
     * Creates a new gui and sets up the main menu of the game.
     */
    public GUINew() {
        setLayout(new BorderLayout());

        title = new JLabel(cover);
        mainButtons = new ButtonBar(this);

        add(title, BorderLayout.NORTH);
        add(mainButtons, BorderLayout.CENTER);

        bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(credits);
        bottom.add(space);
        bottom.add(version);

        add(bottom, BorderLayout.SOUTH);

        credits.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Desktop d = Desktop.getDesktop();
                try {
                    d.browse(new URI("https://youtu.be/dQw4w9WgXcQ"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                credits.setForeground(new Color(0, 73, 218));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                credits.setForeground(Color.BLACK);
            }
        });
    }

    /**
     * Sets the initial scale of the cards in the player's hand.
     *
     * @param scale The initial scale factor.
     */
    public void setInitScale(double scale) {
        initScale = scale;
    }

    /**
     * Returns the initial card scale.
     *
     * @return the initial card scale.
     */
    public double getInitScale() {
        return initScale;
    }

    /**
     * Returns whether the mute button is selected or not.
     *
     * @return whether the mute button is selected or not.
     */
    public boolean getMuted() {
        return mute.isSelected();
    }

    /**
     * Sets up a new CClient with a given ip address, opens an information popup
     * if the ip address was wrong and no server could be found.
     *
     * @param ipAddress The ip address the CClient connects to.
     */
    public void setCClient(String ipAddress) {
        cClient = new CClient(ipAddress,this);
        if (cClient.verbindungGescheitert()) {
            Utility.showInfoDialog("Verbindung zu der eingegebenen IP-Adresse fehlgeschlagen!");
        }
        else {
            joinGame();
        }
    }

    /**
     * Returns the client's CClient.
     *
     * @return the client's CClient.
     */
    public CClient getCClient() {
        return cClient;
    }

    /**
     * Returns whether the player of this client is allowed to do an input. Is
     * obviously only true if it's the player's turn.
     *
     * @return whether the player of this client is allowed to do an input.
     */
    public boolean getInputAllowed() {
        return inputAllowed;
    }

    /**
     * Sets up the pre-game interface by hiding the main menu and showing the
     * game lobby with all names that are currently connected.
     */
    private void joinGame() {
        title.setVisible(false);
        mainButtons.setVisible(false);
        bottom.setVisible(false);

        lobbyPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 40));
        lobbyPanel.add(lobby);
        add(lobbyPanel, BorderLayout.CENTER);
    }

    /**
     * Resets the card status, whether one is playable or not. In this case, all
     * cards are set playable as otherwise it would be disturbing for the
     * player.
     */
    public void resetPlayableCards() {
        ArrayList<CardPanel> hand = ownHand.getOwnHand();
        for (CardPanel cv : hand) {
            cv.setPlayable(true);
        }
    }

    /**
     * Sets the card status in the corresponding boolean array based on all the
     * given cards that are playable at the moment.
     *
     * @param cards All playable card at the moment.
     */
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

    /**
     * Opens a popup window that asks for the number of stitches, the player
     * wants.
     *
     * @param forbidden The number the player is not allowed to say if he is the
     *                  last one to say his stitches and the rule is enabled. If
     *                  not, this value must be -1.
     * @return The input made by the player.
     */
    private String askForStitches(int forbidden) {
        if (forbidden <= -1) {
            return Utility.askInput("Gewünschte Stichzahl eingeben.");
        }
        return Utility.askInput("Gewünschte Stichzahl eingeben (nicht " + forbidden + "):");
    }

    /**
     * Allows input of the player and updates the playable cards in the player's
     * hand. This method is called by the CClient when it receives that it's the
     * player's turn.
     */
    public void requestCard() {
        inputAllowed = true;
        updatePlayableCards();
    }

    /**
     * Updates the playable cards of the player for the next turn.
     */
    private void updatePlayableCards() {
        ownHand.setPlayableCards(allowedCards);
    }

    /**
     * Asks for user input for the number of stitches and interprets it wants.
     * If there is an invalid input (e.g. no number, negative number, forbidden
     * number,...), it automatically asks again for an input (as long as a valid
     * input is given).
     *
     * @param forbiddenNumber The number the player is not allowed to say if he
     *                        is the last one to say his stitches and the rule
     *                        is enabled. If not, this value must be -1.
     *
     * @return The number of stitches the player said.
     */
    public int validateStitches(int forbiddenNumber) {
        String stitchesCount = askForStitches(forbiddenNumber);
        int sCount;
        if(stitchesCount != null) {
            try {
                sCount = Integer.parseInt(stitchesCount);
            } catch (Exception e) {
                return validateStitches(forbiddenNumber);
            }
        } else {
            return validateStitches(forbiddenNumber);
        }

        if (sCount == forbiddenNumber || sCount < 0) {
            return validateStitches(forbiddenNumber);
        }
        return sCount;
    }

    /**
     * Opens a popup window that asks for the trump color, the player wants.
     *
     * @return The user's input.
     */
    private String askForTrumpColor() {
        return Utility.askInput("Gewünschte Trumpffarbe eingeben (Grün, Blau, Rot, Gelb):");
    }

    /**
     * Asks for user input for the desired trump color and interprets it. If
     * there is an invalid input, in this case none of the words "blau", "grün",
     * "rot" or "gelb" (not case-sensitive).
     *
     * @return The trump color the player entered.
     */
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

    /**
     * Returns a specific player by its unique player ID.
     * @param id The player's ID.
     * @return the player with the given ID.
     */
    public Player getPlayerByID(int id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * Sets relative IDs so every client can show the other players in the right
     * order based on his position.
     */
    private void setRelativeIDs() {
        relativeID[0] = idSelf;
        for (int i = 0; i < players.size() - 1; i++) {
            relativeID[i + 1] = (idSelf + 1 + i) % players.size();
        }
    }

    /**
     * Shows an information dialog popup when a player or the server
     * disconnects.
     *
     * @param name The disconnected player's name.
     */
    public void disconnected(String name) {
        Utility.showInfoDialog(name + " hat das Spiel verlassen!");
    }

    /**
     * Starts the game by hiding the lobby and showing all main game components,
     * e.g. the player's hand, the current stitch, the trump color, player stats
     * and all the other stuff.
     */
    public void startGame() {
        lobbyPanel.setVisible(false);
        setRelativeIDs();
        opw = new OtherPlayersView(createSortedPlayerView());
        selfView = new PlayerView(players.get(relativeID[0]));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new VerticalFlowLayout());
        add(leftPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        add(centerPanel, BorderLayout.CENTER);

        add(opw, BorderLayout.NORTH);
        leftPanel.add(trump);
        leftPanel.add(selfView);
        centerPanel.add(stitchImage);
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

    /**
     * Creates and shows a new window with the chat.
     */
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

        cw.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                resetChatWindow();
                isOpened = false;
            }
        });
    }

    /**
     * Creates a sorted list of all players based on the relative IDs to show
     * the other players in the correct order.
     *
     * @return a list with all the other players in the right order based on the
     * relative IDs.
     */
    private ArrayList<PlayerView> createSortedPlayerView() {
        ArrayList<PlayerView> playerView = new ArrayList<>();
        for (int i = 0; i < players.size() - 1; i++) {
            playerView.add(new PlayerView(players.get(relativeID[i + 1])));
        }
        return playerView;
    }

    /**
     * Creates a list of card panels of the own hand to be shown in the gui.
     *
     * @return a list of card panels ot the own hand.
     */
    private ArrayList<CardPanel> createCardView() {
        ArrayList<CardPanel> cards = new ArrayList<>();
        for (Card c : hand) {
            cards.add(new CardPanel(c, initScale, true));
        }
        return cards;
    }

    /**
     * Sorts the own hand according to the card order defined in the card enum
     * and shows it in the gui.
     */
    public void showOwnHand() {
        Collections.sort(hand);
        ownHand.initHand(createCardView());
    }

    /**
     * Locks the user's input again if his turn is over.
     */
    public void lockInput() {
        inputAllowed = false;

    }

    /**
     * Adds a new player and shows it in the lobby. This is called by the
     * CClient when a new player joins the game.
     *
     * @param p The player to be added.
     */
    public void addPlayer(Player p) {
        players.add(p);
        updateNames();
    }

    /**
     * Updates all the player names in the lobby.
     */
    public void updateNames() {
        for (int i = 0; i < players.size(); i++) {
            lobby.addPlayerName(players.get(i), i);
        }
    }

    /**
     * Updates the current trump color.
     */
    public void updateTrump() {
        if (currentTrumpCard != null && currentRound != (60 / players.size())) {
            trump.setTrumpCard(currentTrumpCard);
        }
    }

    /**
     * Sets the trump color.
     *
     * @param c The trump color.
     */
    public void setTrumpColor(ColorW c) {
        trump.setTrumpColor(c);
    }

    /**
     * Makes the main game thread sleep for the given amount of time.
     *
     * @param millis The time in milliseconds, the thread should sleep.
     */
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Marks the player with a green square when he made a stitch.
     *
     * @param id The player to be marked.
     */
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

    /**
     * Marks the players after one stitch round according to if they were right
     * or not. Correct players are marked green, the others red.
     */
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

    /**
     * Updates the round counter in the gui.
     */
    public void updateRoundCounter() {
        cRound.setText("Runde: " + currentRound);
    }

    /**
     * Updates the current stitch sum in the gui.
     * @param sum the new stitch sum.
     */
    public void updateStitchSum(int sum) {
        if (sum != -1) {
            stitchSum.setText("Stiche: " + sum);
        } else {
            stitchSum.setText("Stiche: -");
        }
    }

    /**
     * Updates the current points of all players in the gui.
     */
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

    /**
     * Updates the made stitches count of all players in the gui.
     */
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

    /**
     * Updates the said stitches count of all players in the gui.
     */
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

    /**
     * Updates the current stitch in the gui.
     */
    public void updateStitch() {
        for (int i = 0; i < 6; i++) {
            stitchImage.getStitch().get(i).setCard(null);
        }
        for (int i = 0; i < stitch.size(); i++) {
            stitchImage.getStitch().get(i).setCard(stitch.get(i));
        }

    }

    /**
     * Updates the player markers in the gui. This marks the player who is on
     * turn in blue and removes any markers from the other players.
     */
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

    /**
     * Resets the stats (made/said stitches, trump) when a new stitch round
     * starts.
     */
    public void resetStats() {
        selfView.resetStats();
        for (int i = 0; i < players.size() - 1; i++) {
            opw.getPlayerView(i).resetStats();
        }
        trump.setTrumpCard(null);
    }

    /**
     * Resets the trump.
     */
    public void resetTrump() {
        trump.setTrumpColor(null);
        trump.setTrumpCard(null);
    }

    /**
     * Shows a popup dialog with the winner's name and his points and asks if
     * the player wants to play again. Then all currently open windows are
     * closed and a "new gui" is created.
     *
     * @param nameWinner The name of the winner.
     * @param playerID The winner's ID to determine his points.
     */
    public void gameOver(String nameWinner, int playerID) {
        int winningPoints = 0;
        for (Player p : players) {
            if (p.getId() == playerID) {
                winningPoints = p.getPoints();
            }
        }
        int result = Utility.showConfirmDialog(nameWinner + " hat mit " + winningPoints + " Punkten gewonnen. Erneut spielen?", "Game over.");
        if(result == JOptionPane.YES_OPTION) {
            for(Frame f: Frame.getFrames()) {
                f.dispose();
            }
            String[] argsNew = new String[0];
            try {
                Main.main(argsNew);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Shows a popup window with a text field where the user can enter a name.
     *
     * @return the name the user entered.
     */
    public String getInputName() {
        return Utility.askInput("Name eingeben:");
    }

    /**
     * Shuts down the CClient if the user decided to quit the game.
     */
    public void shutdown() {
        if (cClient != null && !cClient.verbindungGescheitert()) {
            cClient.verbindungSchliessen();
        }
    }

    /**
     * Adds a message to the chat. If the chat is not open, it marks the chat
     * button with a green dot (if not already there) and plays a notification
     * sound (if the sound is not muted).
     *
     * @param s The text of the chat message.
     */
    public void addMessage(String s) {
        if (chat.size() > 9) {
            chat.remove(0);
        }
        chat.add(s);
        if (cw != null) {
            cw.updateChat(chat);
        } else {
            openChat.setIcon(new ImageIcon("./Resources/dot.png"));
            if(!mute.isSelected()) {
                Utility.playPingSound();
            }
        }
    }

    /**
     * Resets/removes the chat window.
     */
    public void resetChatWindow() {
        cw = null;
    }

    /**
     * Returns the name of this player.
     *
     * @return the name of this player.
     */
    public String getDisplayName() {
        return players.get(relativeID[0]).getName();
    }

    public void playSound(String filename) {
        if(!mute.isSelected()) {
            Utility.playSound(filename);
        }
    }
}
