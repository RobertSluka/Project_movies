package dev.sluka.movies.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.sluka.movies.DTO.MovieDTO;
import dev.sluka.movies.Entity.Movie;
import dev.sluka.movies.Repository.MovieRepository;
import jakarta.transaction.Transactional;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> allMovies() {
        return movieRepository.findAll();
    }
    public Optional<Movie> singleMovie(String imdbId) {
        return movieRepository.findMovieByImdbId(imdbId);
    }
    @Transactional 
    public List<MovieDTO> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();

        // Force Hibernate to initialize lazy collections
        movies.forEach(movie -> {
            movie.getGenres().size();  // Trigger lazy loading
            movie.getBackdrops().size();  // Trigger lazy loading
        });

        return movies.stream()
                .map(MovieDTO::new)
                .collect(Collectors.toList());
    }
    public Movie getMovieWithBackdrops(Long movieId) {
        return movieRepository.findMovieWithBackdrops(movieId);
    }
    
    @Transactional 
    public MovieDTO getMovieByImdbId(String imdbId) {
        Movie movie = movieRepository.findMovieByImdbId(imdbId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

         movie.getGenres().size();     // Force initialization
         movie.getBackdrops().size(); 
        return new MovieDTO(movie);  // Convert to DTO
    }
    
    
}