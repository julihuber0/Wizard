import java.util.ArrayList;

public class Server {


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
        //broadcastet den an alle Spieler

        for (Player p:players) {


        }
    }

}
