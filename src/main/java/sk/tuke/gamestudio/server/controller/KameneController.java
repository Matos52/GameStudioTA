package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    private Field field = new Field(4,4);

    private GameState gamestate;

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
        prepareModel(model);
        this.field.moveUp();
        return "redirect:/kamene";
    }

    @RequestMapping("/down")
    public String moveDown(Model model){
        prepareModel(model);
        this.field.moveDown();
        return "redirect:/kamene";
    }

    @RequestMapping("/right")
    public String moveRight(Model model){
        prepareModel(model);
        this.field.moveRight();
        return "redirect:/kamene";
    }

    @RequestMapping("/left")
    public String moveLeft(Model model){
        prepareModel(model);
        this.field.moveLeft();
        return "redirect:/kamene";
    }

    //Pre asynchronnu verziu hry
    @RequestMapping("/asynch")
    public String loadInAsynchMode() {
        startOrUpdateGame(null,null);
        return "kameneAsynch";
    }

    @RequestMapping(value="/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field processUserInputJson(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column){
        boolean justFinished = startOrUpdateGame(row,column);
        this.field.setJustFinished(justFinished);
        return this.field;
    }

    @RequestMapping(value="/jsonnew", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field newGameJson(){
        startNewGame();
        this.field.setJustFinished(false);
        return this.field;
    }

    @RequestMapping(value="/jsonup", produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field moveUpAsynch() {
        this.field.moveUp();

        return this.field;
    }

    @RequestMapping(value="/jsondown", produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field moveDownAsynch() {
        this.field.moveDown();

        return this.field;
    }

    @RequestMapping(value="/jsonright", produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field moveRightAsynch() {
        this.field.moveRight();

        return this.field;
    }

    @RequestMapping(value="/jsonleft", produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field moveLeftAsynch() {
        this.field.moveLeft();

        return this.field;
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
    private boolean startOrUpdateGame(Integer row, Integer column){

        boolean justFinished = false;

        if(this.field==null){
            startNewGame();
        }

        if(this.field.isSolved()) {
            this.field.setGameState(GameState.SOLVED);
            ableToMove = false;
            this.isPlaying = false;

            justFinished=true;

            if(userController.isLogged()){
                Score newScore = new Score(GAME, userController.getLoggedUser(), this.field.getScore(), new Date());
                scoreService.addScore(newScore);
            }
        }
        return justFinished;
    }

    private String getStatusMessage() {
        String gameStatus="";
        if(this.field.getGameState() == GameState.SOLVED){
            gameStatus="Vyhral si (skóre: "+this.field.getScore()+")";
        } else {
            gameStatus="Hraješ a posúvaš políčko";
        }
        return gameStatus;
    }

    public void prepareModel(Model model) {
        model.addAttribute("ableToMove",this.ableToMove);
        model.addAttribute("gameStatus",getStatusMessage());
        model.addAttribute("kameneField", field.getTiles());
        model.addAttribute("bestScores", scoreService.getBestScores(GAME));
        model.addAttribute("getComments", commentService.getComments(GAME));
        model.addAttribute("getAvgRating", isRating());
    }
}
