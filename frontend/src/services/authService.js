import api from './api';

/**
 * Authentication Service
 * Handles all auth-related API calls to the backend
 */
const AuthService = {
  /**
   * Register a new user
   * @param {string} fullName - User's full name
   * @param {string} email - User's email
   * @param {string} password - User's password
   * @param {number} companyId - Optional company ID
   * @returns {Promise} Response with token and user data
   */
  register: async (fullName, email, password, companyId) => {
    try {
      const response = await api.post('/api/auth/register', {
        fullName,
        email,
        password,
        companyId: companyId || null,
      });

      if (response.data?.token) {
        localStorage.setItem('authToken', response.data.token);
        localStorage.setItem('userId', response.data.userId);
        localStorage.setItem('userRole', response.data.role);
        localStorage.setItem('tokenExpiresAt', response.data.expiresAt);
      }

      return response;
    } catch (error) {
      throw error;
    }
  },

  /**
   * Login user
   * @param {string} email - User's email
   * @param {string} password - User's password
   * @returns {Promise} Response with token and user data
   */
  login: async (email, password) => {
    try {
      const response = await api.post('/api/auth/login', {
        email,
        password,
      });

      if (response.data?.token) {
        localStorage.setItem('authToken', response.data.token);
        localStorage.setItem('userId', response.data.userId);
        localStorage.setItem('userRole', response.data.role);
        localStorage.setItem('tokenExpiresAt', response.data.expiresAt);
      }

      return response;
    } catch (error) {
      throw error;
    }
  },

  /**
   * Refresh JWT token
   * @returns {Promise} Response with new token
   */
  refreshToken: async () => {
    try {
      const token = localStorage.getItem('authToken');
      if (!token) {
        throw new Error('No token found');
      }

      const response = await api.post('/api/auth/refresh', {}, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.data?.token) {
        localStorage.setItem('authToken', response.data.token);
        localStorage.setItem('tokenExpiresAt', response.data.expiresAt);
      }

      return response;
    } catch (error) {
      throw error;
    }
  },

  /**
   * Get current authenticated user
   * @returns {Promise} Response with current user data
   */
  getCurrentUser: async () => {
    try {
      const response = await api.get('/api/auth/me');
      return response;
    } catch (error) {
      throw error;
    }
  },

  /**
   * Logout user - clears local storage and calls backend logout
   */
  logout: async () => {
    try {
      await api.post('/api/auth/logout');
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      localStorage.removeItem('authToken');
      localStorage.removeItem('userId');
      localStorage.removeItem('userRole');
      localStorage.removeItem('tokenExpiresAt');
    }
  },

  /**
   * Check if user is authenticated
   * @returns {boolean} True if user has valid token
   */
  isAuthenticated: () => {
    const token = localStorage.getItem('authToken');
    const expiresAt = localStorage.getItem('tokenExpiresAt');

    if (!token || !expiresAt) {
      return false;
    }

    // Check if token is expired
    const now = Date.now();
    return now < parseInt(expiresAt);
  },

  /**
   * Get current user data from localStorage
   * @returns {object} User data
   */
  getCurrentUserLocal: () => {
    return {
      userId: localStorage.getItem('userId'),
      role: localStorage.getItem('userRole'),
      token: localStorage.getItem('authToken'),
    };
  },

  /**
   * Get stored auth token
   * @returns {string} JWT token
   */
  getToken: () => {
    return localStorage.getItem('authToken');
  },
};

export default AuthService;

