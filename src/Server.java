import ea.*;
import java.util.*;

public class Server  implements  Runnable{

    private ea.Server server;
    private ArrayList<ClientHandler> handlers = new ArrayList<ClientHandler>();
    private GameW game;

    public Server(GameW game) {
        this.game = game;
    }

    /**
     * Bietet Broadcast (BC) Methoden an
     *
     * Methoden, die allgemein für alle Spieler zählen z.B. Trumpffarbe, Scoreboard etc.
     *
     * Unerlaubte Zeichen Spielername ; |
     *
     */

    /**
     * Sendet immer ein zweistelliges Kürzel gefolgt von einem Strichpunkt an die Clients zur Identifikation
     *
     * siehe File CommunicationShorts
     */

    public void update() {
        bcCurrentRound(game.currentRound);
        bcCurrentStitch(game.stitch);
        bcCurrentTrump(game.currentTrump);
        bcScoreboard(game.players);
        bcCurrentPoints(game.players);
        bcSaidStitches(game.players);
        bcCurrentStitches(game.players);
    }

    public void bcScoreboard(ArrayList<Player> players) {
        //zieht sich den Playername und den zugehörigen Score
        //formatiert ihn zu einem String (wird später in den Clients wieder decoded)
        String score = "SB|";

        for (Player p:players) {
            score = score + p.getName() + ";";
            ArrayList<StitchHistory> sh = p.getSh();
            for (StitchHistory stitchHistory:sh) {
                score = score + stitchHistory.getStitches() + ";" + stitchHistory.getPoints() + ";";
            }
            score = score + "|";
        }

        //broadcastet den an alle Spieler
        server.sendeString(score);
    }

    public void bcCurrentPoints(ArrayList<Player> players) {
        //CP|Julian;50|Luca;100|
        String score = "CP|";
        for (Player p: players) {
            score = score + p.getName() + ";" + p.getPoints() + "|";
        }
        server.sendeString(score);
    }

    public void bcSaidStitches(ArrayList<Player> players) {
        //Schlüssel ist die Position in der ArrayList
        //SC|1;3|2;0| -> Said Stichtes Player1 - 3 Stiche; Player2 - 0 Stiche

        String saidStitches = "SS|";

        for(Player p:players) {
            saidStitches = saidStitches + p.getId() + ";";
            saidStitches = saidStitches + p.getSaidStitches() + "|";
        }
        server.sendeString(saidStitches);
    }

    public void bcCurrentStitches(ArrayList<Player> players) {
        //Schlüssel ist die Position in der ArrayList
        //SC|1;3|2;0| -> Current Stitches Player1 - 3 Stiche; Player2 - 0 Stiche

        String currentStitches = "SS|";

        for(int i = 0; i<players.size();i++) {
            currentStitches = currentStitches + i + ";";
            currentStitches = currentStitches + players.get(i).getCurrentStitches() + "|";
        }
        server.sendeString(currentStitches);
    }

    public void gameOver(String nameWinner) {
        update();
        String winner = "GO|" + nameWinner;
        server.sendeString(winner);
    }

    public void bcCurrentRound(int round) {
        String currentRound = "AR|" + round;
        server.sendeString(currentRound);
    }

    public void bcCurrentStitch(ArrayList<Card> stitch) {
        String currentStitch = "AS|";
        for(Card c:stitch) {
            currentStitch = currentStitch + c.getValue() + ";" + c.getColor() + "|";
        }
        server.sendeString(currentStitch);
    }

    public void bcCurrentTrump(Color c) {
        String currentTrump = "AT|" + c;
        server.sendeString(currentTrump);
    }

    /**
     * In dieser Run-Methode wartet ein eigener Thread nur auf neu andockende Clients,
     * um diese dann durch einen eigenen ClientHandler zu bedienen.
     */
    @Override
    public void run() {
        //Dauerschleife
        while(!Thread.interrupted()) {
            //Gib mir die nächste Verbindung
            //   (warte ggf. solange, bis eine neue Verbindung zustandekommt)
            NetzwerkVerbindung verbindung = server.naechsteVerbindungAusgeben();

            ClientHandler c = new ClientHandler(verbindung);

            //Namen erfragen
            c.setId(handlers.size());
            c.setServer(this);
            c.sendeString("NF|");

            //hinzufügen
            handlers.add(c);
        }
    }

    public void addPlayer(String name, int id) {
        Player p = new Player(name, id);
        p.setCh(handlers.get(id));
        handlers.get(id).setPlayer(p);
        game.addPlayer(p);
    }
}
