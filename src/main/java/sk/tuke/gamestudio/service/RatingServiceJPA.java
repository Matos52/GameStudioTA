package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;

@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {

        String user = rating.getUserName();
        String game = rating.getGame();
        int ratingValue = rating.getRating();

        Rating rating2Write = null;

        try {
            rating2Write = (Rating) entityManager.createQuery("select r from Rating r where r.userName = :user and r.game = :game")
                    .setParameter("user", user)
                    .setParameter("game", game)
                    .getSingleResult();

            rating2Write.setRating(ratingValue);
            rating2Write.setRatedOn(new Date());

        } catch (NoResultException e) {
            rating2Write = new Rating(game,user,ratingValue,new Date());
            entityManager.persist(rating2Write);
        }
    }

    @Override
    public int getAverageRating(String game) {
        double avgRating = (Double) entityManager.createQuery("select AVG(CAST(r.rating as integer)) from Rating r where r.game = :myGame")
                .setParameter("myGame", game).getSingleResult();
        return (int) avgRating;
    }

    @Override
    public int getRating(String game, String username) {
        Rating rating = (Rating) entityManager.createQuery("Select r from Rating r where r.game = :myGame and r.userName = :myUserName")
                .setParameter("myGame",game).setParameter("myUserName", username).getSingleResult();
        return rating.getRating();
    }

    @Override
    public void reset() {
        entityManager.createNativeQuery("DELETE FROM rating").executeUpdate();
    }
}
