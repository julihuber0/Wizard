import ea.*;

import java.awt.Color;
import java.util.ArrayList;

public class GUIClient extends Game implements MausReagierbar {
    private Maus maus;
    private Text joinButton;
    private Text playButton;
    private Text backButton;
    private Bild bg;
    private Bild[] s = new Bild[5];
    private Text[] names = new Text[5];
    private Bild[] stitchImage = new Bild[5];

    private CClient cClient;

    //Nur zur Datenhaltung
    public ArrayList<Player> players = new ArrayList<>(); //currentPoints, hand, clienthandler sind nicht ausgefüllt
    public ArrayList<Card> hand = new ArrayList<>();    //Karten auf der Hand
    public ArrayList<Card> stitch = new ArrayList<>(); //Karten, die aktuell auf dem Tisch liegen
    public Color currentTrump = null;      //aktuelle Trumpffarbe
    public int currentRound = 0;       //aktuelle Runde
    public int idCurrentPlayer = 0; //ID des Spielers, der gerade an der Reihe ist
    public int idSelf = 0; //ID des Spielers, der man selbst ist


    public GUIClient()
    {
        super(1200,650, "Wizard");

        maus = new Maus(new Bild(0,0, "./src/images/fadenkreuz.gif"), new Punkt(11, 11));
        mausAnmelden(maus);

        bg = new Bild(0,0,"./src/images/BG2.jpg");
        sichtbarMachen(bg);

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


        //Avatare erzeugen
        for(int i = 0; i<5; i++)
        {
            s[i] = new Bild(100+i*200, 20, "./src/images/avatar.png");
            sichtbarMachen(s[i]);
            s[i].sichtbarSetzen(false);
        }

        //Namen erzeugen
        for(int i = 0; i<5; i++)
        {
            names[i] = new Text("Spieler"+(i+2), 110+i*200, 180, 15);
            sichtbarMachen(names[i]);
            names[i].sichtbarSetzen(false);
        }
    }

    public Player getPlayerByID(int id)
    {
        for(Player p:players)
        {
            if(p.getId()==id)
            {
                return p;
            }
        }
        return null;
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
                for(int i = 0; i<5; i++)
                {
                    s[i].sichtbarSetzen(true);
                }
                for(int i = 0; i<5; i++)
                {
                    names[i].sichtbarSetzen(true);
                }
            default:
                break;
        }
    }
}
