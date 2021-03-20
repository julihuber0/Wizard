import ea.*;

import java.awt.Color;

public class GUIClient extends Game implements MausReagierbar {
    private Maus maus;
    private Text joinButton;
    private Text playButton;
    private Text backButton;
    private Rechteck table;
    public Bild bg;

    public GUIClient()
    {
        super(1200,650, "Wizard");

        maus = new Maus(new Bild(0,0, "./src/images/fadenkreuz.gif"), new Punkt(11, 11));
        mausAnmelden(maus);

        //bg = new Bild(0,0,"./src/images/BG.jpg");
        //sichtbarMachen(bg);

        joinButton = new Text("Beitreten", 200, 200, 20);
        sichtbarMachen(joinButton);
        backButton = new Text("Zurück", 200, 300, 20);
        sichtbarMachen(backButton);
        backButton.sichtbarSetzen(false);
        maus.anmelden(this, joinButton, 0);
        maus.anmelden(this, backButton, 1);
        playButton = new Text("Start", 200, 250, 20);
        sichtbarMachen(playButton);
        maus.anmelden(this, playButton, 2);

        //Game-GUI
        table = new Rechteck(200, 200, 200, 200);
        table.farbeSetzen(Color.GRAY);
        sichtbarMachen(table);
        table.sichtbarSetzen(false);
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
                getInputIP();
                joinButton.sichtbarSetzen(false);
                backButton.sichtbarSetzen(true);
                break;
            case 1:
                joinButton.sichtbarSetzen(true);
                backButton.sichtbarSetzen(false);
                break;
            case 2:
                joinButton.sichtbarSetzen(false);
                playButton.sichtbarSetzen(false);
                table.sichtbarSetzen(true);
            default:
                break;
        }
    }
}
