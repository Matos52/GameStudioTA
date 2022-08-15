package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Country;
import sk.tuke.gamestudio.entity.Score;

import java.util.Arrays;
import java.util.List;

public class CountryServiceRest implements CountryService {

    //    private String url = "http://localhost:8080/api";

    @Value("${remote.server.api}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addCountry(Country country) {
        restTemplate.postForEntity(url+"/country", country, Country.class);
    }

    @Override
    public List<Country> getCountries() {
        return Arrays.asList(restTemplate.getForEntity(url + "/country/all" ,Country[].class).getBody());
    }
}
