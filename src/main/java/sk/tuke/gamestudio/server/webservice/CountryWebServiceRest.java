package sk.tuke.gamestudio.server.webservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Country;
import sk.tuke.gamestudio.service.CountryService;

import java.util.List;

@RestController
@RequestMapping("/api/country")
public class CountryWebServiceRest {

    @Autowired
    private CountryService countryService;

    //http://localhost:8080/api/country/minesweeper
    @GetMapping("/{game}")
    public List<Country> getCountries(@PathVariable String game) {
        return countryService.getCountries();
    }

    @PostMapping
    public void addCountry(@RequestBody Country country) {
        countryService.addCountry(country);
    }
}
