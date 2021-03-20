import ea.Client;

public class CClient extends Client {

    private String name;

    public CClient(String ipAdress, String name) {
        //Verbinde dich mit der übergebenen IP-Adresse und Port 7654.
        //Port 7654 ist willkürlich gewählt. Wichtig ist,
        //  - dass die Portnummer größer als 1024 ist.
        //  - dass der Client dieselbe Portnummer hat.
        super(ipAdress, 7654);
        this.name = name;

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
            case "NF":
                //Namen zurücksenden
                String send = "NA|" + name;
                super.sendeString(send);
                break;

            default:
                break;

        }

    }


}
