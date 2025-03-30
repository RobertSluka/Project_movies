import './App.css';
import api from './api/axiosConfig';
import {useState, useEffect} from 'react';
import Layout from './components/Layout';
import {Routes, Route} from 'react-router-dom';
import Home from './components/home/Home';
import Header from './components/header/Header';
import WatchList from './components/watchList/watchList';
import Trailer from './components/trailer/Trailer';
import Reviews from './components/reviews/Reviews';
import NotFound from './components/notFound/NotFound';
import Login from './components/login/Login';
import Register from './components/register/Register';
import { useCallback } from 'react';
function App() {
 
  const [movies, setMovies] = useState();
  const [movie, setMovie] = useState();
  const [reviews, setReviews] = useState([]);

  const getMovies = async () =>{
    console.log("Calling /movies/all first time ");
    try
    {
    

    
      console.log("Calling /movies/all second time");
      const response = await api.get("/movies/all");
      console.log("Response:", response);
      console.log("Movies loaded:", response.data);

      setMovies(response.data);

    } 
    catch(err)
    {
      console.log(err);
    }
  }

  const getMovieData = useCallback(async (movieId) => {
     
    try {
      const movieResponse = await api.get(`/movies/movie/${movieId}`);
      setMovie(movieResponse.data);
      
      const reviewsResponse = await api.get(`/api/v1/reviews/${movieId}`);
      setReviews(reviewsResponse.data || []);

       if (JSON.stringify(reviews) !== JSON.stringify(reviewsResponse.data)) {
            setReviews(reviewsResponse.data || []);
        }

  } catch (error) {
      console.error("Error fetching movie data or reviews:", error);
  }

  }, [setMovie,setReviews]);

  useEffect(() => {
    getMovies();
  },[])
  

  return (
    <div className="App">
      <Header/>
      <Routes>
          <Route path="/" element={<Layout/>}>
            <Route path="/" element={<Home movies={movies} />} ></Route>
            <Route path="/" element={<WatchList movies={movies} />} ></Route>
            <Route path="/Trailer/:ytTrailerId" element={<Trailer/>}></Route>
            <Route path="/Reviews/:movieId" element ={<Reviews getMovieData = {getMovieData} movie={movie} reviews ={reviews} setReviews = {setReviews} />}></Route>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="*" element = {<NotFound/>}></Route>
          </Route>
      </Routes> 


    </div>
  );
}

export default App;