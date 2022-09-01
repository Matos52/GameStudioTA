package sk.tuke.gamestudio.sudoku.core;

import sk.tuke.gamestudio.sudoku.consoleUI.ConsoleUISudoku;
import sk.tuke.gamestudio.sudoku.consoleUI.UserInterface;

public class Sudoku {
    private static Sudoku instance;

    //Vracia prave jednu instanciu singletona
    public static Sudoku getInstance() {
        if(instance == null) {
            instance = new Sudoku();
        }
        return instance;
    }

    //singleton - konstruktor musi byt private
    public Sudoku() {
        instance = this; //singleton
        final UserInterface userInterface = new ConsoleUISudoku();
        userInterface.play();
    }

    public static void main(String[] args) {
        getInstance();
    }
}
