package sk.tuke.gamestudio.kamene.core;

import sk.tuke.gamestudio.kamene.consoleUI.ConsoleUIKamene;
import sk.tuke.gamestudio.kamene.consoleUI.UserInterface;

public class Kamene {

    private UserInterface userInterface;
    private static Kamene instance;

    private Settings setting; //nastavenie obtiaznosti

    public Kamene() {
        instance = this;
        final ConsoleUIKamene userInterface = new ConsoleUIKamene();
        userInterface.play();
    }

    public static void main(String[] args) {

//        System.out.println("Hello " + System.getProperty("user.name") + " !");
        getInstance();
    }

    public static Kamene getInstance() {
        if(instance == null) {
            instance = new Kamene();
        }
        return Kamene.getInstance();
    }
}
