package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Country;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CountryServiceJPA implements CountryService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addCountry(Country country) {
        entityManager.persist(country);
    }

    @Override
    public List<Country> getCountries() {
        List<Country> getCountries = entityManager
                .createQuery("Select c from Country c")
                .getResultList();

        return getCountries;
    }
}
