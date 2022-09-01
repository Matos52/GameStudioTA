package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.sudoku.core.Field;
import sk.tuke.gamestudio.sudoku.core.GameState;
import sk.tuke.gamestudio.sudoku.core.Tile;

import java.util.Date;
import java.util.Random;

@Controller
@RequestMapping("/sudoku")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class SudokuController {

    @Autowired
    private UserController userController;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;

    private Field field;

    private final String GAME = "sudoku";

    private boolean isPlaying = true;

    private int choice;

    private boolean validationOfTile = true;

    @RequestMapping
    public String processUserInput(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column, Model model){
        //method renamed from sudoku

        startOrUpdateGame(row,column);
        prepareModel(model);
        return GAME;
    }

    @RequestMapping("/new")
    public String newGame(Model model){
        startNewGame();
        prepareModel(model);
        return GAME;
    }

    @RequestMapping("/addNumber")
    public String addNumber(int number, Model model) {
        choice = number;
        prepareModel(model);
        return GAME;
    }

    @RequestMapping("/unmark")
    public String unmark(Model model) {
        choice = 0;
        prepareModel(model);
        return GAME;
    }

    @RequestMapping("/comment")
    public String comment(String comment, Model model) {
        if(userController.isLogged()) {
            Comment newComment = new Comment(GAME,userController.getLoggedUser(),comment,new Date());
            commentService.addComment(newComment);
        } else {
            Comment newComment = new Comment(GAME,"Anonym",comment,new Date());
            commentService.addComment(newComment);
        }
        prepareModel(model);
        return GAME;
    }

    public String isRating() {
        if(ratingService.getAverageRating(GAME) == 0) {
            return "Bez hodnotení";
        }
        return String.valueOf(ratingService.getAverageRating(GAME));
    }

    @RequestMapping("/rating")
    public String rating(int rating, Model model) {
        Rating newRating;
        if(userController.isLogged()) {
            newRating = new Rating(GAME,userController.getLoggedUser(),rating,new Date());
            ratingService.setRating(newRating);
        } else {
            newRating = new Rating(GAME,"Anonym",rating,new Date());
            ratingService.setRating(newRating);
        }
        prepareModel(model);
        return GAME;
    }

    public String getTileText(Tile tile) {
        if(tile.getValue()==0){
            return "";
        } else {
            return String.valueOf(tile.getValue());
        }
    }

    public String getTile(Tile tile) {
        return String.valueOf(tile.getValue());
    }

    /**
     * Initiates the game field and other variables to the state at the start of a new game
     */
    private void startNewGame(){
        this.field = new sk.tuke.gamestudio.sudoku.core.Field(9,9);
        this.isPlaying = true;
    }

    /**
     * Updates the game field and other variables according to the move of the user
     * Also adds the score to the score table if the game just ended.
     * If the game did not start yet, starts the game.
     * @param row row of the tile on which the user clicked
     * @param column column of the tile on which the user clicked
     */
    private void startOrUpdateGame(Integer row, Integer column){

        if(this.field==null){
            startNewGame();
        }

        if(row != null && column != null && (field.getTile(row,column).getValue() == 0) && choice != 0) {
            if(field.validationOfTile(choice, row, column)) {
                field.setTile(choice,row,column);
                validationOfTile = true;
            } else {
                validationOfTile = false;
            }
        }

        if(row != null && column != null && (field.getTile(row,column).getValue() != 0) && choice == 0) {
            field.setTile(choice,row,column);
        }

        if(this.field.isSolved()) {
            this.isPlaying = false;
            this.field.setState(GameState.SOLVED);

            if(userController.isLogged()){
                Score newScore = new Score(GAME, userController.getLoggedUser(), this.field.getScore(), new Date());
                scoreService.addScore(newScore);
            }
        }
    }

    private String getErrorTileMessage() {
        String tileMessage;
        if(!validationOfTile) {
            tileMessage = "Zvolené číslo sa už nachádza buď v riadku, stĺpci alebo v príslušnom štvorčeku.";
        } else {
            tileMessage = "Zvoľ číslo";
        }
        return tileMessage;
    }

    private String getStatusMessage() {
        String gameStatus="";
        if(this.field.getState() == sk.tuke.gamestudio.sudoku.core.GameState.SOLVED){
            gameStatus="Vyhral si (skóre: "+this.field.getScore()+")";
        } else {
            gameStatus="Hraješ";
        }
        return gameStatus;
    }

    public void prepareModel(Model model) {
        model.addAttribute("isPlaying",this.isPlaying);
        model.addAttribute("gameStatus",getStatusMessage());
        model.addAttribute("errorTileMessage",getErrorTileMessage());
        model.addAttribute("sudokuField", field.getTiles());
        model.addAttribute("bestScores", scoreService.getBestScores(GAME));
        model.addAttribute("getComments", commentService.getComments(GAME));
        model.addAttribute("getAvgRating", isRating());
    }
}

