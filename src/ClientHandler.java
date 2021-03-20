
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
                if(content.contains("|") || content.contains(";") ) {
                    sendeString("NF|");
                }
                else {
                    server.addPlayer(content,id);
                }
                break;

            default:
                break;

        }

    }

    @Override
    public void verbindungBeendet () {
        //ToDo
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
        String content = "0;2;30;3;10/1;1;-10;2;20/";
        String[] lines = content.split("/");
        for(String line:lines) {
            String[] s = line.split(";");
            //erste Stelle ist die PlayerID
            //Player p = gClient.getPlayerByID(Integer.getInteger(s[0]));
            //SH wird ganz geupdatet -> aktuelle Einträge löschen
            //p.clearSH();
            int saidStitches = 0;
            for (int i = 1;i<s.length; i++) {
                if(i%2 != 0) {//ungerade -> angesagte Stiche
                    saidStitches = Integer.getInteger(s[i]);
                }
                else {
                    System.out.println("Stitches: " + saidStitches + " Points: " + Integer.getInteger(s[i]));
                    //p.addToSH(saidStitches, Integer.getInteger(s[i]));
                }
            }
        }
    }

}
