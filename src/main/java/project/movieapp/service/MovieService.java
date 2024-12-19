package project.movieapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import project.movieapp.entity.Movie;
import project.movieapp.repository.MovieRepository;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    public Page<Movie> getAllMovies(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Movie> searchMoviesByTitle(String title) {
        return repository.findByTitleContainingIgnoreCase(title);
    }

    public Page<Movie> filterMoviesByGenre(String genre, Pageable pageable) {
        return repository.findByGenreContainingIgnoreCase(genre, pageable);
    }

    public Movie addMovie(Movie movie) {
        return repository.save(movie);
    }

    public Movie updateMovie(Long id, Movie movie) {
        return repository.findById(id)
                .map(existingMovie -> {
                    existingMovie.setTitle(movie.getTitle());
                    existingMovie.setGenre(movie.getGenre());
                    existingMovie.setReleaseYear(movie.getReleaseYear());
                    existingMovie.setRating(movie.getRating());
                    return repository.save(existingMovie);
                })
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    public void deleteMovie(Long id) {
        repository.deleteById(id);
    }
}