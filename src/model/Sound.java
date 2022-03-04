package model;

public enum Sound {
    WHY, SWEAR, FASTER;

    public static String toString(Sound s) {
        if(s==WHY) {
            return "Warum?";
        }
        if(s==SWEAR) {
            return "$#@%!";
        }
        return "Schneller!";
    }
}
