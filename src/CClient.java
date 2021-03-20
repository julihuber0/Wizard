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
        switch (key) {
            case "CP":

                break;

            case "SB":

                break;

            case "SS":

                break;

            case "CS":

                break;

            case "AR":

                break;

            case "AS":

                break;

            case "AT":

                break;

            case "GO":

                break;

            case "NF":
                //Eingabe Namen auffordern
                name = gClient.getInputName();
                //Namen zurücksenden
                String send = "NA|" + name;
                super.sendeString(send);
                break;

            default:
                break;

        }

    }


}
