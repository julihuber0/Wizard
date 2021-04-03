import java.util.ArrayList;

public class Card implements Comparable<Card> {

    public Integer value;
    public ColorW colorW;

    public Card(Integer value, ColorW colorW) {
        this.value = value;
        this.colorW = colorW;
        String fileName = value + "_in_" + colorW;       //dient zur Zuweisung des richtigen Bildes zur Karte
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public ColorW getColor() {
        return colorW;
    }

    public void setColor(ColorW colorW) {
        this.colorW = colorW;
    }

    //gibt eine Liste mit allen möglichen Kartenwerten zurück
    public static ArrayList<Integer> getValidValues() {
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i <= 14; i++) {
            values.add(i);
        }
        return values;
    }

    //gibt eine Liste mit allen möglichen Farben zurück
    public static ArrayList<ColorW> getValidColors() {
        ArrayList<ColorW> colorWS = new ArrayList<>();
        colorWS.add(ColorW.BLUE);
        colorWS.add(ColorW.RED);
        colorWS.add(ColorW.GREEN);
        colorWS.add(ColorW.YELLOW);
        return colorWS;
    }

    public int getColorOrder() {
        if (this.colorW == ColorW.GREEN) {
            return 1;
        }
        if (this.colorW == ColorW.BLUE) {
            return 2;
        }
        if (this.colorW == ColorW.RED) {
            return 3;
        }
        return 4;
    }

    //Gibt die entsprechenden Sortierregeln für Karten vor
    public int compareTo(Card c) {
        if (c.getValue() == 14 && this.getValue() == 14) {
            if(c.getColorOrder()>this.getColorOrder())
            {
                return 1;
            }
            else if(c.getColorOrder()<this.getColorOrder())
            {
                return -1;
            }
            return 0;
        }
        if(c.getValue() == 14 && this.getValue()!=14)
        {
            return 1;
        }
        if(c.getValue()==0 && this.getValue()==0)
        {
            if(c.getColorOrder()>this.getColorOrder())
            {
                return 1;
            }
            else if(c.getColorOrder()<this.getColorOrder())
            {
                return -1;
            }
            else return 0;
        }
        if (c.getValue() == 0 && this.getValue()!=0) {
            return -1;
        }
        if (c.getValue() > this.getValue() && c.getColorOrder() == this.getColorOrder()) {
            return 1;
        }
        if (c.getValue() < this.getValue() && c.getColorOrder() == this.getColorOrder()) {
            return -1;
        }
        if (c.getColorOrder() < this.getColorOrder()) {
            return -1;
        }
        if (c.getColorOrder() > this.getColorOrder()) {
            return 1;
        }
        if(c.getValue() == this.getValue() && c.getColorOrder() == this.getColorOrder())
        {
            return 0;
        }
        return -1;
    }

    public boolean isEqual(Card c) {
        if (c.getValue().equals(this.getValue()) && c.getColor() == this.getColor()) {
            return true;
        }
        return false;
    }
}
