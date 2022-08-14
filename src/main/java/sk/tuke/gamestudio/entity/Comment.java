package sk.tuke.gamestudio.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Comment implements Serializable {

    public Comment() {
    }

    @Id
    @GeneratedValue
    private long ident;
    @Column(nullable = false, length=64)
    private String game;
    @Column(nullable = false, length=64)
    private String userName;

    @Column(nullable = false, length = 1000)
    private String comment;
    @Column(nullable = false)
    private Date commentedOn;

    public Comment(String game, String userName, String comment, Date commentedOn) {
        this.game = game;
        this.userName = userName;
        this.comment = comment;
        this.commentedOn = commentedOn;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentedOn() {
        return commentedOn;
    }

    public void setCommentedOn(Date commentedOn) {
        this.commentedOn = commentedOn;
    }

    @Override
    public String toString() {
//        return "Comment{" +
//                "game='" + game + '\'' +
//                ", userGame='" + userGame + '\'' +
//                ", comment='" + comment + '\'' +
//                ", commented_on=" + commented_on +
//                '}';

        return "Player " +userName+ " in " +game+ " commented:  " +comment+ " on " + commentedOn +"!";
    }
}
