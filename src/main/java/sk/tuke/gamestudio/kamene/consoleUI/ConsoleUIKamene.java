package sk.tuke.gamestudio.kamene.consoleUI;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.*;
import sk.tuke.gamestudio.kamene.core.Field;
import sk.tuke.gamestudio.kamene.core.GameState;
import sk.tuke.gamestudio.kamene.core.Kamene;
import sk.tuke.gamestudio.kamene.core.Settings;
import sk.tuke.gamestudio.service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ConsoleUIKamene implements UserInterface {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private OccupationService occupationService;

    @Autowired
    private PlayerService playerService;

    private String userName ="";

    private static final String GAME = "kamene";

    Field field;

    Settings settings;

    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    private final ArrayList<String> listOfCommands = new ArrayList<>(
            Arrays.asList("new", "exit", "w", "s", "a", "d", "up", "down", "right", "left"));

    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void play() {
        this.field = new Field(4,4);
        newGameStarted(this.field);
    }

    @Override
    public void newGameStarted(Field field) {
        this.field = field;
        System.out.println("Zadaj svoje meno:");
        userName = readLine();

        //ak najde meno, vypise vsetko info
        printAllInformationFromUserName();
        //vypise vsetkych hracov
        printAllPlayers();
        //moznost vyberu mena
        playerChoiceOfName();

        int gameScore=0;

        do {
            update();
            processInput();

            if(field.getGameState() == GameState.SOLVED) {
                System.out.println("Vyhral si hru");
                if(wantNewRound()) {
                    System.out.println("Vytvaram nove kolo");
                    newGameStarted(new Field(4, 4));
                } else {
                    System.out.println("Ukoncujem hru");
                    gameScore=this.field.getScore();
                }
                break;
            }

            if(field.getGameState() == GameState.EXIT) {
                System.out.println("Koniec hry");
                break;
            }

            if(field.getGameState() == GameState.NEW_GAME) {
                System.out.println("Vytvaram ti novu hru");
                newGameStarted(new Field(settings.getRozmerStvorca(),
                        settings.getRozmerStvorca()));
                break;
            }

        } while(true);
        handlerComment();
        handlerRating();
        score(gameScore);
        System.exit(0);
    }

    public void printAllPlayers() {
        try {
            List<Player> players = playerService.getAllPlayers();
            for (int i = 0; i < players.size(); i++) {
                System.out.printf("("+ (i) +") -> %3s\n", players.get(i).getUserName());
            }
        } catch (Exception e) {
            System.err.println("Problem with reading from the database.\n" + e.getMessage());
        }
    }

    public void printAllOccupations() {
        try {
            List<Occupation> occupations = occupationService.getOccupations();
            System.out.println("Vyber si z nasledujuceho zoznamu povolani: ");
            for (int i = 0; i < occupations.size(); i++) {
                System.out.printf("("+ (i) +") -> %3s\n", occupations.get(i).getOccupation());
            }
        } catch (Exception e) {
            System.err.println("Problem with reading from the database.\n" + e.getMessage());
        }
    }

    public void printAllCountries() {
        try {
            List<Country> countries = countryService.getCountries();
            System.out.println("Vyber si z nasledujuceho zoznamu krajin: ");
            for (int i = 0; i < countries.size(); i++) {
                System.out.printf("("+ (i) +") -> %3s\n", countries.get(i).getCountry());
            }
        } catch (Exception e) {
            System.err.println("Problem with reading from the database.\n" + e.getMessage());
        }
    }

    public void printAllInformationFromUserName() {
        try {
            List<Player> players = playerService.getPlayersByUserName(userName);
            System.out.println("Informacie o hracovi: ");
            for (int i = 0; i < players.size(); i++) {
                System.out.printf("Meno - %2s, Priezvisko - %2s, Hodnotenie - %2d, Bydlisko - %2s, Povolanie - %2s\n",
                        players.get(i).getUserName(),
                        players.get(i).getFullName(),
                        players.get(i).getSelfEvaluation(),
                        players.get(i).getCountry().getCountry(),
                        players.get(i).getOccupation().getOccupation());
            }
        } catch (Exception e) {
            System.err.println("Problem with reading from the database.\n" + e.getMessage());
        }
    }

    public void countryAdding() {
        try {
            System.out.println("Zadaj nazov krajiny ktoru chces pridat: ");
            String nameOfCountry = readLine();
            countryService.addCountry(new Country(nameOfCountry));
        } catch (Exception e) {
            System.err.println("Problem with writing to the database.\n" + e.getMessage());
        }
    }

    public void playerChoiceOfName() {
        System.out.println("Chces si vybrat jedno z existujucich mien (0) alebo zacat s novym? (1)");
        int choice = Integer.parseInt(readLine());

        if(choice == 0) {
            playerUpdating();
        } else if (choice == 1) {
            playerAdding();
        } else {
            System.out.println("Nespravne zadanie.");
            playerChoiceOfName();
        }
    }

    public void playerUpdating() {
        System.out.println("S ktorym menom chces pokracovat?");
        printAllPlayers();
        String uName = readLine();
        playerService.getPlayersByUserName(uName);
    }

    public void playerAdding() {

        try {
            List<Occupation> occupations = occupationService.getOccupations();
            List<Country> countries = countryService.getCountries();

            System.out.println("Zadaj priezvisko: ");
            String lastName = readLine();

            //Osetrenie vstupu od pouzivatela pre selfvaluation
            boolean check = true;
            System.out.println("Ohodnot sa (1-10): ");
            int selfValue = 0;
            while (check) {
                int checker = Integer.parseInt(readLine());
                if (checker > 0 && checker < 11) {
                    selfValue = checker;
                    check = false;
                } else {
                    System.out.println("Nespravne zadanie, zadaj cislo od 1 po 10.");
                }
            }

            //Osetrenie vstupu od pouzivatela pre occupation
            check = true;
            printAllOccupations();
            int choiceOfOcc = 0;
            while (check) {
                int checker = Integer.parseInt(readLine());
                if (checker >= 0 && checker < occupations.size()) {
                    choiceOfOcc = checker;
                    check = false;
                } else {
                    System.out.println("Nespravne zadanie, zadaj cislo podla povolania");
                    printAllOccupations();
                }
            }

            printAllCountries();
            System.out.println("Ak nieje v zozname chces pridat novu? (Y/N)");
            String choiceOfCoAddition = readLine();
            if (choiceOfCoAddition.toLowerCase().equals("y")) {
                countryAdding();
            }

            //Osetrenie vstupu od pouzivatela pre countries
            check = true;
            printAllCountries();
            int choiceOfCo = 0;
            while(check) {
                int checker = Integer.parseInt(readLine());
                if(checker >= 0 && checker < countries.size()) {
                    choiceOfCo = checker;
                    check = false;
                } else {
                    System.out.println("Nespravne zadanie, zadaj cislo podla krajiny");
                    printAllCountries();
                }
            }

            playerService.addPlayer(new Player(userName, lastName, selfValue, countries.get(choiceOfCo), occupations.get(choiceOfOcc)));
            System.out.println("Pridany pouzivatel");

        } catch (Exception e) {
            System.err.println("Problem with writing to the database.\n" + e.getMessage());
        }
    }

    public void score(int gameScore) {

//        ScoreService scoreService = new ScoreServiceJDBC();
        var scores = scoreService.getBestScores(GAME);

        scoreService.addScore(new Score(GAME, userName, gameScore, new Date()));

        System.out.println("Top 5 hracov:");
        for (int i = 0; i < scores.size(); i++) {
            System.out.println((i + 1) + ". " +scores.get(i));
        }
    }
    private void handlerComment() {
        try {
            comment();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            handlerComment();
        }
    }

    private void handlerRating() {
        try {
            rating();
        } catch (NumberFormatException e) {
            System.out.println("Musi zadat len cislo, nie text.");
            handlerRating();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            handlerRating();
        }
    }

    public void comment() throws GameStudioException {
//        CommentService commentService = new CommentServiceJDBC();

        var comments = commentService.getComments(GAME);

        System.out.println("Zanechaj komentar k tvojej hre: ");
        String userComment = readLine();
        if(userComment.length() > 1000 || userComment.length() == 0) {
            throw new GameStudioException("Komentar je prilis dlhy alebo prilis kratky.");
        }
        commentService.addComment(new Comment(GAME, userName, userComment, new Date()));

        System.out.println("Poslednych 5 komentarov:");
        for (int i = 0; i < comments.size(); i++) {
            System.out.println((i + 1) + ". " +comments.get(i));
        }
    }

    public void rating() throws GameStudioException {

//        RatingService ratingService = new RatingServiceJDBC();
        System.out.println("Zanechaj hodnotenie k tvojej hre: ");

        String userRating = readLine();
        if(Integer.parseInt(userRating) < 1 || Integer.parseInt(userRating) > 5) {
            throw new GameStudioException("Nespravne hodnotenie. Zadaj hodnotenie este raz(1-5)");
        }

        ratingService.setRating(new Rating(GAME, userName, Integer.parseInt(userRating), new Date()));

        int rating = ratingService.getRating(GAME, userName);
        int avgRating = ratingService.getAverageRating(GAME);

        System.out.println("Hru si hodnotil " +rating+ "* ,priemerne hodnotenie hry " +GAME+ " je " +avgRating+ "*");
    }

    public boolean wantNewRound() {
        System.out.println("Chces si zahrat este raz? Y/N");
        String choice = readLine().toLowerCase(Locale.ROOT);
        if(choice.toLowerCase().equals("y")) {
            return true;
        }
        if(choice.toLowerCase().equals("n")) {
            return false;
        }
        System.out.println("Nespravne zadanie, skus este raz");
        return wantNewRound();
    }

    public void handleInput(String playerInput) throws WrongFormatException {
        //overi vstup, porovna s listom prikazov
        if(!listOfCommands.contains(playerInput)) {
            throw new WrongFormatException("Zadal si nespravny vstup, skus este raz.");
        }
    }

    public void processInput() {
        String playerInput = readLine().toLowerCase(Locale.ROOT);

        try {
            handleInput(playerInput);
        } catch (WrongFormatException e) {
            System.out.println(e.getMessage());
            processInput();
            return;
        }

        //vykona operaciu podla vstupu
        doOperation(playerInput);
        //skontroluje ci hra je vyriesena
        if(field.isSolved()) {
            field.setGameState(GameState.SOLVED);
        }
        //vypise dobu trvania daneho kola v sekundach
//        System.out.printf("Toto kolo hrajes %s sekund.%n", field.getPlayingSeconds());
    }

    public void doOperation(String playerInput) {
        switch (playerInput) {
            case "up":
            case "w":
                field.moveUp();
                break;
            case "down":
            case "s":
                field.moveDown();
                break;
            case "right":
            case "d":
                field.moveRight();
                break;
            case "left":
            case "a":
                field.moveLeft();
                break;
            case "new":
                field.setGameState(GameState.NEW_GAME);
                break;
            case "exit":
                field.setGameState(GameState.EXIT);
        }
    }

    @Override
    public void update() {
        System.out.println("Zadaj: (New) -> pre novu hru, (Exit) -> pre ukoncenie hry, (Up,w) -> pre posun hore," +
                " (Down,s) -> pre posun dole, (Right,d) -> pre posun doprava, (Left,a) -> pre posun dolava");
        printField();
    }

    public void printField() {
        for (int i = 0; i < this.field.getRowCount(); i++) {
            for (int j = 0; j < this.field.getColumnCount(); j++) {
                System.out.printf("%3s",this.field.getTile(i,j).toString());
            }
            System.out.println();
        }
    }
}
