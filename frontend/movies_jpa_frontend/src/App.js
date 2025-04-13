import './App.css';
import React from 'react';
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
import { AuthProvider, useAuth } from './context/AuthProvider';
import AdminPanel from "./components/adminPanel/adminPanel.js"

function AppContent() {
  const [movies, setMovies] = useState();
  const [movie, setMovie] = useState();
  const [reviews, setReviews] = useState([]);
  const { auth } = useAuth();

  const getMovies = async () => {
    try {
      const response = await api.get("/movies/all");
      console.log("Response:", response);
      console.log("Movies loaded:", response.data);
      setMovies(response.data);
    } 
    catch(err) {
      console.log("Error fetching movies:", err);
    }
  }

  const getMovieData = useCallback(async (movieId) => {
    try {
      const movieResponse = await api.get(`/movies/movie/${movieId}`);
      setMovie(movieResponse.data);
    } catch (error) {
      console.error("Error fetching movie data or reviews:", error);
    }
  }, [setMovie,setReviews]);

  useEffect(() => {
    if (auth?.accessToken) {
      getMovies();
    }
  }, [auth?.accessToken]);

  return (
    <div className="App">
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<Home movies={movies} />} />
          <Route path="/watchlist" element={<WatchList movies={movies} />} />
          <Route path="/Trailer/:ytTrailerId" element={<Trailer/>} />
          <Route path="/Reviews/:movieId" element={
            <Reviews 
              getMovieData={getMovieData} 
              movie={movie} 
              reviews={reviews} 
              setReviews={setReviews}
            />
          } />
          <Route path="/userPanel" element={<AdminPanel />} />
        </Route>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="*" element={<NotFound/>} />
      </Routes> 
    </div>
  );
}

function App() {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
}

export default App;