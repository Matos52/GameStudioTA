package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sk.tuke.gamestudio.minesweeper.consoleui.ConsoleUI;

@SpringBootApplication
public class SpringClient {
    public static void main(String[] args) {
        SpringApplication.run(SpringClient.class);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI console) {
        return s -> console.play();
    }

    @Bean
    public ConsoleUI console() {
        return new ConsoleUI();
    }
}
