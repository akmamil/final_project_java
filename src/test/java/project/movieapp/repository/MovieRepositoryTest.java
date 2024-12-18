package project.movieapp.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import project.movieapp.entity.Movie;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void testFindByTitleContainingIgnoreCase() {
        Movie movie1 = new Movie();
        movie1.setTitle("Время приключений");
        movie1.setGenre("Мультфильм");
        movieRepository.save(movie1);

        Movie movie2 = new Movie();
        movie2.setTitle("Время");
        movie2.setGenre("Боевик");
        movieRepository.save(movie2);

        List<Movie> result = movieRepository.findByTitleContainingIgnoreCase("время");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Movie::getTitle).contains("Время приключений", "Время");
    }

    @Test
    void testFindByGenreContainingIgnoreCaseWithPagination() {
        for (int i = 1; i <= 5; i++) {
            Movie movie = new Movie();
            movie.setTitle("Фильм " + i);
            movie.setGenre("Боевик");
            movieRepository.save(movie);
        }

        Page<Movie> result = movieRepository.findByGenreContainingIgnoreCase("боевик", PageRequest.of(0, 3));

        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getTotalElements()).isEqualTo(5);
    }
}
