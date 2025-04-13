import React from 'react';
import { Card } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const MovieCard = ({ movie }) => {
    return (
        <Card className="h-100" style={{ backgroundColor: '#1e1e1e', color: '#ffffff', borderColor: '#444' }}>
            <Card.Img 
                variant="top" 
                src={movie.poster} 
                alt={movie.title}
                style={{ height: '400px', objectFit: 'cover' }}
            />
            <Card.Body>
                <Card.Title>{movie.title}</Card.Title>
                <Card.Text>
                    <small className="text-muted">Release Date: {movie.releaseDate}</small>
                </Card.Text>
                <Link to={`/Reviews/${movie.imdbId}`} className="btn btn-primary">
                    View Details
                </Link>
            </Card.Body>
        </Card>
    );
};

export default MovieCard; 