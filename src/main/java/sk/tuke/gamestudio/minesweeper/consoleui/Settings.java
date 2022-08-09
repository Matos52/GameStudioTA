package sk.tuke.gamestudio.minesweeper.consoleui;

import java.io.*;

public class Settings implements Serializable {

    private final int rowCount;
    private final int columnCount;
    private final int mineCount;

    public static final Settings BEGINNER = new Settings(9,9,10);
    public static final Settings INTERMEDIATE = new Settings(16,16,40);
    public static final Settings EXPERT = new Settings(16,30,99);

    private static final String SETTING_FILE = System.getProperty("user.home") + System.getProperty("file.separator") + "minesweeper.settings";


    public Settings(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
    }

    public void save() {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(SETTING_FILE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
        } catch (IOException e) {
            System.out.println("Nepodarilo sa zapisat settings do objektu");
        } finally {
            if(oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    //empty naschval
                }
            }
        }
    }

    public static Settings load() {
        ObjectInputStream ois = null;

        try {
            FileInputStream fis = new FileInputStream(SETTING_FILE);
            ois = new ObjectInputStream(fis);
            Settings s = (Settings) ois.readObject();
            return s;
        } catch (IOException e) {
            System.out.println("Nepodarilo sa otvorit settings subor, pouzivam default BEGINNER");
        } catch (ClassNotFoundException e) {
            System.out.println("Nepodarilo sa precitat settings, pouzivam default BEGINNER");
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    //empty naschval
                }
            }
        }
        return BEGINNER;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Settings)) {
            return false;
        }
        Settings s = (Settings) obj;
        return s.rowCount == this.rowCount && s.columnCount == this.columnCount && s.mineCount == this.columnCount;
    }

    @Override
    public int hashCode() {
        return this.rowCount * this.columnCount * this.mineCount;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "rowCount=" + rowCount +
                ", columnCount=" + columnCount +
                ", mineCount=" + mineCount +
                '}';
    }
}
