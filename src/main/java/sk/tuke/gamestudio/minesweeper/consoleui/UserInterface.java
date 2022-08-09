package sk.tuke.gamestudio.minesweeper.consoleui;

import sk.tuke.gamestudio.minesweeper.core.Field;

public interface UserInterface {
    void newGameStarted(Field field);

    void update();

    void play();
}