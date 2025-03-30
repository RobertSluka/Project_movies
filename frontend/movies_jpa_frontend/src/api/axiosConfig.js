// import axios from 'axios';

// const api = axios.create({
//   baseURL: 'http://localhost:8080',
//   withCredentials: true, // Enable credentials for CORS

// });

// api.interceptors.request.use(async (config) => {
//   let token = localStorage.getItem('accessToken');
//   const refreshToken = localStorage.getItem('refreshToken');

 
//   if (token) {
//     try {
//       const decoded = jwtDecode(token);
//       const now = Date.now() / 1000;

//       if (decoded.exp < now && refreshToken) {
//         // Token expired -> get a new one
//         const res = await axios.post('http://localhost:8080/refresh-token', {
//           refreshToken,
//         });

//         const newToken = res.data.accessToken;
//         localStorage.setItem('accessToken', newToken);
//         token = newToken;
//       }

//       const token = localStorage.getItem('accessToken');
//       if (token) {
//         config.headers.Authorization = `Bearer ${token}`;
//       }

//     } catch (e) {
//       console.warn("Token decode or refresh failed:", e);
//       localStorage.removeItem('accessToken');
//       localStorage.removeItem('refreshToken');
//       window.location.href = '/login'; // Force logout
//     }
//   }

//   return config;
// });

// export default api;

import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true, // Enable credentials for CORS

});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
  console.log('Using Token:', token); // Debug: Check token

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;