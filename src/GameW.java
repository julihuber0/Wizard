import ea.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class GameW extends Game implements MausReagierbar, Runnable {

    public int playerCount;        //Anzahl Mitspieler
    public int maxRounds;      //Maximale Rundenzahl
    public int currentRound = 0;       //aktuelle Runde
    public GameState gs = GameState.WAITING_FOR_NEXT_ROUND;        //aktueller Spielzustand
    public ArrayList<Player> players = new ArrayList<>();      //Liste, welche alle Spieler beinhaltet
    public ArrayList<Card> stitch = new ArrayList<>();     //Liste, die den aktuellen Stich hält.
    public Integer currentTrumpValue = null;    //Wert der aktuellen Trumpfkarte
    public ColorW currentTrumpColor = null;     //Farbe der aktuellen Trumpfkarte
    public ColorW currentTrump = null;      //aktuelle Trumpffarbe
    public CardDeck deck = new CardDeck();     //Kartendeck
    public int currentPlayerID = 0;
    public int dealerID = -1;
    private Server server;
    private String ipadress;
    private String ipadress2 = "";

    //GUI Elements
    private Maus maus;
    private Text startButton;
    private Text displayIP;
    private Text displayIP2;

    public GameW() {
        super(500, 300, "Wizard-Server", false, false);
        run();

        server = new Server(this);
        maus = new Maus(new Bild(0, 0, "Resources/pointer.png"), new Punkt(0, 0));
        mausAnmelden(maus);
        startButton = new Text("Starte Spiel", 100, 100, "Segoe UI", 20);
        wurzel.add(startButton);
        startButton.sichtbarSetzen(true);
        maus.anmelden(this, startButton, 0);

        //IP-Adresse ausgeben
        try {
            URL myIP = new URL("http://myip.dnsomatic.com/");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(myIP.openStream())
            );
            ipadress = in.readLine();
        } catch (Exception e) {
            ipadress = "Fehler beim Anzeigen der eigenen IP-Adresse.";
            ipadress2 = "Der Server startet ganz normal.";
        }

        displayIP = new Text("IP: " + ipadress, 40, 200, "Segoe UI", 20);
        wurzel.add(displayIP);
        displayIP.sichtbarSetzen(true);

        if (!ipadress2.isEmpty()) {
            displayIP2 = new Text(ipadress2, 40, 240, "Segoe UI", 20);
            wurzel.add(displayIP2);
            displayIP2.sichtbarSetzen(true);
        }
    }

    public void mausReagieren(int code) {
        switch (code) {
            case 0:
                server.sendString("SG/");
                start();
                break;
            default:
                System.out.println("lel");
                break;
        }
    }

    public void addPlayer(Player p) {
        players.add(p);
        //Collections.sort(players); //ToDo @Tobi
        server.sendString("CP/");
        for (Player player : players) {
            String toSend = "PN/" + player.getName() + ";" + player.getId();
            server.sendString(toSend);
        }
    }

    public void tasteReagieren(int code) {
    }

    //legt fest, wie viele Spieler mitspielen
    public void setPlayerCount(int pc) {
        if (pc > 2 && pc < 7) {
            this.playerCount = pc;
        } else {
            throw new IllegalArgumentException("Die Spielerzahl muss mindestens 3 und darf maximal 6 sein.");
        }
    }

    //setzt abhängig von der Spielerzahl die maximale Rundenzahl
    public void setMaxRounds(int pc) {
        if (pc > 2 && pc < 7) {
            this.maxRounds = 60 / pc;
        } else {
            throw new IllegalArgumentException("Die Spielerzahl muss mindestens 3 und darf maximal 6 sein.");
        }
    }

    //schaltet den Rundenzähler auf die nächste Runde, setzt GameState auf OVER, wenn letzte Runde
    public void nextRound() {
        if (currentRound < maxRounds) {
            currentRound++;
            dealerID = (dealerID + 1) % players.size();
            server.update();
        } else {
            gs = GameState.OVER;
        }
    }

    //teilt rundenabhängig Karten an die Spieler aus
    public void distribute(int round) {
        deck = new CardDeck();
        deck.shuffleDeck();
        for (Player p : players) {
            for (int i = 0; i < round; i++) {
                p.addCards(deck.removeCard());
            }
        }
        for (Player p : players) {
            p.sortHand();
            p.sendHand();
        }
        server.update();
    }

    //gibt den Spieler zurück, dem der aktuelle Stich gehört
    public Player calculateStitch() {
        Card highestCard = null;
        int highCardPos = -1;
        for (int i = 0; i < players.size(); i++) {
            if (stitch.get(i).getValue() == 14)    //sobald ein Zauberer auftritt: Höchste Karte gefunden
            {
                return players.get(i);
            }
            if (stitch.get(i).getValue() == 0) {   //Narren werden übergangen, bei nur Narren sticht der Erste
                continue;
            }
            if (highestCard == null || (stitch.get(i).getValue() > highestCard.getValue() && stitch.get(i).getColor() == highestCard.getColor())) {
                highestCard = stitch.get(i);    //Wenn Kartenwert größer, als der der aktuellen höchsten Karte und gleiche Farbe: Neue hächste Karte
                highCardPos = i;
                continue;
            }
            if (highestCard.getColor() != currentTrump && stitch.get(i).getColor() == currentTrump)    //Trumpf sticht Nicht-Trumpf
            {
                highestCard = stitch.get(i);
                highCardPos = i;
                continue;
            }
        }
        if(highCardPos == -1){
            return players.get(0);
        }
        return players.get(highCardPos);
    }

    public Player getWinner() {
        if (gs == GameState.OVER) {
            int maxPoints = Integer.MIN_VALUE;
            int bestPlayer = 0;
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getPoints() > maxPoints) {
                    maxPoints = players.get(i).getPoints();
                    bestPlayer = i;
                }
            }
            return players.get(bestPlayer);
        }
        return null;
    }

    public void setDynNumbers() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).dynNumber = i;
        }
    }

    //gibt eine neue Liste mit Spielern zurück, bei der der übergebene Spieler an erster Stelle ist
    public ArrayList<Player> getNewFirstPlayer(Player p) {
        setDynNumbers();
        for (Player pl : players) {
            System.out.println("Players-List: " + pl.getName() + ", " + pl.getId());
        }
        ArrayList<Player> newPlayers = new ArrayList<>();
        boolean behind = false;
        for (int i = 0; i < players.size(); i++) {
            if (p.dynNumber == players.get(i).dynNumber) {
                behind = true;
            }
            if (behind) //if (behind == true)
            {
                newPlayers.add(players.get(i));
            }
        }
        Player f = players.get(0);
        for (int i = 1; f.dynNumber < p.dynNumber; i++) {
            newPlayers.add(f);
            f = players.get(i);
        }
        for (Player pl : newPlayers) {
            System.out.println("Players: " + pl.getName());
        }
        return newPlayers;
    }

    //gibt die in diesem Stich erlaubte Farbe zurück
    public ColorW getAllowed() {
        ColorW allowed = null;
        if (!stitch.isEmpty()) {
            if (stitch.get(0).getValue() == 14) {
                return null;
            }
            for (int i = 0; i < players.size() && i < stitch.size() && stitch.get(i) != null; i++) {
                if (stitch.get(i).getValue() != 0) {
                    allowed = stitch.get(i).getColor();
                    break;
                }
            }
        }
        return allowed;
    }

    public Player getPlayerToID(int id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    //gibt eine Liste mit allen erlaubten/spielbaren Karten eines Spielers zurück
    public ArrayList<Card> getAllowedCards(Player p) {
        ColorW allowed = getAllowed();
        ArrayList<Card> allowedCards = new ArrayList<>();
        if (allowed == null) {
            return p.getHand();
        } else {
            for (Card c : p.getHand()) {
                if (c.getColor() == allowed && c.getValue()!=0 && c.getValue()!=14) {
                    allowedCards.add(c);
                }
            }
        }
        if (allowedCards.isEmpty()) {
            return p.getHand();
        }
        for (Card c : p.getHand()) {
            if (c.getValue() == 0 || c.getValue() == 14) {
                allowedCards.add(c);
            }
        }
        return allowedCards;
    }

    public void start() {
        setPlayerCount(players.size());
        setMaxRounds(players.size());
        startNextRound();
    }

    //startet die nächste Runde
    public synchronized void startNextRound() {
        server.startNextRound();
        gs = GameState.RUNNING;
        nextRound();
        if (gs != GameState.OVER) {
            players = getNewFirstPlayer(getPlayerToID(dealerID));
            distribute(currentRound);
            Card currentTrumpCard = deck.removeCard();
            currentTrumpValue = currentTrumpCard.getValue();
            currentTrumpColor = currentTrumpCard.getColor();
            //server.update();
            if (currentTrumpCard.getValue() == 0) {
                currentTrump = null;
                server.update();
            } else if (currentTrumpCard.getValue() == 14) {
                Player p = players.get((currentRound - 1) % players.size());
                p.selectedTrump = null;
                server.update();
                p.selectTrump();
                p.setThread(Thread.currentThread());
                System.out.println(Thread.currentThread().getName() + " eingeschläfert bei Trumpfabfrage");
                try { //einschläfern, wird dann durch den ClientHandler geweckt
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                this.run();
                System.out.println("Falsches Gutten Tag");

                currentTrump = p.selectedTrump;

                server.update();
            } else {
                currentTrump = currentTrumpCard.getColor();
                server.update();
            }

            int forbiddenNumber = currentRound;
            for (int i = 0; i < players.size(); i++) {
                players.get(i).saidStitches = -1;
                currentPlayerID = players.get(i).getId();
                server.update();
                if (i + 1 == players.size()) { //Der letzte in der Reihe bekommt mit, welche Anzahl er nicht sagen darf
                    players.get(i).sayStitches(forbiddenNumber);
                } else {
                    players.get(i).sayStitches(-1);
                }
                //ToDo @Tobi wtf
                players.get(i).setThread(Thread.currentThread());
                System.out.println(Thread.currentThread().getName() + " eingeschläfert bei Stichabfrage");
                try { //einschläfern, wird dann durch den ClientHandler geweckt
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                this.run();
                System.out.println("Gutten Tag");
                forbiddenNumber = forbiddenNumber - players.get(i).getSaidStitches();
                server.update();
            }

            //Spielphase
            for (int i = 0; i < currentRound; i++) {
                for (int j = 0; j < players.size(); j++) {
                    currentPlayerID = players.get(j).getId();
                    //stitch.add(players.get(i).requestCard());
                    players.get(j).sendPlayableCards(getAllowedCards(players.get(j)));
                    server.update();
                    players.get(j).setThread(Thread.currentThread());
                    players.get(j).requestCard();
                    System.out.println(Thread.currentThread().getName() + " eingeschläfert bei Kartenabfrage");
                    try { //einschläfern, wird dann durch den ClientHandler geweckt
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    this.run();
                    System.out.println("Gutten Tag");

                    stitch.add(players.get(j).played);
                    for(Card c:players.get(j).hand)
                    {
                        if(c.isEqual(players.get(j).played))
                        {
                            c.setValue(0);
                            break;
                        }
                    }
                    server.update();
                }
                Player p = calculateStitch();
                p.addStitch();
                server.update();
                server.sendString("TP/"+p.getId());
                warten(2000);
                stitch.clear();
                players = getNewFirstPlayer(p);
                server.update();
            }

            for (Player pl : players) {
                pl.clearHand();
            }
            currentTrumpColor = null;
            currentTrumpValue = null;
            currentTrump = null;

            server.update();

            //Punkteberechnung
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getSaidStitches() == players.get(i).getCurrentStitches()) {
                    players.get(i).addPoints(20 + (players.get(i).getCurrentStitches() * 10));
                    server.update();
                } else {
                    players.get(i).addPoints(-(10 * Math.abs((players.get(i).getSaidStitches() - players.get(i).getCurrentStitches()))));
                    server.update();
                }
            }

            for (Player pl : players) {
                pl.saidStitches = -1;
                pl.currentStitches = 0;
                System.out.println("Angesagte und gemachte Stiche für "+pl.getName()+" zurückgesetzt.");
            }
            gs = GameState.WAITING_FOR_NEXT_ROUND;
            startNextRound();
        }
        server.gameOver(getWinner().getName(), getWinner().getId());
        server.update();
    }


    @Override
    public void run() {
        System.out.println("Fred läuft");
    }

    public static void main(String[] args) {
        GameW gameW = new GameW();
    }
}
