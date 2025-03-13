import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true, // Enable credentials for CORS

});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  console.log('Using Token:', token); // Debug: Check token

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;