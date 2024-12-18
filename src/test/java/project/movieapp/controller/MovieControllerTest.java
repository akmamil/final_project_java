package project.movieapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import project.movieapp.entity.Movie;
import project.movieapp.service.MovieService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private Movie sampleMovie;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleMovie = new Movie();
        sampleMovie.setId(1L);
        sampleMovie.setTitle("Sample Movie");
        sampleMovie.setGenre("Action");
    }

    @Test
    void testGetAllMovies() {
        Page<Movie> moviesPage = new PageImpl<>(List.of(sampleMovie));
        when(movieService.getAllMovies(any(Pageable.class))).thenReturn(moviesPage);

        Page<Movie> result = movieController.getAllMovies(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Movie", result.getContent().get(0).getTitle());
        verify(movieService, times(1)).getAllMovies(any(Pageable.class));
    }

    @Test
    void testSearchMoviesByTitle() {
        List<Movie> movies = Collections.singletonList(sampleMovie);
        when(movieService.searchMoviesByTitle("Sample")).thenReturn(movies);

        List<Movie> result = movieController.searchMoviesByTitle("Sample");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Sample Movie", result.get(0).getTitle());
        verify(movieService, times(1)).searchMoviesByTitle("Sample");
    }

    @Test
    void testFilterMoviesByGenre() {
        Page<Movie> moviesPage = new PageImpl<>(List.of(sampleMovie));
        when(movieService.filterMoviesByGenre(eq("Action"), any(Pageable.class))).thenReturn(moviesPage);

        Page<Movie> result = movieController.filterMoviesByGenre("Action", Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Action", result.getContent().get(0).getGenre());
        verify(movieService, times(1)).filterMoviesByGenre(eq("Action"), any(Pageable.class));
    }

    @Test
    void testAddMovie() {
        when(movieService.addMovie(any(Movie.class))).thenReturn(sampleMovie);

        ResponseEntity<Movie> response = movieController.addMovie(sampleMovie);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Sample Movie", response.getBody().getTitle());
        verify(movieService, times(1)).addMovie(any(Movie.class));
    }

    @Test
    void testUpdateMovie() {
        when(movieService.updateMovie(eq(1L), any(Movie.class))).thenReturn(sampleMovie);

        ResponseEntity<Movie> response = movieController.updateMovie(1L, sampleMovie);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Sample Movie", response.getBody().getTitle());
        verify(movieService, times(1)).updateMovie(eq(1L), any(Movie.class));
    }

    @Test
    void testDeleteMovie() {
        doNothing().when(movieService).deleteMovie(1L);

        ResponseEntity<Void> response = movieController.deleteMovie(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(movieService, times(1)).deleteMovie(1L);
    }
}



