package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {

    private String loggedUser;

    private String comment;

    private final String GAME = "minesweeper";

    @RequestMapping("/loginMinesweeper")
    public String loginMinesweeper(String login, String password) {
        if(("heslo").equals(password)) {

            //Osertrenie dlzky mena
            this.loggedUser = login.trim();
            if(this.loggedUser.length()>0) {
                //Presmeruje nas na danu adresu
                return "redirect:/minesweeper";
            }
        }
        this.loggedUser=null;
        //Presmeruje nas na danu adresu
        return "redirect:/gamestudio";
    }

    @RequestMapping("/loginKamene")
    public String loginKamene(String login, String password) {
        if(("heslo").equals(password)) {

            //Osertrenie dlzky mena
            this.loggedUser = login.trim();
            if(this.loggedUser.length()>0) {
                //Presmeruje nas na danu adresu
                return "redirect:/kamene";
            }
        }
        this.loggedUser=null;
        //Presmeruje nas na danu adresu
        return "redirect:/kamene";
    }

    @RequestMapping("/logout")
    public String login() {
        this.loggedUser=null;
        return "redirect:/gamestudio";
    }

    public String getLoggedUser(){
        return loggedUser;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }
}
