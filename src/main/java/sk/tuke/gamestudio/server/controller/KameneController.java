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
import sk.tuke.gamestudio.kamene.core.Field;
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

    @RequestMapping
    public String kamene(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column, Model model) {

        prepareModel(model);
        return GAME;
    }

    @RequestMapping("/new")
    public String newGame(Model model){
        prepareModel(model);
        field = new Field(4,4);
        return GAME;
    }

    @RequestMapping("/up")
    public String moveUp(Model model){
        prepareModel(model);
        field.moveUp();
        return GAME;
    }

    @RequestMapping("/down")
    public String moveDown(Model model){
        prepareModel(model);
        field.moveDown();
        return GAME;
    }

    @RequestMapping("/right")
    public String moveRight(Model model){
        prepareModel(model);
        field.moveRight();
        return GAME;
    }

    @RequestMapping("/left")
    public String moveLeft(Model model){
        prepareModel(model);
        field.moveLeft();
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
        return String.valueOf(tile.getValue());
    }

    public void prepareModel(Model model) {
        model.addAttribute("kameneField", field.getTiles());
        model.addAttribute("bestScores", scoreService.getBestScores(GAME));
        model.addAttribute("getComments", commentService.getComments(GAME));
        model.addAttribute("getAvgRating", isRating());
    }
}
