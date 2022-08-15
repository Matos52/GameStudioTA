package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Player;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class PlayerServiceJPA implements PlayerService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addPlayer(Player player) {
        entityManager.persist(player);
    }

    @Override
    public List<Player> getPlayersByUserName(String uName) {

        List<Player> getPlayersByUserName = entityManager
                .createQuery("Select p from Player p where p.userName = :uName")
                .setParameter("uName", uName)
                .getResultList();

        return getPlayersByUserName;
    }

    public List<Player> getAllPlayers() {

        List<Player> getAllPlayers = entityManager
                .createQuery("Select p from Player p")
                .getResultList();

        return getAllPlayers;
    }
}
