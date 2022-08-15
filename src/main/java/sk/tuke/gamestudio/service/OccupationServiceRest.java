package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Country;
import sk.tuke.gamestudio.entity.Occupation;

import java.util.Arrays;
import java.util.List;

public class OccupationServiceRest implements OccupationService {

    @Value("${remote.server.api}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addOccupation(Occupation occupation) {
        restTemplate.postForEntity(url+"/occupation", occupation, Occupation.class);
    }

    @Override
    public List<Occupation> getOccupations() {
        return Arrays.asList(restTemplate.getForEntity(url + "/occupation/all" ,Occupation[].class).getBody());
    }
}
