public class Main {
    public static void main(String[] args) {
        GUIClient gui = new GUIClient();
        gui.players.add(new Player("Tobias",0));
        gui.players.add(new Player("Julian",1));
        gui.players.get(0).getSh().add(new StitchHistory(2,30));
        gui.players.get(0).getSh().add(new StitchHistory(0,60));
        gui.players.get(1).getSh().add(new StitchHistory(3,100));
        gui.players.get(1).getSh().add(new StitchHistory(5,-10));
        gui.getEScoreboard();
        gui.setSichtbardEScoreboard(true);
    }
}
