package model;

public class StitchHistory {

    public int stitches;
    public int points;

    public StitchHistory(int s, int p) {
        this.stitches = s;
        this.points = p;
    }

    public int getStitches() {
        return stitches;
    }

    public void setStitches(int stitches) {
        this.stitches = stitches;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
