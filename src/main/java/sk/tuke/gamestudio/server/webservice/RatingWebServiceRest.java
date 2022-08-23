package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;

import java.util.List;

@RestController
@RequestMapping("/api/rating")
public class RatingWebServiceRest {

    @Autowired
    private RatingService ratingService;

    //http://localhost:8080/api/rating/minesweeper
    @GetMapping("/{game}/{userName}")
    public int getRating(@PathVariable String game,@PathVariable String userName) {
        return ratingService.getRating(game, userName);
    }

    /*  http://localhost:8080/api/rating?userName=Edo
    @GetMapping("/{userName}")
    public int getRating(String game, String userName) {
        return ratingService.getRating(game, userName);
    }
    */

    @PostMapping
    public void setRating(@RequestBody Rating rating) {
        ratingService.setRating(rating);
    }

    @GetMapping("/{game}")
    public int getAverageRating(@PathVariable String game) {
        return ratingService.getAverageRating(game);
    }
}
