package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Occupation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class OccupationServiceJPA implements OccupationService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addOccupation(Occupation occupation) {
        entityManager.persist(occupation);
    }

    @Override
    public List<Occupation> getOccupations() {

        List<Occupation> getOccupations = entityManager
                .createQuery("Select o from Occupation o")
                .getResultList();

        return getOccupations;
    }
}
