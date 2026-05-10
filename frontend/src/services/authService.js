import axiosInstance from '../lib/axios';

const persistAuth = (authData) => {
  if (!authData?.token) return;

  localStorage.setItem('token', authData.token);
  if (authData.userId) localStorage.setItem('userId', authData.userId);
  if (authData.role) localStorage.setItem('userRole', authData.role);
  localStorage.setItem('tokenExpiresAt', authData.expiresAt);
};

const persistUser = (user) => {
  if (!user) return;

  localStorage.setItem('authUser', JSON.stringify(user));
  if (user.id) localStorage.setItem('userId', user.id);
  if (user.role) localStorage.setItem('userRole', user.role);
  if (user.companyId) localStorage.setItem('companyId', user.companyId);
};

const clearAuth = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('authToken');
  localStorage.removeItem('authUser');
  localStorage.removeItem('userId');
  localStorage.removeItem('userRole');
  localStorage.removeItem('companyId');
  localStorage.removeItem('tokenExpiresAt');
};

const unwrap = (response) => response.data?.data;

const AuthService = {
  register: async (fullName, email, password, companyId) => {
    const response = await axiosInstance.post('/auth/register', {
      fullName,
      email,
      password,
      companyId: companyId || null,
    });
    const authData = unwrap(response);
    persistAuth(authData);
    return authData;
  },

  login: async (email, password) => {
    const response = await axiosInstance.post('/auth/login', {
      email,
      password,
    });
    const authData = unwrap(response);
    persistAuth(authData);
    return authData;
  },

  refreshToken: async () => {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('No token found');
    }

    const response = await axiosInstance.post('/auth/refresh', {}, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    const authData = unwrap(response);
    persistAuth(authData);
    return authData;
  },

  getCurrentUser: async () => {
    const response = await axiosInstance.get('/auth/me');
    const user = unwrap(response);
    persistUser(user);
    return user;
  },

  logout: async () => {
    try {
      await axiosInstance.post('/auth/logout');
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      clearAuth();
    }
  },

  isAuthenticated: () => {
    const token = localStorage.getItem('token');
    const expiresAt = localStorage.getItem('tokenExpiresAt');

    if (!token || !expiresAt) {
      return false;
    }

    return Date.now() < parseInt(expiresAt, 10);
  },

  getCurrentUserLocal: () => ({
    userId: localStorage.getItem('userId'),
    role: localStorage.getItem('userRole'),
    token: localStorage.getItem('token'),
  }),

  getToken: () => localStorage.getItem('token'),
};

export default AuthService;
