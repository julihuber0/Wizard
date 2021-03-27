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
}
