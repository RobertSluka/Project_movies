import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import MovieCard from '../movieCard/MovieCard';
import Chat from '../chat/Chat';
import { useAuth } from '../../context/AuthProvider';

const Home = ({ movies }) => {
    const { auth } = useAuth();

    return (
        <Container>
            <Row className="mb-4">
                {movies?.map((movie) => (
                    <Col key={movie.imdbId} xs={12} sm={6} md={4} lg={3} className="mb-4">
                        <MovieCard movie={movie} />
                    </Col>
                ))}
            </Row>
            {auth?.isAuthenticated && (
                <Row>
                    <Col>
                        <Chat />
                    </Col>
                </Row>
            )}
        </Container>
    );
};

// const Home = ({ movies }) => {
//   console.log('Movies:', movies);  // Debugging line

//   return (
//     <div>
//       {movies?.map((movie) => (
//         <div key={movie.imdbId}>
//           <h2>{movie.title}</h2>
//           <p>Release Date: {movie.releaseDate}</p>
//         </div>
//       ))}
//     </div>
//   );
// };


export default Home;
