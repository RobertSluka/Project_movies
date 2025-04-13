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
import { jwtDecode } from 'jwt-decode';

const BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true
});

// Add request interceptor to handle token refresh
api.interceptors.request.use(async (config) => {
  const token = localStorage.getItem('accessToken');
  const refreshToken = localStorage.getItem('refreshToken');

  if (token) {
    try {
      const decoded = jwtDecode(token);
      const now = Date.now() / 1000;

      // If token is expired and we have a refresh token, get a new one
      if (decoded.exp < now && refreshToken) {
        try {
          const response = await axios.post(`${BASE_URL}/refresh-token`, {
            refreshToken: refreshToken
          });

          const newAccessToken = response.data.accessToken;
          const newRefreshToken = response.data.refreshToken;

          // Update tokens in localStorage
          localStorage.setItem('accessToken', newAccessToken);
          localStorage.setItem('refreshToken', newRefreshToken);

          // Update the Authorization header with the new token
          config.headers.Authorization = `Bearer ${newAccessToken}`;
        } catch (refreshError) {
          console.error('Token refresh failed:', refreshError);
          // If refresh fails, clear tokens and redirect to login
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
          window.location.href = '/login';
          return Promise.reject(refreshError);
        }
      } else {
        // Token is still valid, use it
        config.headers.Authorization = `Bearer ${token}`;
      }
    } catch (e) {
      console.error('Token decode failed:', e);
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      window.location.href = '/login';
      return Promise.reject(e);
    }
  }

  return config;
});

// Add response interceptor to handle 401 errors
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response && error.response.status === 401) {
      // If we get a 401, clear tokens and redirect to login
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;

export const axiosPrivate = axios.create({
  baseURL: BASE_URL,
  headers: { 
    'Content-Type': 'application/json' 
  },
  withCredentials: true
});

// Add the same interceptors to axiosPrivate
axiosPrivate.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

axiosPrivate.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem('refreshToken');
        if (!refreshToken) {
          localStorage.removeItem('accessToken');
          window.location.href = '/login';
          return Promise.reject(error);
        }

        const response = await axios.post(`${BASE_URL}/refresh-token`, 
          { refreshToken },
          { withCredentials: true }
        );

        const { accessToken, refreshToken: newRefreshToken } = response.data;
        
        localStorage.setItem('accessToken', accessToken);
        if (newRefreshToken) {
          localStorage.setItem('refreshToken', newRefreshToken);
        }

        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        return axiosPrivate(originalRequest);
      } catch (refreshError) {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

// api.interceptors.request.use((config) => {
//   const token = localStorage.getItem('accessToken');
//   console.log('Using Token:', token); // Debug: Check token

//   if (token) {
//     config.headers.Authorization = `Bearer ${token}`;
//   }
//   return config;
// });

// export default api;