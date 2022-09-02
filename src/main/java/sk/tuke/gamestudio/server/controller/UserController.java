package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Country;
import sk.tuke.gamestudio.entity.Occupation;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.service.*;

import java.util.Date;
import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {

    private String loggedUser;

    @Autowired
    PlayerService playerService;
    @Autowired
    CountryService countryService;
    @Autowired
    OccupationService occupationService;

    @RequestMapping("/login")
    public String login(String login, String password) {
        this.loggedUser = login.trim();
        if (isRegistered(loggedUser)) {
            if (("heslo").equals(password) && (this.loggedUser.length() > 0 && this.loggedUser.length() <= 32)) {
                return "redirect:/gamestudio";
            } else {
                this.loggedUser = null;
                return "redirect:/gamestudio";
            }
        } else {
            this.loggedUser = null;
            return "redirect:/registration";
        }
    }

    @RequestMapping("/logout")
    public String login() {
        this.loggedUser=null;
        return "redirect:/gamestudio";
    }

    @RequestMapping("/registration")
    public String register(Model model) {
        prepareModel(model);
        return "registration";
    }

    @RequestMapping("/addPlayer")
    public String newPlayer(String username, String fullname, int evaluation, int country, int occupation) {
        if (validOfUN(username) && validOfFN(fullname) && validOfSE(evaluation) && validOfCO(country) && validOfOC(occupation)) {

            List<Country> countries = countryService.getCountries();
            List<Occupation> occupations = occupationService.getOccupations();

            Player newPlayer = new Player(username, fullname, evaluation, countries.get(country), occupations.get(occupation));
            playerService.addPlayer(newPlayer);

            return "redirect:/gamestudio";
        } else {
            return "redirect:/registration";
        }
    }

    private boolean validOfUN(String username) {
        return (username.trim().length()) > 0 && (username.trim().length()) <= 32;
    }

    private boolean validOfFN(String fullname) {
        return (fullname.trim().length()) > 0 && (fullname.trim().length()) <= 128;
    }

    private boolean validOfSE(int evaluation) {
        return (evaluation > 0 && evaluation <= 10);
    }

    private boolean validOfCO(int country) {
        int countrySize = countryService.getCountries().size();
        return country >= 0 && country <= countrySize;
    }

    private boolean validOfOC(int occupation) {
        int occupationSize = occupationService.getOccupations().size();
        return occupation >=0 && occupation <= occupationSize;

    }

    public String getLoggedUser(){
        return loggedUser;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }

    public String getCountry(Country country) {
        return country.getCountry();
    }

    public String getOccupation(Occupation occupation) {
        return occupation.getOccupation();
    }

    public boolean isRegistered(String userName) {
        List<Player> players = playerService.getPlayersByUserName(userName);
        return players.size() != 0;
    }

    private void prepareModel(Model model) {
        model.addAttribute("countries", countryService.getCountries());   //pridame do modelu zoznam s krajinami
        model.addAttribute("occupations", occupationService.getOccupations());   //pridame do modelu zoznam s profesiami
    }
}
