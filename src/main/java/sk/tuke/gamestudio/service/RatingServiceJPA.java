package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {

    }

    @Override
    public int getAverageRating(String game) {
        entityManager.createNativeQuery("SELECT ROUND(AVG(rating)) FROM rating where game = :myGame")
                .setParameter("myGame", game);
    }

    @Override
    public int getRating(String game, String username) {
        entityManager.createNativeQuery("SELECT rating FROM rating where game = :myGame AND username = :myUserName")
                .setParameter("myGame",game).setParameter("myUserName", username).executeUpdate();
    }

    @Override
    public void reset() {
        entityManager.createNativeQuery("DELETE FROM rating").executeUpdate();
    }
}
