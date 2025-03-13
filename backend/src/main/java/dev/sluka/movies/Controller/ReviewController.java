package dev.sluka.movies.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.sluka.movies.DTO.ReviewDTO;
import dev.sluka.movies.Service.ReviewService;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Map<String, String> payload) {
        try {
            ReviewDTO review = reviewService.createReview(payload.get("reviewBody"), payload.get("imdbId"));
            return ResponseEntity.status(HttpStatus.CREATED).body(review);
        } catch (Exception e) {
            e.printStackTrace(); // 🔍 Print full error in logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{imdbId}")
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable String imdbId) {
    List<ReviewDTO> reviews = reviewService.getReviewsByMovieId(imdbId);
    return ResponseEntity.ok(reviews);
}
}
 