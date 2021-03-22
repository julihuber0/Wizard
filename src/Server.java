import ea.*;
import java.util.*;

public class Server  implements  Runnable{

    private ea.Server server;
    private ArrayList<ClientHandler> handlers = new ArrayList<ClientHandler>();
    private GameW game;

    public Server(GameW game) {
        server = new ea.Server(7654);
        new Thread(this).start();
        this.game = game;
    }

    /**
     * Bietet Broadcast (BC) Methoden an
     *
     * Methoden, die allgemein für alle Spieler zählen z.B. Trumpffarbe, Scoreboard etc.
     *
     * Unerlaubte Zeichen Spielername ; /
     *
     */

    /**
     * Sendet immer ein zweistelliges Kürzel gefolgt von einem Strichpunkt an die Clients zur Identifikation
     *
     * siehe File CommunicationShorts
     */

    public void update() {
        bcScoreboard(game.players);
        bcSaidStitches(game.players);
        bcCurrentStitches(game.players);
        bcCurrentRound(game.currentRound);
        bcCurrentStitch(game.stitch);
        bcCurrentTrump(game.currentTrump);
        bcCurrentPlayerID(game.currentPlayerID);
    }

    public void bcScoreboard(ArrayList<Player> players) {
        //zieht sich die PlayerID und den zugehörigen Score
        //formatiert ihn zu einem String (wird später in den Clients wieder decoded)
        //SB/0;2;30;3;10/1;1;-10;2;20/
        //SB/PlayerID;angesagteStiche;Punkte;angesagteStiche;Punkte/PlayerID;angesagteStiche;Punkte;angesagteStiche;Punkte/
        String score = "SB/";

        for (Player p:players) {
            score = score + p.getId() + ";";
            ArrayList<StitchHistory> sh = p.getSh();
            for (StitchHistory stitchHistory:sh) {
                score = score + stitchHistory.getStitches() + ";" + stitchHistory.getPoints() + ";";
            }
            score = score + "/";
        }

        //broadcastet den an alle Spieler
        server.sendeString(score);
    }

    public void bcSaidStitches(ArrayList<Player> players) {
        //Schlüssel ist die PlayerID
        //SC/1;3/2;0/ -> Said Stitches Player1 - 3 Stiche; Player2 - 0 Stiche

        String saidStitches = "SS/";

        for(Player p:players) {
            saidStitches = saidStitches + p.getId() + ";";
            saidStitches = saidStitches + p.getSaidStitches() + "/";
        }
        server.sendeString(saidStitches);
    }

    public void bcCurrentStitches(ArrayList<Player> players) {
        //Schlüssel ist die PlayerID
        //SC/1;3/2;0/ -> Current Stitches Player1 - 3 Stiche; Player2 - 0 Stiche

        String currentStitches = "CS/";

        for(Player p: players) {
            currentStitches = currentStitches + p.getId() + ";" + p.getCurrentStitches() + "/";
        }
        server.sendeString(currentStitches);
    }

    public void gameOver(String nameWinner, int playerID) {
        update();
        String winner = "GO/" + nameWinner + ";" + playerID;
        server.sendeString(winner);
    }

    public void bcCurrentRound(int round) {
        String currentRound = "AR/" + round;
        server.sendeString(currentRound);
    }

    public void bcCurrentStitch(ArrayList<Card> stitch) {
        String currentStitch = "AS/";
        for(Card c:stitch) {
            currentStitch = currentStitch + c.getValue() + ";" + c.getColor() + "/";
        }
        server.sendeString(currentStitch);
    }

    public void bcCurrentTrump(ColorW c) {
        String currentTrump = "AT/" + c;
        server.sendeString(currentTrump);
    }

    public void bcCurrentPlayerID (int playerID) {
        String currentPlayerID = "AP/" + playerID;
        server.sendeString(currentPlayerID);
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
            System.out.println("Verbindung reingekommen");

            ClientHandler c = new ClientHandler(verbindung);
            System.out.println("CH erstellt");
            //Namen erfragen
            c.setId(handlers.size());
            c.setServer(this);
            c.sendeString("NF/");
            System.out.println("Die Frage gestellt");

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
    public void sendString(String s) {
        server.sendeString(s);
    }
}
