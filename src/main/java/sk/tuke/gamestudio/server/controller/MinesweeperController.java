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
import sk.tuke.gamestudio.minesweeper.core.Clue;
import sk.tuke.gamestudio.minesweeper.core.Field;
import sk.tuke.gamestudio.minesweeper.core.GameState;
import sk.tuke.gamestudio.minesweeper.core.Tile;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;

@Controller
@RequestMapping("/minesweeper")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MinesweeperController {

    @Autowired
    private UserController userController;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    private Field field = new Field(9,9,10);

    /**
     * false if opening tiles, true if marking tiles
     */
    private boolean marking = false;

    private final String GAME = "minesweeper";

    /**
     * false if finished (won or lost), true if playing the game
     */
    private boolean isPlaying = true;

    @RequestMapping
    public String minesweeper(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column, Model model){

        if(row != null && column != null){

            if(this.field.getState()==GameState.PLAYING){
                if(marking){
                    this.field.markTile(row,column);
                }else{
                    this.field.openTile(row,column);
                }
            }

            if(this.field.getState()!= GameState.PLAYING && this.isPlaying==true){ //I just won/lose
                this.isPlaying=false;

                if(userController.isLogged()) {
                    Score newScore = new Score(GAME, userController.getLoggedUser(), this.field.getScore(), new Date());
                    scoreService.addScore(newScore);
                }
            }
        }

        prepareModel(model);
        return "minesweeper";
    }

    @RequestMapping("/mark")
    public  String changeMarking(Model model){
        if(this.field.getState()==GameState.PLAYING){
            this.marking = !this.marking;
        }
        prepareModel(model);
        return "minesweeper";
    }

    @RequestMapping("/new")
    public  String newGame(Model model){
        this.field = new Field(9,9,10);
        this.isPlaying = true;
        this.marking = false;
        prepareModel(model);
        return "minesweeper";
    }

    public String getCurrTime(){
        return new Date().toString();
    }

    public boolean getMarking(){
        return this.marking;
    }


    /**
     * Generates the full table with the minesweeper field.
     * (now unused, this is transformed to the template)
     * @return String with HTML of the table
     */
    public String getFieldAsHtml(){

        int rowCount = this.field.getRowCount();
        int colCount = this.field.getColumnCount();

        StringBuilder sb = new StringBuilder();

        sb.append("<table class='minefield'>\n");

        for (int row = 0; row<rowCount;row++){
            sb.append("<tr>\n");

            for (int col = 0; col<colCount;col++){
                Tile tile = this.field.getTile(row,col);

                sb.append("<td class='" + getTileClass(tile) + "'> ");
                sb.append("<a href='/minesweeper?row="+row+"&column="+col+"'> ");
                sb.append("<span>" + getTileText(tile) + "</span>");
                sb.append(" </a>\n");
                sb.append(" </td>\n");

            }
            sb.append("</tr>\n");
        }


        sb.append("</table>\n");

        return sb.toString();
    }

    /**
     * Gets the text that may be displayed inside a HTML element representing 1 tile
     * Now public as it is called from the template
     * @param tile - the Tile object the text is extracted from
     * @return the text that may be displayed inside a HTML element representing the Tile tile
     */
    public String getTileText(Tile tile){
        switch (tile.getState()){
            case CLOSED:
                return "-";
            case MARKED:
                return "M";
            case OPEN:
                if (tile instanceof Clue) {
                    return String.valueOf(((Clue) tile).getValue());
                } else {
                    return "X";
                }
            default:
                throw new IllegalArgumentException("Unsupported tile state " + tile.getState());
        }
    }

    /**
     * Gets the HTML class of the <td> element representing the Tile tile
     * Now public as it is called from the template
     * @param tile - the Tile object the class is assigned to
     * @return String with the HTML class of the <td> element representing the Tile tile
     */
    public String getTileClass(Tile tile) {
        switch (tile.getState()) {
            case OPEN:
                if (tile instanceof Clue)
                    return "open" + ((Clue) tile).getValue();
                else
                    return "mine";
            case CLOSED:
                return "closed";
            case MARKED:
                return "marked";
            default:
                throw new RuntimeException("Unexpected tile state");
        }
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
        return "minesweeper";
    }

    public String isRating() {
        if(ratingService.getAverageRating(GAME) == 0) {
            return "Bez hodnotení";
        }
        return String.valueOf(ratingService.getAverageRating(GAME));
    }

    public String noRating() {
        return "Bez hodnoteni";
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
        return "minesweeper";
    }

    /**
     * Fills the Spring MVC model object for the Thymeleaf template
     * @param model - the Spring MVC model
     */
    private void prepareModel(Model model){

        String gameStatus="";
        if(this.field.getState()== GameState.FAILED){
            gameStatus="Prehral si";
        }else if(this.field.getState()== GameState.SOLVED){
            gameStatus="Vyhral si (skóre: "+this.field.getScore()+")";
        }else{
            gameStatus="Hraješ a ";
            if(this.marking){
                gameStatus+="označuješ";
            }else{
                gameStatus+="otváraš";
            }
        }

        model.addAttribute("isPlaying",this.isPlaying);
        model.addAttribute("marking",this.marking);
        model.addAttribute("gameStatus",gameStatus);
        model.addAttribute("minesweeperField",this.field.getTiles());
        model.addAttribute("bestScores",scoreService.getBestScores(GAME));
        model.addAttribute("getComments",commentService.getComments(GAME));
        model.addAttribute("getAvgRating", isRating());
    }
}
