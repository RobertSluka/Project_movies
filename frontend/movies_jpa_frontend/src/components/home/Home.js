import Hero from '../hero/Hero';

const Home = ({ movies }) => {
  console.log("Movies in Home:", movies ?? "No movies or undefined");


  return (
    <div>
      <h1>Movie List</h1>

      {/* Render Hero only if movies is an array */}
      {Array.isArray(movies) && movies.length > 0 ? (
        <Hero movies={movies} />
      ) : (
        <p>Loading...</p>
      )}

      {/* Safe mapping with null checks */}
      {Array.isArray(movies) &&
        movies.map((movie) => (
          <div key={movie.imdbId}>
            <h2>{movie.title}</h2>
            <p>Release Date: {movie.releaseDate}</p>
          </div>
        ))}
    </div>
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
