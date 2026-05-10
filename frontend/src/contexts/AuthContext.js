import React, { createContext, useState, useContext, useEffect } from 'react';
import AuthService from '../services/authService';

const AuthContext = createContext(null);

const decodeUserFromToken = (token, emailFallback) => {
  if (!token) return null;

  const payload = JSON.parse(atob(token.split('.')[1]));
  console.log('JWT payload:', payload);

  const companyId = payload.companyId ?? payload.company_id ?? payload.company?.id;
  const id = payload.id ?? payload.userId ?? payload.user_id ?? payload.sub;

  return {
    id: id != null ? Number(id) : undefined,
    email: payload.email ?? emailFallback ?? localStorage.getItem('userEmail') ?? '',
    role: payload.role,
    companyId: companyId != null ? Number(companyId) : undefined,
  };
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const initializeAuth = () => {
      const storedToken = localStorage.getItem('token') || localStorage.getItem('authToken');

      if (storedToken) {
        if (!localStorage.getItem('token')) {
          localStorage.setItem('token', storedToken);
        }
        try {
          const decodedUser = decodeUserFromToken(storedToken);
          setToken(storedToken);
          setUser(decodedUser);
        } catch (err) {
          localStorage.removeItem('token');
          localStorage.removeItem('authToken');
          setToken(null);
          setUser(null);
        }
      }
      setLoading(false);
    };

    initializeAuth();
  }, []);

  const register = async (fullName, email, password, companyId) => {
    setError(null);
    try {
      const authData = await AuthService.register(fullName, email, password, companyId);
      localStorage.setItem('userEmail', email);
      const decodedUser = decodeUserFromToken(authData.token, email);
      setToken(authData.token);
      setUser(decodedUser);
      return authData;
    } catch (err) {
      const errorMessage = err.response?.data?.message || err.message || 'Registration failed';
      setError(errorMessage);
      throw err;
    }
  };

  const login = async (email, password) => {
    setError(null);
    try {
      const authData = await AuthService.login(email, password);
      localStorage.setItem('userEmail', email);
      const decodedUser = decodeUserFromToken(authData.token, email);
      setToken(authData.token);
      setUser(decodedUser);
      return authData;
    } catch (err) {
      const errorMessage = err.response?.data?.message || err.message || 'Login failed';
      setError(errorMessage);
      throw err;
    }
  };

  const logout = async () => {
    await AuthService.logout();
    setUser(null);
    setToken(null);
    setError(null);
  };

  const value = {
    user,
    token,
    loading,
    error,
    register,
    login,
    logout,
    isAuthenticated: !!user && !!token,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};

export default AuthContext;
