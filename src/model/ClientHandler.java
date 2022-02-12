package model;

import ea.*;

import java.util.ArrayList;
import java.util.Collections;

public class ClientHandler implements Empfaenger {
    /**
     * Die Netzwerkverbindung, über die der Handler nachrichten senden und empfangen kann.
     */
    private NetzwerkVerbindung verbindung;
    private Thread t;

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

    public void sendID() {
        sendeString("ID/" + id);
    }

    public void sendeString(String s) {
        verbindung.sendeString(s);
    }

    public void setT(Thread thread) {
        this.t = thread;
    }

    /* --- Empfaenger-Methoden --- */

    @Override
    public synchronized void empfangeString (String string) {
        System.out.println("[Server hat empfangen:] " + string);

        String key = string.substring(0,2);
        String content = string.substring(3);
        switch (key) {
            case "NA":
                //Namen setzen
                if(content.contains("/") || content.contains(";") || content.isEmpty()) {
                    sendeString("NF/");
                }
                else {
                    server.addPlayer(content,id);
                }
                break;

            case "GK":
                String[] s = content.split(";");
                player.played = new Card(Integer.parseInt(s[0]),ColorW.toEnum(s[1]));
                t.interrupt();
                break;

            case "TA":
                player.selectedTrump = ColorW.toEnum(content);
                t.interrupt();
                break;

            case "SA":
                player.saidStitches = Integer.parseInt(content);
                t.interrupt();
                break;
            case "CS":
                server.sendString("CN/" + content);
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

    //Nur um schnell was zu testen, falls nötig
    public static void main(String[] args) {
        //EngineAlpha.main(null);
        ArrayList<Card> test = new ArrayList<>();

        test.add(new Card(3, ColorW.YELLOW));
        test.add(new Card(9, ColorW.GREEN));
        test.add(new Card(11, ColorW.BLUE));
        test.add(new Card(11,ColorW.GREEN));
        test.add(new Card(0, ColorW.RED));
        test.add(new Card(12,ColorW.GREEN));
        test.add(new Card(13,ColorW.BLUE));
        test.add(new Card(14, ColorW.GREEN));
        test.add(new Card(7,ColorW.BLUE));
        test.add(new Card(8,ColorW.BLUE));
        test.add(new Card(14, ColorW.YELLOW));

        ArrayList<Card> clone = new ArrayList<>(test);
        Collections.sort(test);

        ArrayList<Card> sorted = new ArrayList<>(GameW.sortCardArray(clone));

        for(Card c:test) {
            System.out.println(c.value + " in " + c.colorW);
        }
        System.out.println("---------------------------------------------------");
        for(Card c:sorted) {
            System.out.println(c.value + " in " + c.colorW);
        }

    }

}
