public enum ColorW {
    GREEN, BLUE, RED, YELLOW;

    public ColorW toEnum(String s) {
        if(s.equals("GREEN")) {
            return ColorW.GREEN;
        }
        if(s.equals("BLUE")) {
            return ColorW.BLUE;
        }
        if(s.equals("RED")) {
            return ColorW.RED;
        }
        else {
            return ColorW.YELLOW;
        }
    }
}
