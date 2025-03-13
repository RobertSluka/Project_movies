package dev.sluka.movies.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "movies")
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false) // Ensure uniqueness
    private Long id;

    @Column(name = "imdb_id", unique = true) // IMDb ID should also be unique
    private String imdbId;

    private String title;
    private String releaseDate;
    private String trailerLink;
    private String poster;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "genre")
    private List<String> genres;  // Keep this as a list

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_backdrops", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "backdrop")
    private List<String> backdrops;

    // One-to-Many relationship with Review
    @JsonIgnore
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
    
    @JsonProperty("backdrops")  // Ensures JSON always includes `backdrops`
public List<String> getBackdrops() {
    return backdrops != null ? backdrops : new ArrayList<>();
}
}


