package sk.tuke.gamestudio.minesweeper;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Score;

import javax.persistence.EntityManager;
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

        //The EntityManager. persist() operation is used to insert a new object into the database.
        // persist does not directly insert the object into the database: it just registers it as
        // new in the persistence context (transaction).
        entityManager.persist(new Score(game, "Stevo", 10, new Date()));
        entityManager.persist(new Score(game, "Stevoo", 10, new Date()));

        entityManager.persist(new Comment(game, "Matej", "Hahaha", new Date()));
        entityManager.persist(new Comment(game, "Filip", "Kukuku", new Date()));

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

        System.out.println("Closing JPA playground");
    }
}
