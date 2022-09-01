package sk.tuke.gamestudio.sudoku.consoleUI;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.*;
import sk.tuke.gamestudio.service.*;
import sk.tuke.gamestudio.sudoku.core.Field;
import sk.tuke.gamestudio.sudoku.core.GameState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUISudoku implements UserInterface {

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

    private static final String GAME = "sudoku";

    Pattern ADD_NUMBER_PATTERN = Pattern.compile("([A-Z]{1})([A-Z]{1})([1-9]{1})");
    Pattern UN_MARK_PATTERN = Pattern.compile("([A-Z]{1})([A-Z]{1})([U]{1})");

    Field field;

    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void play() {
        this.field = new Field(9,9);
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

        int gameScore = 0;

        do {
            update();
            processInput();

            if(field.isSolved()) {
                field.setState(GameState.SOLVED);
                gameScore = this.field.getScore();
                System.out.println(userName+", vyhral si. Tvoje skore je "+gameScore+".");
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

    @Override
    public void update() {
        printField();
    }

    public void printField() {
        for (int r = 0; r < field.getRowCount(); r++) {
            if(r % 3 == 0 && r != 0) {
                System.out.println("-----------------------------------");
            }
            for (int c = 0; c < field.getColumnCount(); c++) {
                if(c % 3 == 0 && c != 0) {
                    System.out.printf("  |");
                }
                System.out.printf("%3s", field.getTile(r,c));
            }
            System.out.println();
        }
    }

    public void processInput() {
        System.out.println("Ocakavany vstup:  X - ukoncenie hry. Prve pismeno (A-I) na oznacenie riadku, druhe pismeno (A-I) na oznacenie stplca.");
        System.out.println("Tretie cislo (1-9) na vlozenie pozadovaneho cislo. Priklad: (BF5)");
        String playerInput = readLine();

        if(playerInput.toLowerCase().trim().equals("x")) {
            System.out.println("Ukoncujem hru");
            System.exit(0);
        }

        // overi format vstupu - exception handling
        try {
            handleInput(playerInput);
        } catch (WrongFormatException e) {
            System.out.println(e.getMessage());
            processInput();
        }
    }

    void handleInput(String playerInput) throws WrongFormatException {
        Matcher matcher1 = ADD_NUMBER_PATTERN.matcher(playerInput);
        Matcher matcher2 = UN_MARK_PATTERN.matcher(playerInput);

        if (!matcher1.matches() && !matcher2.matches()) {
            throw new WrongFormatException("Zadal si nespravny format vstupu, opakuj vstup.");
        }

        if(matcher1.matches()) {
            if (!isInputInBorderOfField(matcher1.group(1), matcher1.group(2), matcher1.group(3))) {
                System.out.println("");
                processInput();
                return;
            }
        }

        if(matcher2.matches()) {
            if(!isUnMarkInBorderOfField(matcher2.group(1), matcher2.group(2))) {
                System.out.println("");
                processInput();
                return;
            }
        }

        if(matcher1.matches()) {
            doOperation(matcher1.group(1).charAt(0), matcher1.group(2).charAt(0), Integer.parseInt(matcher1.group(3)));
        }

        if(matcher2.matches()) {
            unMarkField(matcher2.group(1).charAt(0), matcher2.group(2).charAt(0));
        }
    }

    private void doOperation(char row, char column, int choice) {

        int rowInt = row - 65;
        int columnInt = column - 65;

        if(field.validationOfTile(choice, rowInt, columnInt)) {
            field.setTile(choice, rowInt, columnInt);
            System.out.println("Vykonal som pozadovanu operaciu");
        } else {
            System.out.println("Zly vstup! Cislo sa uz nachadza bud v riadku, stlpci alebo v zadanom stvorci. Skus este raz.");
        }
    }

    private void unMarkField(char row, char column) {
        int rowInt = row - 65;
        int columnInt = column - 65;
        field.unSetTile(rowInt, columnInt);
    }

    private boolean isInputInBorderOfField(String rowString, String columnString, String choice) {
        boolean result = true;

        if ((int) rowString.charAt(0) >= (65 + field.getRowCount())) {
            result = false;
            System.out.print("Pismeno prekracuje pocet riadkov.");
        }

        if ((int) columnString.charAt(0) >= (65 + field.getColumnCount())) {
            result = false;
            System.out.print("Pismeno prekracuje pocet stlpcov.");
        }

        if(Integer.parseInt(choice) < 1 || Integer.parseInt(choice) > 9) {
            result = false;
            System.out.println("Cislo nieje v rozmedzi 1-9.");
        }

        if (!result) {
            System.out.println(" Opakuj vstup.");
        }
        return result;
    }

    private boolean isUnMarkInBorderOfField(String rowString, String columnString) {
        boolean result = true;

        if ((int) rowString.charAt(0) >= (65 + field.getRowCount())) {
            result = false;
            System.out.print("Pismeno prekracuje pocet riadkov.");
        }

        if ((int) columnString.charAt(0) >= (65 + field.getColumnCount())) {
            result = false;
            System.out.print("Pismeno prekracuje pocet stlpcov.");
        }

        if (!result) {
            System.out.println(" Opakuj vstup.");
        }
        return result;
    }
}
