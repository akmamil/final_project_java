package project.movieapp;

import project.movieapp.entity.Movie;
import project.movieapp.repository.MovieRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MovieAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieAppApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(MovieRepository movieRepository) {
        return args -> {
            movieRepository.save(new Movie(null, "Titanic", "Drama", 1997, 8.5));
            movieRepository.save(new Movie(null, "Hunger Games", "Action", 2012, 9.0));
        };
    }
}