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

const BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true
});

// Add request interceptor to add JWT token
api.interceptors.request.use(
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

// Add response interceptor to handle token refresh
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // If error is 401 and we haven't tried to refresh token yet
    if (error.response?.status === 401 && !originalRequest._retry) {
      console.log("Token expired or invalid, attempting refresh...");
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem('refreshToken');
        if (!refreshToken) {
          console.log("No refresh token found, redirecting to login");
          localStorage.removeItem('accessToken');
          window.location.href = '/login';
          return Promise.reject(error);
        }

        console.log("Attempting to refresh token...");
        // Try to refresh the token
        const response = await axios.post(`${BASE_URL}/refresh-token`, 
          { refreshToken },
          { withCredentials: true }
        );

        console.log("Token refresh successful:", response.data);
        const { accessToken, refreshToken: newRefreshToken } = response.data;
        
        // Store the new tokens
        localStorage.setItem('accessToken', accessToken);
        if (newRefreshToken) {
          localStorage.setItem('refreshToken', newRefreshToken);
        }

        // Retry the original request with new token
        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        console.error("Token refresh failed:", refreshError);
        // Refresh failed, user needs to log in again
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
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