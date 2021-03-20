import ea.*;

public class GUIClient extends Game implements MausReagierbar {
    private Maus maus;
    private Text joinButton;
    private Text backButton;

    public GUIClient()
    {
        super(500,500, "Wizard");

        maus = new Maus(

                new Bild(0,0, "./src/images/fadenkreuz.gif"),

                new Punkt(11, 11));
        mausAnmelden(maus);

        joinButton = new Text("Beitreten", 200, 200, 20);
        sichtbarMachen(joinButton);
        backButton = new Text("Zurück", 200, 300, 20);
        sichtbarMachen(backButton);
        backButton.sichtbarSetzen(false);
        maus.anmelden(this, joinButton, 0);
        maus.anmelden(this, backButton, 1);
    }

    @Override
    public void tasteReagieren(int i) {

    }

    public void sichtbarMachen(Raum m)
    {
        wurzel.add(m);
    }

    public String getInputName()
    {
        return eingabeFordern("Name eingeben:");
    }

    public String getInputIP()
    {
        return eingabeFordern("Server-IP-Adresse:");
    }

    @Override
    public void mausReagieren(int code)
    {
        System.out.println(code);
        switch (code)
        {
            case 0:
                joinButton.sichtbarSetzen(false);
                getInputName();
                getInputIP();
                backButton.sichtbarSetzen(true);
                break;
            case 1:
                joinButton.sichtbarSetzen(true);
                backButton.sichtbarSetzen(false);
                break;
            default:
                break;
        }
    }
}
