package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.PlayerService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerWebServiceRest {

    @Autowired
    private PlayerService playerService;

    //http://localhost:8080/api/player/uName
    @GetMapping("/{game}")
    public List<Player> getPlayers(@PathVariable String uName) {
        return playerService.getPlayersByUserName(uName);
    }

    @PostMapping
    public void addPlayer(@RequestBody Player player) {
        playerService.addPlayer(player);
    }
}
