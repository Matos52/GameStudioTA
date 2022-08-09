package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.RatingServiceJDBC;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RatingServiceTest {

    private RatingService ratingService = new RatingServiceJDBC();

    private static final String GAME = "minesweeper";

    @Test
    public void getRating() {
        ratingService.reset();
        ratingService.setRating(new Rating(GAME, "Matej", 4, new Date()));
        assertEquals(4,ratingService.getRating(GAME, "Matej"));
    }

    @Test
    public void avgRatingTest() {
        ratingService.reset();
        var date = new Date();

        ratingService.setRating(new Rating(GAME, "Peto", 5, date));
        ratingService.setRating(new Rating(GAME, "Katka", 1, date));
        ratingService.setRating(new Rating(GAME, "Zuzka", 4, date));

        assertEquals((5+1+4)/3,ratingService.getAverageRating(GAME));
    }

    @Test
    public void Duplicity() {
        ratingService.reset();
        var date = new Date();
        ratingService.setRating(new Rating(GAME, "Peto", 5, date));
        ratingService.setRating(new Rating(GAME, "Peto", 2, date));
        assertEquals(2,ratingService.getRating(GAME, "Peto"));
    }


}
