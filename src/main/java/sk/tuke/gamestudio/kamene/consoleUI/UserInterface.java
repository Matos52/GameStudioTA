package sk.tuke.gamestudio.kamene.consoleUI;


import sk.tuke.gamestudio.kamene.core.Field;

public interface UserInterface {

    void newGameStarted(Field field);

    void update();

    void play();
}
