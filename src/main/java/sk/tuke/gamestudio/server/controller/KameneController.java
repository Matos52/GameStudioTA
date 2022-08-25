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
import sk.tuke.gamestudio.kamene.core.Field;
import sk.tuke.gamestudio.kamene.core.GameState;
import sk.tuke.gamestudio.kamene.core.Tile;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;

@Controller
@RequestMapping("/kamene")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class KameneController {

    @Autowired
    private UserController userController;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;

    private Field field = new Field(4, 4);

    private String GAME = "kamene";

    /**
     * false if finished (won or lost), true if playing the game
     */
    private boolean isPlaying = true;
    private boolean ableToMove = true;

    @RequestMapping
    public String kamene(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column, Model model) {

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

    @RequestMapping("/up")
    public String moveUp(Model model){
        if(ableToMove) {
            prepareModel(model);
            field.moveUp();
        }
        return GAME;
    }

    @RequestMapping("/down")
    public String moveDown(Model model){
        if(ableToMove) {
            prepareModel(model);
            field.moveDown();
        }
        return GAME;
    }

    @RequestMapping("/right")
    public String moveRight(Model model){
        if(ableToMove) {
            prepareModel(model);
            field.moveRight();
        }
        return GAME;
    }

    @RequestMapping("/left")
    public String moveLeft(Model model){
        if(ableToMove) {
            prepareModel(model);
            field.moveLeft();
        }
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

    public String getTileNum(Tile tile) {
        if(tile.getValue()==0){
            return "";
        } else {
            return String.valueOf(tile.getValue());
        }
    }

    /**
     * Initiates the game field and other variables to the state at the start of a new game
     */
    private void startNewGame(){
        this.field = new Field(4,4);
        this.isPlaying = true;
        this.ableToMove = true;
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

        if(row != null && column != null){

            if(this.field.getGameState() == GameState.SOLVED && this.isPlaying==true){ //I just won/lose
                this.isPlaying=false;
                this.ableToMove=false;

                if(userController.isLogged()){
                    Score newScore = new Score(GAME, userController.getLoggedUser(), this.field.getScore(), new Date());
                    scoreService.addScore(newScore);
                }
            }
        }
    }

    public void prepareModel(Model model) {
        model.addAttribute("kameneField", field.getTiles());
        model.addAttribute("bestScores", scoreService.getBestScores(GAME));
        model.addAttribute("getComments", commentService.getComments(GAME));
        model.addAttribute("getAvgRating", isRating());
    }
}
