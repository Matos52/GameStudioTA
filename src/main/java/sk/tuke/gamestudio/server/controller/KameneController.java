package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.kamene.core.Field;
import sk.tuke.gamestudio.kamene.core.Tile;
import sk.tuke.gamestudio.service.ScoreService;

@Controller
@RequestMapping("/kamene")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class KameneController {

    private Field field = new Field(4, 4);

    private String GAME = "kamene";

    @Autowired
    ScoreService scoreService;

    @RequestMapping
    public String kamene(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column, Model model) {

        prepareModel(model);
        return "kamene";
    }

    @RequestMapping("/new")
    public String newGame(Model model){
        field = new Field(4,4);
        prepareModel(model);
        return "kamene";
    }

    public String getTileText(Tile tile) {
        return String.valueOf(tile.getValue());
    }

    public void prepareModel(Model model) {
        model.addAttribute("kameneField", field.getTiles());
    }
}
