import axios from 'axios';

// Create axios instance with default config
const api = axios.create({
  baseURL: process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080',
  timeout: parseInt(process.env.REACT_APP_API_TIMEOUT) || 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

/**
 * Request interceptor to add JWT token to every request
 * Token is retrieved from localStorage and added as Authorization header
 */
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

/**
 * Response interceptor to handle token expiration and common errors
 */
api.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
    // Handle 401 Unauthorized - token may have expired
    if (error.response?.status === 401) {
      localStorage.removeItem('authToken');
      localStorage.removeItem('authUser');
      localStorage.removeItem('userRole');
      // Redirect to login
      window.location.href = '/login';
    }

    return Promise.reject(error.response?.data || error.message);
  }
);

export default api;

