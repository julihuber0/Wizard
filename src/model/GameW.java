package model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The GameW class has the main game logic.
 */
public class GameW implements Runnable {

    /**
     * The number of players in the game.
     */
    public int playerCount;

    /**
     * The maximal round in the game.
     */
    public int maxRounds;

    /**
     * The current round.
     */
    public int currentRound = 0;

    /**
     * The current game state.
     */
    public GameState gs = GameState.WAITING_FOR_NEXT_ROUND;

    /**
     * All players in the game.
     */
    public ArrayList<Player> players = new ArrayList<>();

    /**
     * All cards in the current stitch.
     */
    public ArrayList<Card> stitch = new ArrayList<>();

    /**
     * The number value of the current trump card.
     */
    public Integer currentTrumpValue = null;

    /**
     * The color of the current trump card.
     */
    public ColorW currentTrumpColor = null;

    /**
     * The current trump color.
     */
    public ColorW currentTrump = null;

    /**
     * The whole card deck with all 60 cards.
     */
    public CardDeck deck = new CardDeck();

    /**
     * The player's ID who's on turn.
     */
    public int currentPlayerID = 0;

    /**
     * The player's ID who is in the position of the dealer.
     */
    public int dealerID = -1;

    /**
     * The server where all the players connect to.
     */
    private Server server;

    /**
     * The number of stitches, the last player who has to say his stitches may not be allowed to say.
     */
    public int forbiddenNumber;

    /**
     * Whether all stitch numbers are allowed or not (use of forbidden number).
     */
    public boolean forbidden = true;

    /**
     * Creates a new game and a new server for the players to connect to.
     */
    public GameW() {
        run();

        server = new Server(this);
    }

    /**
     * Sets whether stitches are allowed to fit to the current round or not.
     *
     * @param f If the mentioned rule is enabled or not.
     */
    public void setForbidden(boolean f) {
        forbidden = f;
    }

    /**
     * Returns the server.
     *
     * @return the server.
     */
    public Server getServer() {
        return server;
    }

    /**
     * Adds a new player to the game. If another player is added while the current player hasn't passed a name yet, its
     * name will be temporarily set to "Connecting". Also makes the server notify the clients that a new player was
     * added.
     *
     * @param p The player to be added.
     */
    public void addPlayer(Player p) {
        if(players.size()==p.getId()) {
            players.add(p);
        }
        else if (players.size()>p.getId()) {
            players.add(p.getId(),p);
            players.remove(p.getId()+1);
        }
        else {
            while (players.size() < p.getId()) {
                players.add(new Player("Connecting...", players.size()));
            }
            players.add(p);
        }

        server.sendString("CP/");
        for (Player player : players) {
            String toSend = "PN/" + player.getName() + ";" + player.getId();
            server.sendString(toSend);
        }
    }

    /**
     * Sets the number of players for the game. If there are less than 3 or more than 6 players, it throws an exception.
     *
     * @param pc The number of players.
     */
    public void setPlayerCount(int pc) {
        if (pc > 2 && pc < 7) {
            this.playerCount = pc;
        } else {
            throw new IllegalArgumentException("Die Spielerzahl muss mindestens 3 und darf maximal 6 sein.");
        }
    }

    /**
     * Sets the maximum round corresponding to the current player count.
     *
     * @param pc The number of players in the game.
     */
    public void setMaxRounds(int pc) {
        if (pc > 2 && pc < 7) {
            this.maxRounds = 60 / pc;
        } else {
            throw new IllegalArgumentException("Die Spielerzahl muss mindestens 3 und darf maximal 6 sein.");
        }
    }

    /**
     * Sets the round counter to the next round and makes the server notify the clients. If we're in the last round, the
     * game state is set to "OVER".
     */
    public void nextRound() {
        if (currentRound < maxRounds) {
            currentRound++;
            dealerID = (dealerID + 1) % players.size();
            server.update();
            server.sendString("RC/");
        } else {
            gs = GameState.OVER;
        }
    }

