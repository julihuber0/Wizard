
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

    public ClientHandler(NetzwerkVerbindung verbindung) {
        this.verbindung = verbindung;

        //Dieses Objekt als Empfänger an der Verbindung melden
        verbindung.empfaengerHinzufuegen(this);
    }


    public void sendeString(String s) {
        verbindung.sendeString(s);
    }

    /* --- Empfaenger-Methoden --- */

    @Override
    public void empfangeString (String string) {
        System.out.println("[Server hat empfangen:] " + string);
        if(string.startsWith("Hallo, ich bin ")) {
            //Client stellt sich vor => Namen übernehmen

            //Der neue Clientname ist das, was nach "Hallo, ich bin " kommt.
            this.clientname = string.substring(15);

            //Zurückgrüßen
            verbindung.sendeString("Hallo " + clientname
                    + ", schön dich kennenzulernen. Ich kümmere mich heute nur um dich.");
        }
        if(string.startsWith("Was ist die Antwort?")) {
            //Der Client hat eine Frage. Besser schnell was ausdenken.
            verbindung.sendeString("Nun " + clientname +", ich schätze die Antwort ist 42.");
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

}
