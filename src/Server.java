import ea.*;
import java.util.*;

public class Server  implements  Runnable{

    private ea.Server server;
    private ArrayList<ClientHandler> handlers = new ArrayList<ClientHandler>();

    /**
     * Bietet Broadcast (BC) Methoden an
     *
     * Methoden, die allgemein für alle Spieler zählen z.B. Trumpffarbe, Scoreboard etc.
     *
     */

    /**
     * Sendet immer ein zweistelliges Kürzel gefolgt von einem Strichpunkt an die Clients zur Identifikation
     *
     * SC: Scoreboard
     *
     */

    public void bcScoreboard(ArrayList<Player> players) {
        //zieht sich den Playername und den zugehörigen Score
        //formatiert ihn zu einem String (wird später in den Clients wieder decoded
        String score = "SC;";

        for (Player p:players) {
            score = score + p.getName() + ";";
            score = score + p.getPoints() + ";";
        }

        //broadcastet den an alle Spieler
        server.sendeString(score);
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



        }
    }
}
