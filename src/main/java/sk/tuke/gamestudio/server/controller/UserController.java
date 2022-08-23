package sk.tuke.gamestudio.server.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {

    private String loggedUser;

    @RequestMapping("/login")
    public String login(String login, String password) {
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
