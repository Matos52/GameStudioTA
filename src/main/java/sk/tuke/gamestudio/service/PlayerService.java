package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Player;

import java.util.List;

public interface PlayerService {

    public void addPlayer(Player player);

    public List<Player> getPlayersByUserName(String uName);
}
