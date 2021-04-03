public enum ColorW {
    GREEN, BLUE, RED, YELLOW;

    public static ColorW toEnum(String s) {
        if(s.equals("GREEN")) {
            return ColorW.GREEN;
        }
        if(s.equals("BLUE")) {
            return ColorW.BLUE;
        }
        if(s.equals("RED")) {
            return ColorW.RED;
        }
        if(s.equals("YELLOW"))
        {
            return ColorW.YELLOW;
        }

        else {
            return null;
        }
    }

    public static String toString(ColorW c)
    {
        if(c == ColorW.BLUE)
        {
            return "Blau";
        }
        if(c == ColorW.GREEN)
        {
            return "Grün";
        }
        if(c == ColorW.RED)
        {
            return "Rot";
        }
        if(c == ColorW.YELLOW)
        {
            return "Gelb";
        }
        else {return null;}
    }
}
