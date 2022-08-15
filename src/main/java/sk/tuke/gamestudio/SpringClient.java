package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.kamene.consoleUI.ConsoleUIKamene;
import sk.tuke.gamestudio.minesweeper.PlaygroundJPA;
import sk.tuke.gamestudio.minesweeper.consoleui.ConsoleUI;
import sk.tuke.gamestudio.service.*;

@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "sk.tuke.gamestudio.server.*"))
public class SpringClient {
    public static void main(String[] args) {
//        SpringApplication.run(SpringClient.class);
       new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    //Sluzi na spustenie testovacej triedy Playground
    //@Bean
    public CommandLineRunner runnerJPA(PlaygroundJPA console) {
        return s -> console.play();
    }

    //Sluzi na spustenie hry Minesweeper
    //@Bean
    public CommandLineRunner runner(ConsoleUI console) {
        return s -> console.play();
    }


    //Sluzi na spustenie hry Kamene
    @Bean
    public CommandLineRunner runner(ConsoleUIKamene consoleUIKamene) {
        return s -> consoleUIKamene.play();
    }

    @Bean
    public PlaygroundJPA consoleJPA() {
        return new PlaygroundJPA();
    }

    @Bean
    public ConsoleUI console() {
        return new ConsoleUI();
    }

    @Bean
    public  ConsoleUIKamene consoleUIKamene() {
        return new ConsoleUIKamene();
    }

    @Bean
    public ScoreService scoreService() {
//        return new ScoreServiceJDBC();
        return new ScoreServiceJPA();
//        return new ScoreServiceRest();
    }

    @Bean
    public CommentService commentService() {
//        return new CommentServiceJDBC();
        return new CommentServiceJPA();
//        return new CommentServiceRest();
    }

    @Bean
    public RatingService ratingService() {
//        return new RatingServiceJDBC();
        return new RatingServiceJPA();
//        return new RatingServiceRest();
    }

    @Bean
    public StudentServiceJPA studentServiceJPA() {
        return new StudentServiceJPA();
    }

    @Bean
    public StudyGroupServiceJPA studyGroupServiceJPA() {
        return new StudyGroupServiceJPA();
    }

    @Bean
    public CountryServiceJPA countryServiceJPA() {
        return new CountryServiceJPA();
    }

    @Bean
    public OccupationServiceJPA occupationServiceJPA() {
        return new OccupationServiceJPA();
    }

    @Bean
    public PlayerServiceJPA playerServiceJPA() {
        return new PlayerServiceJPA();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
