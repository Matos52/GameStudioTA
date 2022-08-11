package sk.tuke.gamestudio.minesweeper;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
public class PlaygroundJPA {

    @PersistenceContext
    private EntityManager entityManager;

    public void play() {
        System.out.println("Opening JPA playground");

        String game = "minesweeper";
        String user = "Feri";
        int ratingValue = 4;

//        entityManager.persist(new Rating(game, user, ratingValue, new Date()));

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

        System.out.println(rating2Write);

        //The EntityManager. persist() operation is used to insert a new object into the database.
        // persist does not directly insert the object into the database: it just registers it as
        // new in the persistence context (transaction).
        entityManager.persist(new Score(game, "Stevo", 10, new Date()));
        entityManager.persist(new Score(game, "Stevoo", 10, new Date()));

        entityManager.persist(new Comment(game, "Matej", "Hahaha", new Date()));
        entityManager.persist(new Comment(game, "Filip", "Kukuku", new Date()));

        entityManager.persist(new Rating(game, "Matej", 4, new Date()));
        entityManager.persist(new Rating(game, "Edo", 2, new Date()));

        List<Score> bestScores = entityManager
                .createQuery("Select s from Score s where s.game = :myGame order by s.points desc")
                .setParameter("myGame", game)
                .getResultList();

        System.out.println(bestScores);

        List<Comment> getComments = entityManager
                .createQuery("Select c from Comment c where c.game = :myGame order by c.commentedOn desc")
                .setParameter("myGame", game)
                .getResultList();

        System.out.println(getComments);

        double avgRating = (Double) entityManager.createQuery("select AVG(CAST(r.rating as integer)) from Rating r where r.game = :myGame")
                .setParameter("myGame", game).getSingleResult();

        System.out.printf("%.0f\n", avgRating);

        System.out.println("Closing JPA playground");
    }
}
