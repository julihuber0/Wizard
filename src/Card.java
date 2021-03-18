import java.util.ArrayList;

public class Card {

    private Integer value;
    private Color color;

    public Card(Integer value, Color color) {
        this.value = value;
        this.color = color;
        String fileName = value+"_in_"+color;       //dient zur Zuweisung des richtigen Bildes zur Karte
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    //gibt eine Liste mit allen möglichen Kartenwerten zurück
    public static ArrayList<Integer> getValidValues()
    {
        ArrayList<Integer> values = new ArrayList<>();
        for(int i = 0; i<=14; i++)
        {
            values.add(i);
        }
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
