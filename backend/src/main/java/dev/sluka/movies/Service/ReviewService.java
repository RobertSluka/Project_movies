package dev.sluka.movies.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.sluka.movies.DTO.ReviewDTO;
import dev.sluka.movies.Entity.Movie;
import dev.sluka.movies.Entity.Review;
import dev.sluka.movies.Repository.MovieRepository;
import dev.sluka.movies.Repository.ReviewRepository;
import jakarta.transaction.Transactional;


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Transactional
    public ReviewDTO createReview(String reviewBody, String imdbId) {
        Movie movie = movieRepository.findMovieByImdbId(imdbId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

                Review review = new Review(reviewBody, movie);
                Review savedReview = reviewRepository.save(review);
            
                return new ReviewDTO(savedReview);
    }

    @Transactional
    public List<ReviewDTO> getReviewsByMovieId(String imdbId) {
        return reviewRepository.findReviewsByImdbId(imdbId)
            .stream()
            .map(review -> new ReviewDTO(review))
            .toList();
    }


}
