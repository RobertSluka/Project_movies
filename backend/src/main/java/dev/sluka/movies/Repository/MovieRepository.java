package dev.sluka.movies.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.sluka.movies.Entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findMovieByImdbId(String imdbId);


    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.backdrops WHERE m.id = :movieId")
Movie findMovieWithBackdrops(@Param("movieId") Long movieId);

    
}