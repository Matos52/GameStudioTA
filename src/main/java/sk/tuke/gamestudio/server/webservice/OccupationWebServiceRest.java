package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Occupation;
import sk.tuke.gamestudio.service.OccupationService;

import java.util.List;

@RestController
@RequestMapping("/api/occupation")
public class OccupationWebServiceRest {

    @Autowired
    private OccupationService occupationService;

    //http://localhost:8080/api/occupation/minesweeper
    @GetMapping("/{game}")
    public List<Occupation> getOccupations(@PathVariable String game) {
        return occupationService.getOccupations();
    }

    @PostMapping
    public void addOccupation(@RequestBody Occupation occupation) {
        occupationService.addOccupation(occupation);
    }
}
