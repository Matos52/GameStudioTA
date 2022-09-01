package sk.tuke.gamestudio.sudoku.consoleUI;

import sk.tuke.gamestudio.sudoku.core.Field;

public interface UserInterface {

    void newGameStarted(Field field);

    void update();

    void play();
}
