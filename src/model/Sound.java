package model;

public enum Sound {
    WARUM, ZEFIX, SCHNELLER;

    public static String toString(Sound s) {
        if(s==WARUM) {
            return "Warum?";
        }
        if(s==ZEFIX) {
            return "Zefix!";
        }
        return "Schneller!";
    }
}
