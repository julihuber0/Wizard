import ea.*;

public class GUIClient extends Game implements KlickReagierbar {
    private Maus maus;

    public GUIClient()
    {
        super(500,500, "Wizard");

        maus = new Maus(

                new Bild(0,0, "./src/images/fadenkreuz.gif"),

                new Punkt(11, 11));
        mausAnmelden(maus);

        maus.klickReagierbarAnmelden(this);
    }

    @Override
    public void tasteReagieren(int i) {

    }

    @Override
    public void klickReagieren(Punkt punkt) {

        //Erstelle ein neues Rechteck (Maße: 30 x 50 px). Die Position ist hier egal.
        Rechteck rechteck = new Rechteck(0,0, 30, 50);
        //setze die Farbe auf Rot
        rechteck.farbeSetzen("Rot");

        //setze den Mittelpunkt des Kreises auf den Klick-Punkt
        rechteck.mittelpunktSetzen(punkt);

        //Mache das Rechteck sichtbar: An Wurzel anmelden.
        wurzel.add(rechteck);
    }
}
