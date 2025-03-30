import { useEffect, useRef } from 'react';
import api from '../../api/axiosConfig';
import { useParams } from 'react-router-dom';
import { Container, Row, Col } from 'react-bootstrap';
import ReviewForm from '../reviewForm/reviewForm';
import React from 'react';

const Reviews = ({ getMovieData, movie, watchlist, setWatchList }) => {
    const revText = useRef();
    let params = useParams();
    const movieId = params.movieId;

    useEffect(() => {
        getMovieData(movieId);
    }, [movieId]); 

    const addMovie = async (e) => {
        e.preventDefault();
        const rev = revText.current.value.trim();

        if (!rev) {
            alert("Review cannot be empty");
            return;
        }

        try {
            const response = await api.post("/api/v1/reviews", {
                reviewBody: rev,
                imdbId: movieId
            });

            const newReview = { body: response.data.body };

            setTimeout(() => {
                setReviews((prevReviews) => (Array.isArray(prevReviews) ? [...prevReviews, newReview] : [newReview]));
            }, 100);

            revText.current.value = ""; // ✅ Reset input after success
        } catch (err) {
            console.error("Error submitting review:", err.response?.data || err.message);
            alert("Error: " + JSON.stringify(err.response?.data || err.message));
        }
    };

    return (
        <Container>
            <Row>
                <Col><h3>Watch List</h3></Col>
            </Row>
            <Row className="mt-2">
                <Col>
                    <img src={movie?.poster} alt={movie?.title || "Movie Poster"} />
                </Col>
                <Col>
                    {movie && (
                        <>
                            <Row>
                                <Col>
                                    <h1>{movie?.title}</h1>
                                    <p>Genres: {movie?.genres?.join(', ')}</p>
                                    <p>Release Date: {movie?.releaseDate}</p>
                                </Col>
                            </Row>
                            <Row>
                                <Col>
                                    <hr />
                                </Col>
                            </Row>
                        </>
                    )}
                    <>
                        <Row>
                            <Col>
                                <ReviewForm handleSubmit={addReview} revText={revText} labelText="Delete from list" />
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <hr />
                            </Col>
                        </Row>
                    </>
                    {reviews?.map((r, index) => (
                        <React.Fragment key={index}>
                            <Row>
                                <Col>{r.body}</Col>
                            </Row>
                            <Row>
                                <Col><hr /></Col>
                            </Row>
                        </React.Fragment>
                    ))}
                </Col>
            </Row>
            <Row>
                <Col>
                    <hr />
                </Col>
            </Row>
        </Container>
    );
};

export default Reviews;
