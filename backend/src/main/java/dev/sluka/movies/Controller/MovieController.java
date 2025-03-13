package dev.sluka.movies.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.sluka.movies.DTO.MovieDTO;
import dev.sluka.movies.Service.MovieService;

@RestController
@RequestMapping("/movies")
    public class MovieController {

@Autowired
    private MovieService movieService;

   
@GetMapping("/movie/{imdbId}")
    public ResponseEntity<MovieDTO> getMovieByImdbId(@PathVariable String imdbId) {
        MovieDTO movieDTO = movieService.getMovieByImdbId(imdbId);
        return ResponseEntity.ok(movieDTO);
    }
@GetMapping("/all")
    public List<MovieDTO> getAllMoviesDTOs() {
        return movieService.getAllMovies();
    }

}