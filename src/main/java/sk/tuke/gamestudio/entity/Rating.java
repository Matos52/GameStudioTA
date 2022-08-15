package sk.tuke.gamestudio.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueGameAndUsername", columnNames = {"game", "userName"})})
public class Rating implements Serializable {


    @Id
    @GeneratedValue
    private long ident;
    @Column(nullable = false, length=64)
    private String game;
    @Column(nullable = false, length=64)
    private String userName;
    @Column(columnDefinition = "INT CHECK(rating BETWEEN 1 AND 5) NOT NULL")
    private int rating;
    @Column(nullable = false)
    private Date ratedOn;

    public Rating() {
    }

    public Rating(int rating) {
        this.rating = rating;
    }


    public Rating(String game, String userName, int rating, Date ratedOn) {
        this.game = game;
        this.userName = userName;
        this.rating = rating;
        this.ratedOn = ratedOn;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRatedOn() {
        return ratedOn;
    }

    public void setRatedOn(Date ratedOn) {
        this.ratedOn = ratedOn;
    }

    @Override
    public String toString() {
//        return "Rating{" +
//                "game='" + game + '\'' +
//                ", userName='" + userName + '\'' +
//                ", rating=" + rating +
//                ", ratedOn=" + ratedOn +
//                '}';
//    }

        return "Player " + userName + " rated game " + game + " with " + rating + "*, played on " + ratedOn + "!";
    }
}
