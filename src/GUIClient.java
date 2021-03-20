import ea.*;

import java.util.ArrayList;

public class GUIClient extends Game implements MausReagierbar {
    private Maus maus;
    private Text joinButton;
    private Text playButton;
    private Text backButton;
    private Bild bg;
    private Bild logo;
    private Bild[] s = new Bild[5];
    private Text[] names = new Text[5];
    private Text[] saidStitches = new Text[5];
    private Text[] points = new Text[5];
    private Bild[] stitchImage = new Bild[6];
    private Bild[] ownHand = new Bild[20];
    private Text l;

    private CClient cClient;

    //Nur zur Datenhaltung
    public ArrayList<Player> players = new ArrayList<>(); //currentPoints, hand, clienthandler sind nicht ausgefüllt
    public ArrayList<Card> hand = new ArrayList<>();    //Karten auf der Hand
    public ArrayList<Card> stitch = new ArrayList<>(); //Karten, die aktuell auf dem Tisch liegen
    public ColorW currentTrump = null;      //aktuelle Trumpffarbe
    public int currentRound = 0;       //aktuelle Runde
    public int idCurrentPlayer = 0; //ID des Spielers, der gerade an der Reihe ist
    public int idSelf = 0; //ID des Spielers, der man selbst ist


    public GUIClient()
    {
        super(1200,650, "Wizard");

        maus = new Maus(new Bild(0,0, "./src/images/pointer.png"), new Punkt(0, 0));
        mausAnmelden(maus);

        bg = new Bild(0,0,"./src/images/BG2.jpg");
        sichtbarMachen(bg);

        //Main Menu
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
        logo = new Bild(700, 100, "./src/images/wizardgame.png");
        sichtbarMachen(logo);

        //Game-GUI
        l = new Text("Karte legen", 10, 350, 20);
        sichtbarMachen(l);
        l.sichtbarSetzen(false);
        maus.anmelden(this, l, 3);

        //TODO: Schleifenbedingungen an Spielerzahl etc. anpassen
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

        //Angesagte Stiche anzeigen
        for(int i = 0 ; i<5; i++)
        {
            saidStitches[i] = new Text("Angesagt: ", 110+i*200, 200, 15);
            sichtbarMachen(saidStitches[i]);
            saidStitches[i].sichtbarSetzen(false);
        }

        //Punkte anzeigen
        for(int i = 0; i<5; i++)
        {
            points[i] = new Text("Punkte: ", 110+i*200, 220, 15);
            sichtbarMachen(points[i]);
            points[i].sichtbarSetzen(false);
        }
    }

    public void showOwnHand()
    {
        for(int i = 0; i<currentRound; i++)
        {
            ownHand[i] = new Bild(50, 500, "./src/images/"+hand.get(i).getValue()+"_in_"+hand.get(i).getColor());
            sichtbarMachen(ownHand[i]);
            ownHand[i].sichtbarSetzen(true);
            maus.anmelden(this, ownHand[i], 100+i);
        }
    }

    public String askForTrumpColor()
    {
        return eingabeFordern("Gewünschte Trumpffarbe eingeben (grün, blau, rot, gelb)");
    }

    public ColorW validateTrump()
    {
        String trumpColor = askForTrumpColor();
        if(trumpColor.equals("grün"))
        {
            return ColorW.GREEN;
        }
        if(trumpColor.equals("blau"))
        {
            return ColorW.BLUE;
        }
        if(trumpColor.equals("rot"))
        {
            return ColorW.RED;
        }
        if(trumpColor.equals("gelb"))
        {
            return ColorW.YELLOW;
        }
        return validateTrump();
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

    public void karteLegen()
    {
        for(int i = 0; i<6; i++)
        {
            if(stitchImage[i]==null)
            {
                System.out.println("1");
                stitchImage[i] = new Bild(200+i*50, 300, "./src/images/king.png");
                sichtbarMachen(stitchImage[i]);
                break;
            }
        }
    }

    @Override
    public void mausReagieren(int code)
    {
        switch (code)
        {
            case 0:
                getInputIP();
                joinButton.sichtbarSetzen(false);
                backButton.sichtbarSetzen(true);
                logo.sichtbarSetzen(false);
                break;
            case 1:
                joinButton.sichtbarSetzen(true);
                backButton.sichtbarSetzen(false);
                logo.sichtbarSetzen(true);
                break;
            case 2:
                joinButton.sichtbarSetzen(false);
                playButton.sichtbarSetzen(false);
                logo.sichtbarSetzen(false);
                for(int i = 0; i<5; i++)
                {
                    s[i].sichtbarSetzen(true);
                }
                for(int i = 0; i<5; i++)
                {
                    names[i].sichtbarSetzen(true);
                }
                for(int i = 0; i<5; i++)
                {
                    saidStitches[i].sichtbarSetzen(true);
                }
                for(int i = 0; i<5; i++)
                {
                    points[i].sichtbarSetzen(true);
                }
                l.sichtbarSetzen(true);
                break;
            case 3:
                karteLegen();
                break;
            default:
                break;
        }
    }
}
