package sk.tuke.gamestudio.entity;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {

    private String game;
    private String userName;
    private String comment;
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
