package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Score;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class ScoreServiceJPA implements ScoreService {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addScore(Score score) {
        entityManager.persist(score);
    }

    @Override
    public List<Score> getBestScores(String game) {

        List<Score> bestScores = entityManager
                .createQuery("Select s from Score s where s.game = :myGame order by s.points desc")
                .setParameter("myGame", game)
                .setMaxResults(5)
                .getResultList();

        return bestScores;
    }

    @Override
    public void reset() {
        entityManager.createNativeQuery("DELETE FROM score").executeUpdate();
    }
}
