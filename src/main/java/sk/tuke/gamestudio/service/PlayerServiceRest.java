package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.entity.Score;

import java.util.Arrays;
import java.util.List;

public class PlayerServiceRest implements PlayerService {

    @Value("${remote.server.api}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addPlayer(Player player) {
        restTemplate.postForEntity(url+"/player",player, Score.class);
    }

    @Override
    public List<Player> getPlayersByUserName(String uName) {
        return Arrays.asList(restTemplate.getForEntity(url + "/player/" + uName,Player.class).getBody());
    }

    @Override
    public List<Player> getAllPlayers() {
        return Arrays.asList(restTemplate.getForEntity(url + "/player/" ,Player.class).getBody());
    }
}
