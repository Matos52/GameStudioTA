package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;

import java.util.Arrays;

public class RatingServiceRest implements RatingService {

    @Value("${remote.server.api}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void setRating(Rating rating) {
        restTemplate.postForEntity(url + "/rating",rating, Rating.class);
    }

    @Override
    public int getAverageRating(String game) {
        return restTemplate.getForObject(url + "/rating/avg/" + game, Rating.class).getRating();
    }

    @Override
    public int getRating(String game, String username) {
        Rating rating = restTemplate.getForObject(url + "/rating/" + game + "/" + username, Rating.class);
        return rating.getRating();

    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web.");
    }
}
