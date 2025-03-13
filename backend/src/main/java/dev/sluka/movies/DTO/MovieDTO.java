package dev.sluka.movies.DTO;

import dev.sluka.movies.Entity.Movie;
import lombok.Data;

import java.util.List;

@Data
public class MovieDTO {
    private String imdbId;
    private String title;
    private List<String> genres;
    private String releaseDate;
    private String trailerLink;
    private String poster;
    private List<String> backdrops;

    // Constructor to convert Movie entity to MovieDTO
    public MovieDTO(Movie movie) {
        this.imdbId = movie.getImdbId();
        this.title = movie.getTitle();
        this.genres = movie.getGenres();
        this.releaseDate = movie.getReleaseDate();
        this.trailerLink = movie.getTrailerLink();
        this.poster = movie.getPoster();
        this.backdrops = movie.getBackdrops();
    }
}
