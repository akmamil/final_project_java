package project.movieapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import project.movieapp.entity.Movie;
import project.movieapp.service.MovieService;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Movie> getAllMovies(Pageable pageable) {
        return service.getAllMovies(pageable);
    }
    //возвращает объект типа Page<Movie>,
    // который представляет собой одну страницу данных
    // из общего результата.

    @GetMapping("/search")
    public List<Movie> searchMoviesByTitle(@RequestParam String title) { //Spring автоматически преобразует List<Movie>
        return service.searchMoviesByTitle(title);
    }

    @GetMapping("/filter")
    public Page<Movie> filterMoviesByGenre(@RequestParam String genre, Pageable pageable) { // Извлекает параметр из строки запроса
        return service.filterMoviesByGenre(genre, pageable);
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) { // Извлекает данные из тела HTTP-запроса (обычно JSON).
        Movie savedMovie = service.addMovie(movie);
        return new ResponseEntity<>(savedMovie, HttpStatus.CREATED); //201
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        Movie updatedMovie = service.updateMovie(id, movie);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) { // Извлекает переменную из URL.
        service.deleteMovie(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204
    }
}