import java.util.ArrayList;

public class Card {

    private String value;
    private Color color;

    public Card(String value, Color color) {
        this.value = value;
        this.color = color;
        String fileName = value+"_in_"+color;       //dient zur Zuweisung des richtigen Bildes zur Karte
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    //gibt eine Liste mit allen möglichen Kartenwerten zurück
    public static ArrayList<String> getValidValues()
    {
        ArrayList<String> values = new ArrayList<>();
        values.add("N");
        for(int i = 1; i<=13; i++)
        {
            values.add(Integer.toString(i));
        }
        values.add("Z");
        return values;
    }

    //gibt eine Liste mit allen möglichen Farben zurück
    public static ArrayList<Color> getValidColors()
    {
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        return colors;
    }
}
