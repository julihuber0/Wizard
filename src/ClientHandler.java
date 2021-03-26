
import ea.*;

public class ClientHandler implements Empfaenger {
    /**
     * Die Netzwerkverbindung, über die der Handler nachrichten senden und empfangen kann.
     */
    private NetzwerkVerbindung verbindung;

    /**
     * Der Name des Client, um den sich dieser Handler kümmert.
     */
    private String clientname = "";
    private int id;
    private Server server;
    private Player player;

    public ClientHandler(NetzwerkVerbindung verbindung) {
        this.verbindung = verbindung;

        //Dieses Objekt als Empfänger an der Verbindung melden
        verbindung.empfaengerHinzufuegen(this);
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void sendeString(String s) {
        verbindung.sendeString(s);
    }

    /* --- Empfaenger-Methoden --- */

    @Override
    public void empfangeString (String string) {
        System.out.println("[Server hat empfangen:] " + string);

        String key = string.substring(0,2);
        String content = string.substring(3);
        switch (key) {
            case "NA":
                //Namen setzen
                if(content.contains("/") || content.contains(";") ) {
                    sendeString("NF/");
                }
                else {
                    server.addPlayer(content,id);
                }
                break;

            case "GK":
                String[] s = content.split(";");
                player.played = new Card(Integer.parseInt(s[0]),ColorW.valueOf(s[1]));
                break;

            case "TA":
                player.selectedTrump = ColorW.valueOf(content);
                break;

            default:
                break;

        }

    }

    @Override
    public void verbindungBeendet () {
        server.disconnected(player.getName());
    }

    // Info: Diese Methoden müssen implementiert werden.
    //       Sie bleiben leer, da sie hier nicht verwendet werden.

    @Override
    public void empfangeInt (int i) {
    }

    @Override
    public void empfangeByte (byte b) {
    }

    @Override
    public void empfangeDouble (double d) {
    }

    @Override
    public void empfangeChar (char c) {
    }

    @Override
    public void empfangeBoolean (boolean b) {
    }

    //Nur um schnell was zu testen falls nötig
    public static void main(String[] args) {
        EngineAlpha.main(null);
        for(int i = 1;i<13;i++) {
            if (i % 2 != 0) {
                int p = (i - 1) / 2;
                System.out.println(p);
            }
        }
    }

}
