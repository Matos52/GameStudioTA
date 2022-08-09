package sk.tuke.gamestudio.minesweeper;

import sk.tuke.gamestudio.minesweeper.consoleui.ConsoleUI;
import sk.tuke.gamestudio.minesweeper.consoleui.UserInterface;


/**
 * Main application class.
 */
public class Minesweeper {
    /** User interface. */

    private static Minesweeper instance;

    //vracia prave jednu instanciu singletona
    public static Minesweeper getInstance() {
        if(instance == null) {
            new Minesweeper();
        }
        return instance;
    }

    /**
     * Constructor.
     */
    //singleton - konstruktor musi byt private
    private Minesweeper() {
        instance = this; //singleton

        final UserInterface userInterface = new ConsoleUI();
        userInterface.play();
    }

    /**
     * Main method.
     * @param args arguments
     */
    public static void main(String[] args) {
        Minesweeper.getInstance();
    }
}
