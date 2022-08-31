package sk.tuke.gamestudio.kamene.core;

import org.apache.catalina.User;
import sk.tuke.gamestudio.kamene.consoleUI.ConsoleUIKamene;
import sk.tuke.gamestudio.kamene.consoleUI.UserInterface;

public class Kamene {

    private UserInterface userInterface;
    private static Kamene instance;

    //Vracia prave jednu instanciu singletona
    public static Kamene getInstance() {
        if(instance == null) {
            instance = new Kamene();
        }
        return instance;
    }

    //singleton - konstruktor musi byt private
    private Kamene() {
        instance = this; //singleton
        final UserInterface userInterface = new ConsoleUIKamene();
        userInterface.play();
    }

    public static void main(String[] args) {

        getInstance();
    }
}
