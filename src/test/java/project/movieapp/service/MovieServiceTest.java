package project.movieapp.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import project.movieapp.entity.Movie;
import project.movieapp.repository.MovieRepository;

import java.util.List;
import java.util.Optional;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        movieService = new MovieService(movieRepository);
    }

    @Test
    void testGetAllMovies() {
        Movie movie = new Movie(1L, "Inception", "Sci-Fi", 2010, 8.8);
        List<Movie> movies = List.of(movie);
        Page<Movie> page = new PageImpl<>(movies);
        Pageable pageable = PageRequest.of(0, 10);
        when(movieRepository.findAll(pageable)).thenReturn(page);

        Page<Movie> result = movieService.getAllMovies(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Inception", result.getContent().get(0).getTitle());
    }

    @Test
    void testSearchMoviesByTitle() {
        Movie movie = new Movie(1L, "Inception", "Sci-Fi", 2010, 8.8);
        when(movieRepository.findByTitleContainingIgnoreCase("Inception")).thenReturn(List.of(movie));

        List<Movie> result = movieService.searchMoviesByTitle("Inception");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
    }

    @Test
    void testFilterMoviesByGenre() {
        Movie movie = new Movie(1L, "Inception", "Sci-Fi", 2010, 8.8);
        List<Movie> movies = List.of(movie);
        Page<Movie> page = new PageImpl<>(movies);
        Pageable pageable = PageRequest.of(0, 10);
        when(movieRepository.findByGenreContainingIgnoreCase("Sci-Fi", pageable)).thenReturn(page);

        Page<Movie> result = movieService.filterMoviesByGenre("Sci-Fi", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Inception", result.getContent().get(0).getTitle());
    }

    @Test
    void testAddMovie() {
        Movie movie = new Movie(null, "Interstellar", "Sci-Fi", 2014, 8.6);
        when(movieRepository.save(movie)).thenReturn(movie);

        Movie result = movieService.addMovie(movie);

        assertNotNull(result);
        assertEquals("Interstellar", result.getTitle());
    }

    @Test
    void testUpdateMovie() {
        Movie existingMovie = new Movie(1L, "Inception", "Sci-Fi", 2010, 8.8);
        Movie updatedMovie = new Movie(1L, "Inception", "Sci-Fi", 2010, 9.0);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(existingMovie)).thenReturn(updatedMovie);
        Movie result = movieService.updateMovie(1L, updatedMovie);

        assertNotNull(result);
        assertEquals(9.0, result.getRating());
    }

    @Test
    void testUpdateMovieNotFound() {
        Movie updatedMovie = new Movie(1L, "Inception", "Sci-Fi", 2010, 9.0);
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                movieService.updateMovie(1L, updatedMovie)
        );
        assertEquals("Movie not found", exception.getMessage());
    }

    @Test
    void testDeleteMovie() {
        Long movieId = 1L;

        movieService.deleteMovie(movieId);

        verify(movieRepository, times(1)).deleteById(movieId);
    }
}