package sk.tuke.gamestudio.kamene.core;

public class Settings {

    private final int rozmerStvorca;

    public static final Settings BEGINNER = new Settings(4);
    public static final Settings INTERMEDIATE = new Settings(6);
    public static final Settings EXPERT = new Settings(8);

    public Settings(int rowCount) {
        this.rozmerStvorca = rowCount;
    }

    public int getRozmerStvorca() {
        return rozmerStvorca;
    }
}
