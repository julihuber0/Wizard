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
     * SB: Scoreboard
     * SS: Said Stitches
     * CS: Current Stiches
     * NF: Namen fragen
     * NA: Namen angeben
     */

    public void bcScoreboard(ArrayList<Player> players) {
        //zieht sich den Playername und den zugehörigen Score
        //formatiert ihn zu einem String (wird später in den Clients wieder decoded)
        String score = "SB;";

        for (Player p:players) {
            score = score + p.getName() + ";";
            score = score + p.getPoints() + "|";
        }

        //broadcastet den an alle Spieler
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
            handlers.add(c);

            //Namen erfragen
            c.setId(handlers.size()+1);
            c.sendeString("NF|");


        }
    }

    public void addPlayer(String name, int id) {
        Player p = new Player(name, id);
        game.addPlayer(p);
    }
}
