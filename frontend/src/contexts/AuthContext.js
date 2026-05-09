import React, { createContext, useState, useContext, useEffect } from 'react';
import AuthService from '../services/authService';

/**
 * AuthContext - Provides authentication state and methods to the entire app
 */
const AuthContext = createContext(null);

/**
 * AuthProvider Component - Wraps the app and provides authentication context
 */
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  /**
   * Initialize user from localStorage on mount
   */
  useEffect(() => {
    const initializeAuth = () => {
      const token = localStorage.getItem('authToken');
      const userId = localStorage.getItem('userId');
      const userRole = localStorage.getItem('userRole');

      if (token && userId) {
        setUser({
          userId,
          role: userRole,
          token,
        });
      }
      setLoading(false);
    };

    initializeAuth();
  }, []);

  /**
   * Register new user
   */
  const register = async (fullName, email, password, companyId) => {
    setError(null);
    try {
      const response = await AuthService.register(fullName, email, password, companyId);
      setUser({
        userId: response.data.userId,
        role: response.data.role,
        token: response.data.token,
      });
      return response;
    } catch (err) {
      const errorMessage = err.message || 'Registration failed';
      setError(errorMessage);
      throw err;
    }
  };

  /**
   * Login user
   */
  const login = async (email, password) => {
    setError(null);
    try {
      const response = await AuthService.login(email, password);
      setUser({
        userId: response.data.userId,
        role: response.data.role,
        token: response.data.token,
      });
      return response;
    } catch (err) {
      const errorMessage = err.message || 'Login failed';
      setError(errorMessage);
      throw err;
    }
  };

  /**
   * Logout user
   */
  const logout = async () => {
    await AuthService.logout();
    setUser(null);
    setError(null);
  };

  const value = {
    user,
    loading,
    error,
    register,
    login,
    logout,
    isAuthenticated: !!user,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

/**
 * Custom hook to use auth context
 */
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};

export default AuthContext;

