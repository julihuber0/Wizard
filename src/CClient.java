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
        switch (key) {
            /*
             *  Broadcasts
             */
            case "SB":
                lines = content.split("/");
                for(String line:lines) {
                    String[] s = line.split(";");
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
                    String[] s = line.split(";");
                    Player p = gClient.getPlayerByID(Integer.parseInt(s[0]));
                    p.setSaidStitches(Integer.parseInt(s[1]));
                }
                break;

            case "CS":
                lines = content.split("/");
                for(String line:lines) {
                    String[] s = line.split(";");
                    Player p = gClient.getPlayerByID(Integer.parseInt(s[0]));
                    p.setCurrentStitches(Integer.parseInt(s[1]));
                }
                break;

            case "AR":
                gClient.currentRound = Integer.parseInt(content);
                break;

            case "AS":

                break;

            case "AT":

                break;

            case "GO":

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
                //ToDo zwei Fälle
                break;

            case "HS":

                break;
            default:
                break;

        }

    }


}
