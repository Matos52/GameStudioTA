package sk.tuke.gamestudio.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Score implements Serializable {

    //primarny kluc
    @Id
    @GeneratedValue
    private long ident;
    @Column(nullable = false, length=64)
    private String game;
    @Column(nullable = false, length=64)
    private String userName;
    @Column(nullable = false)
    private int points;
    @Column(nullable = false)
    private Date playedOn;

    public Score() {

    }

    public Score(String game, String userName, int points, Date playedOn) {
        this.game = game;
        this.userName = userName;
        this.points = points;
        this.playedOn = playedOn;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getPlayedOn() {
        return playedOn;
    }

    public void setPlayedOn(Date playedOn) {
        this.playedOn = playedOn;
    }

    @Override
    public String toString() {
//        return "Score{" +
//                "game='" + game + '\'' +
//                ", userName='" + userName + '\'' +
//                ", points=" + points +
//                ", playedOn=" + playedOn +
//                '}';

        return "Player " +userName+ " in " +game+ " acquired " +points+ " points on " +playedOn+"!";
    }
}
