package model;

import ea.Client;
import view.GUINew;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class CClient extends Client {

    private String name;
    private GUINew gClient;

    public CClient(String ipAdress, GUINew gClient) {
        //Verbinde dich mit der übergebenen IP-Adresse und Port 7654.
        //Port 7654 ist willkürlich gewählt. Wichtig ist,
        //  - dass die Portnummer größer als 1024 ist.
        //  - dass der Client dieselbe Portnummer hat.
        super(ipAdress, 7654);
        this.gClient = gClient;

        //Warten, bis die Verbindung zum Server steht:
        //   Achtung: Wenn kein Server gefunden werden kann,
        //            friert dieser Konstruktor ein!
        warteAufVerbindung();

    }

    @Override
    public void empfangeString(String string) {
        //empfangenen String einfach an der Konsole ausgeben
        System.out.println("[Client hat empfangen:] " + string);

        String key = string.substring(0, 2);
        String content = string.substring(3);
        String[] lines;
        String[] s;
        switch (key) {
            /*
             *  Broadcasts
             */
            case "SB":
                lines = content.split("/");
                for (String line : lines) {
                    s = line.split(";");
                    //erste Stelle ist die PlayerID
                    Player p = gClient.getPlayerByID(Integer.parseInt(s[0]));
                    //SH wird ganz geupdatet -> aktuelle Einträge löschen
                    p.clearSH();
                    int saidStitches = 0;
                    for (int i = 1; i < s.length; i++) {
                        if (i % 2 != 0) {//ungerade -> angesagte Stiche
                            saidStitches = Integer.parseInt(s[i]);
                        } else {
                            p.addToSH(saidStitches, Integer.parseInt(s[i]));
                        }
                    }
                }
                gClient.updatePoints();
                break;

            case "SS":
                lines = content.split("/");
                for (String line : lines) {
                    s = line.split(";");
                    Player p = gClient.getPlayerByID(Integer.parseInt(s[0]));
                    p.setSaidStitches(Integer.parseInt(s[1]));
                }
                gClient.updateSaidStitches();
                break;

            case "CS":
                lines = content.split("/");
                for (String line : lines) {
                    s = line.split(";");
                    Player p = gClient.getPlayerByID(Integer.parseInt(s[0]));
                    p.setCurrentStitches(Integer.parseInt(s[1]));
                }
                gClient.updateMadeStitches();
                break;

            case "AR":
                gClient.currentRound = Integer.parseInt(content);
                break;

            case "AS":
                gClient.stitch.clear();
                lines = content.split("/");
                for (String line : lines) {
                    if (!line.isEmpty()) {
                        s = line.split(";");
                        gClient.stitch.add(new Card(Integer.parseInt(s[0]), ColorW.toEnum(s[1])));
                    }
                }
                gClient.updateStitch();
                break;

            case "AT":
                s = content.split(";");
                if(!content.equals("")) {
                    gClient.currentTrumpCard = new Card(Integer.parseInt(s[0]), ColorW.toEnum(s[1]));
                    gClient.currentTrump = ColorW.toEnum(s[1]);
                    gClient.updateTrump();
                }
                else {
                    gClient.resetTrump();
                }
                break;

            case "TC":
                    gClient.setTrumpColor(ColorW.toEnum(content));
                break;

            case "AP":
                gClient.currentPlayerID = Integer.parseInt(content);
                gClient.updateCurrentPlayerMarker();
                break;

            case "CP":
                gClient.players.clear();
                break;

            case "PN":
                s = content.split(";");
                //gClient.showPlayerInList(s[0], Integer.parseInt(s[1]));
                //gClient.players.add(new Player(s[0], Integer.parseInt(s[1])));
                //gClient.updateNames();
                gClient.addPlayer(new Player(s[0], Integer.parseInt(s[1])));
                break;

            case "GO":
                s = content.split(";");
                gClient.gameOver(s[0], Integer.parseInt(s[1]));
                break;

            case "DC":
                gClient.disconnected(content);
                break;

            case "SG":
                gClient.startGame();
                break;

            case "NR":
                lines = content.split("/");
                for (String line : lines) {
                    s = line.split(";");
                    //erste Stelle ist die PlayerID
                    Player p = gClient.getPlayerByID(Integer.parseInt(s[0]));
                    p.setPoints(Integer.parseInt(s[1]));
                }

                gClient.hand.clear();
                gClient.resetStats();
                //gClient.resetLastStitch();
                gClient.stitch.clear();
                //gClient.clearStitchImage();
                gClient.updateStitch();
                gClient.updateStitchSum(-1);
                break;
            case "TP":
                gClient.stitchMarker(Integer.parseInt(content));
                break;
            case "RC":
                gClient.updateRoundCounter();
                break;
            case "SC":
                gClient.updateStitchSum(Integer.parseInt(content));
                break;
            case "MP":
                gClient.markPlayers();
                break;

            case "LS":
                //gClient.updateLastStitch(content);
                break;
            case "RP":
                gClient.resetPlayableCards();
                break;

            case "CN":
                System.out.println("Encoding:" + Charset.defaultCharset());
                //playPingSound();
                gClient.addMessage(content);
                break;

            case "SP":
                gClient.playSound(content);
                //executeSound(content);
                break;

            /*
             *  Clientspezifisches
             */
            case "NF":
                //Eingabe Namen auffordern
                name = gClient.getInputName();
                //Namen zurücksenden
                String send = "NA/" + name;
                super.sendeString(send);
                break;

            case "ID":
                gClient.idSelf = Integer.parseInt(content);
                break;

            case "HA": //Hand des Spielers

                gClient.hand.clear();
                lines = content.split("/");
                for (String line : lines) {
                    if (!line.isEmpty()) {
                        s = line.split(";");
                        gClient.hand.add(new Card(Integer.parseInt(s[0]), ColorW.toEnum(s[1])));
                    }
                }
                gClient.showOwnHand();
                break;

            case "HS": // Spielbare Karten auf der Hand des Spielers
                lines = content.split("/");
                ArrayList<Card> cards = new ArrayList<>();
                for (String line : lines) {
                    if (!line.isEmpty()) {
                        s = line.split(";");
                        cards.add(new Card(Integer.parseInt(s[0]), ColorW.toEnum(s[1])));
                    }
                }
                gClient.setPlayableCards(cards);
                break;

            case "KS":
                //springt er rein, wenn er eine Karte spielen muss
                gClient.requestCard();
                break;

            case "TF":
                Thread askTrump = new Thread(() -> {
                    String ta = "TA/" + gClient.validateTrump();
                    sendeString(ta);
                });
                askTrump.start();
                //String ta = "TA/" + gClient.validateTrump();
                //sendeString(ta);
                break;

            case "SF":
                Thread askStitches = new Thread(() -> {
                    String sa = "SA/" + gClient.validateStitches(Integer.parseInt(content));
                    sendeString(sa);
                });
                askStitches.start();
                //String sa = "SA/" + gClient.validateStitches(Integer.parseInt(content));
                //sendeString(sa);
                break;

            default:
                break;

        }
    }

    public void playCard(Card c) {
        String ks = "GK/" + c.getValue() + ";" + c.getColor();
        super.sendeString(ks);
    }

    public void sendChatMessage(String message) {
        sendeString("CS/" + "<html><strong><i>" + gClient.getDisplayName() + ": </i></strong>" + message + "</html>");
    }

    public void playSound(Sound s) {
        sendChatMessage(Sound.toString(s)+Sound.getPunctation(s));
        sendeString("PS/"+Sound.toString(s)+".wav");
    }

}
