import ea.Client;

public class CClient extends Client {

    private String name;
    private GUIClient gClient;

    public CClient(String ipAdress, GUIClient gClient) {
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

        String key = string.substring(0,2);
        String content = string.substring(3);
        String[] lines;
        String[] s;
        switch (key) {
            /*
             *  Broadcasts
             */
            case "SB":
                lines = content.split("/");
                for(String line:lines) {
                    s = line.split(";");
                    //erste Stelle ist die PlayerID
                    Player p = gClient.getPlayerByID(Integer.parseInt(s[0]));
                    //SH wird ganz geupdatet -> aktuelle Einträge löschen
                    p.clearSH();
                    int saidStitches = 0;
                    for (int i = 1;i<s.length; i++) {
                        if(i%2 != 0) {//ungerade -> angesagte Stiche
                            saidStitches = Integer.parseInt(s[i]);
                        }
                        else {
                            p.addToSH(saidStitches, Integer.parseInt(s[i]));
                        }
                    }
                }
                break;

            case "SS":
                lines = content.split("/");
                for(String line:lines) {
                    s = line.split(";");
                    Player p = gClient.getPlayerByID(Integer.parseInt(s[0]));
                    p.setSaidStitches(Integer.parseInt(s[1]));
                }
                break;

            case "CS":
                lines = content.split("/");
                for(String line:lines) {
                    s = line.split(";");
                    Player p = gClient.getPlayerByID(Integer.parseInt(s[0]));
                    p.setCurrentStitches(Integer.parseInt(s[1]));
                }
                break;

            case "AR":
                gClient.currentRound = Integer.parseInt(content);
                break;

            case "AS":
                lines = content.split("/");
                for(String line:lines) {
                    s = line.split(";");
                    gClient.stitch.add(new Card(Integer.parseInt(s[0]), ColorW.valueOf(s[1])));
                }
                break;

            case "AT":
                gClient.currentTrump = ColorW.valueOf(content);
                break;

            case "AP":
                gClient.currentPlayerID = Integer.parseInt(content);
                break;

            case "PN":
                s = content.split(";");
                gClient.showPlayerInList(s[0], Integer.parseInt(s[1]));
                break;

            case "GO":
                s = content.split(";");
                gClient.gameOver(s[0],Integer.parseInt(s[1]));
                break;

            case "DC":
                gClient.disconnected(content);
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

            case "HA":
                gClient.hand.clear();
                lines = content.split("/");
                for(String line:lines) {
                    s = line.split(";");
                    gClient.hand.add(new Card(Integer.parseInt(s[0]),ColorW.valueOf(s[1])));
                }
                break;

            case "HS":
                //ToDo @Julian wo werden die spielbaren Karten gespeichert
                gClient.hand.clear();
                lines = content.split("/");
                for(String line:lines) {
                    s = line.split(";");
                    gClient.hand.add(new Card(Integer.parseInt(s[0]),ColorW.valueOf(s[1])));
                }
                break;

            case "KS":
                Card c = gClient.requestCard();
                String ks = "GK/" + c.getValue() + ";" + c.getColor();
                super.sendeString(ks);
                break;

            default:
                break;

        }

        //ToDo @Julian in der Methode haben können sich Trumpf, Hand, spielbare Karten etc. ändern -> Gibt es eine update Methode, die hier ausgeführt werden soll?
    }


}