    /**
     * Distributes dependent on the current round the cards to each player and makes the server update.
     *
     * @param round The current round.
     */
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

    /**
     * Returns the player the stitch belongs to when all players played a card.
     *
     * @return the player who made the stitch.
     */
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

    /**
     * Returns the winner of the game if the game is over.
     *
     * @return the winner.
     */
    public Player getWinner() {
        if (gs == GameState.OVER) {
            int maxPoints = Integer.MIN_VALUE;
            Player bestPlayer = null;
            for (Player p: players) {
                if (p.getPoints() > maxPoints) {
                    maxPoints = p.getPoints();
                    bestPlayer = p;
                }
            }
            return bestPlayer;
        }
        return null;
    }

    /**
     * Sets the dynamic number of each player.
     */
    public void setDynNumbers() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).dynNumber = i;
        }
    }

    /**
     * Returns a list with all players in the order were the given player is on the first position.
     *
     * @param p The player to be on the first position.
     * @return the list in the right order with the given player on the first position.
     */
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

    /**
     * Returns the color that must be served in the current stitch.
     *
     * @return the color that must be served.
     */
    public ColorW getAllowed() {
        ColorW allowed = null;
        if (!stitch.isEmpty()) {
            if (stitch.get(0).getValue() == 14) {
                return null;
            }
            for (int i = 0; i < players.size() && i < stitch.size() && stitch.get(i) != null; i++) {
                if (stitch.get(i).getValue() != 0) {
                    if(stitch.get(i).getValue()==14)
                    {
                        return null;
                    }
                    allowed = stitch.get(i).getColor();
                    break;
                }
            }
        }
        return allowed;
    }

    /**
     * Returns the player of a given ID.
     *
     * @param id The ID of a player.
     * @return the player with the given ID.
     */
    public Player getPlayerToID(int id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * Returns a list with all cards of the given player's hand that he is allowed to play in the current turn.
     *
     * @param p The player, we want the allowed cards from.
     * @return a list with all the player's allowed cards.
     */
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

    /**
     * Starts the game by setting up the game parameters (player count, max round) and starting the main game logic.
     */
    public void start() {
        setPlayerCount(players.size());
        setMaxRounds(players.size());
        //currentRound = 14;
        startNextRound();
    }

    /**
     * Starts a new round. This method controls the gameplay (distributing all cards, maybe asking a player for trump
     * color, asking all players for stitch count, consequently asks players for cards to play, decides who made a
     * stitch, calculates the points for every player, starts then a new round, ends the game).
     */
    public synchronized void startNextRound() {
        server.startNextRound();
        gs = GameState.RUNNING;
        nextRound();
        if (gs != GameState.OVER) {
            players = getNewFirstPlayer(getPlayerToID(dealerID));
            distribute(currentRound);
            Card currentTrumpCard = null;
            if(!deck.isEmpty()) {
                currentTrumpCard = deck.removeCard();
                currentTrumpValue = currentTrumpCard.getValue();
                currentTrumpColor = currentTrumpCard.getColor();
            }
            //server.update();
            if (currentTrumpCard == null || currentTrumpCard.getValue() == 0) {
                currentTrump = null;
                server.update();
            } else if (currentTrumpCard.getValue() == 14) {
                Player p = players.get(players.size()-1);
                currentPlayerID = p.getId();
                p.selectedTrump = null;
                currentTrump = null;
                server.update();
                p.selectTrump();
                p.setThread(Thread.currentThread());
                System.out.println(Thread.currentThread().getName() + " eingeschläfert bei Trumpfabfrage von "+p.getName());
                try { //einschläfern, wird dann durch den ClientHandler geweckt
                    this.wait();
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    System.out.println("Thread wurde unterbrochen (Fehler wird geworfen, kann ignoriert werden).");
                }

                this.run();
                System.out.println("Falsches Gutten Tag");

                currentTrump = p.selectedTrump;

                server.update();
            } else {
                currentTrump = currentTrumpCard.getColor();
                server.update();
            }

            forbiddenNumber = currentRound;
            for (int i = 0; i < players.size(); i++) {
                players.get(i).saidStitches = -1;
                currentPlayerID = players.get(i).getId();
                server.update();
                if (i + 1 == players.size() && forbidden) { //Der letzte in der Reihe bekommt mit, welche Anzahl er nicht sagen darf
                    players.get(i).sayStitches(forbiddenNumber);
                } else {
                    players.get(i).sayStitches(-1);
                }
                //ToDo @Tobi wtf
                players.get(i).setThread(Thread.currentThread());
                System.out.println(Thread.currentThread().getName() + " eingeschläfert bei Stichabfrage von "+players.get(i).getName());
                try { //einschläfern, wird dann durch den ClientHandler geweckt
                    this.wait();
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    System.out.println("Thread wurde unterbrochen (Fehler wird geworfen, kann ignoriert werden).");
                }

                this.run();
                System.out.println("Gutten Tag");
                forbiddenNumber = forbiddenNumber - players.get(i).getSaidStitches();
                server.update();
                server.sendString("SC/"+(currentRound-forbiddenNumber));
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
                    System.out.println(Thread.currentThread().getName() + " eingeschläfert bei Kartenabfrage von "+players.get(j).getName()+".");
                    try { //einschläfern, wird dann durch den ClientHandler geweckt
                        this.wait();
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        System.out.println("Thread wurde unterbrochen (Fehler wird geworfen, kann ignoriert werden).");
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
                server.update();
                server.sendString("TP/"+p.getId());
                warten(2300);
                p.addStitch();
                sendLastStitch();
                stitch.clear();
                players = getNewFirstPlayer(p);
                server.sendString("RP/");
                server.update();
            }
            server.sendString("MP/");
            warten(2500);

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
        //TODO: Gewinner wird nicht angezeigt
        Player winner = getWinner();
        //server.gameOver(winner.getName(), winner.getId());
        server.update();
    }

    private void warten(long i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            System.out.println("Waiting");
        }
    }

    //Alternative Sortiermethode für eine Liste mit Karten, falls bei der normalen Methode Fehler auftreten
    public static ArrayList<Card> sortCardArray( ArrayList <Card> toSort) {
        ArrayList<Card> clone = new ArrayList<>(toSort);
        ArrayList<Card> sorted = new ArrayList<>();
        ArrayList<Card> narren = new ArrayList<>();

        //Zauberer ganz vorne
        for(Card c:clone) {
            if(c.getValue() == 14) {
                sorted.add(new Card(c.getValue(),c.getColor()));
                toSort.remove(c);
            }
        }

        //Narren ganz hinten
        for(Card c:clone) {
            if(c.getValue() == 0) {
                narren.add(new Card(c.getValue(),c.getColor()));
                toSort.remove(c);
            }
        }

        Collections.sort(toSort);

        for(Card c: toSort) {
            sorted.add(new Card(c.getValue(),c.getColor()));
        }
        for (Card c:narren) {
            sorted.add(new Card(c.getValue(),c.getColor()));
        }


        return sorted;
    }

    /**
     * Makes the server send the last stitch to the clients.
     */
    public void sendLastStitch() {
        String stitchLS = "LS/";

        for(int i = 0; i<stitch.size(); i++) {
            if(stitch.get(i).getValue() == 0) {
                stitchLS = stitchLS + "Narr in ";
            }
            else if(stitch.get(i).getValue() == 14) {
                stitchLS = stitchLS + "Zauberer in ";
            }
            else {
                stitchLS = stitchLS + stitch.get(i).getValue() + " in ";
            }

            stitchLS = stitchLS + ColorW.toString(stitch.get(i).getColor()) +" ["+players.get(i).getName()+"]  |  ";
        }
        server.sendString(stitchLS);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void run() {
        System.out.println("Fred läuft");
    }
}
