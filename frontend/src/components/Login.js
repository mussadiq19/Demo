import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import '../styles/auth.css';

/**
 * Login Component
 * Allows users to authenticate with email and password
 */
const Login = () => {
  const navigate = useNavigate();
  const { login, error: authError } = useAuth();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  /**
   * Handle form submission
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      // Validate inputs
      if (!email || !password) {
        setError('Please fill in all fields');
        setLoading(false);
        return;
      }

      // Call login
      await login(email, password);

      // Redirect to dashboard on success
      navigate('/');
    } catch (err) {
      setError(err.message || 'Login failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container min-h-screen bg-gradient-to-br from-brand-900 to-brand-700 flex items-center justify-center p-4">
      <div className="auth-card bg-white rounded-3xl shadow-2xl p-10 w-full max-w-md">
        <div className="text-center">
          <div className="auth-logo text-brand-600 text-2xl font-bold">⚡ Foresight</div>
          <div className="auth-subtitle text-xs text-gray-400 uppercase tracking-widest mt-1">Intelligent Business Operations</div>
          <div className="border-t border-surface-border my-6" />
        </div>

        <h2 className="auth-title text-center text-2xl font-semibold text-gray-900">Welcome Back</h2>
        <p className="auth-description text-center text-sm text-gray-500">Sign in to your Foresight account</p>

        <form onSubmit={handleSubmit} className="auth-form space-y-4 mt-6">
          {(error || authError) && (
            <div className="error-message bg-red-50 border border-red-200 text-red-600 text-sm rounded-xl px-4 py-3 mt-2">
              {error || authError}
            </div>
          )}

          <div className="form-group">
            <label htmlFor="email" className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1.5 block">Email Address</label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="your@email.com"
              disabled={loading}
              required
              className="w-full rounded-xl border border-surface-border px-4 py-3 text-sm focus:ring-2 focus:ring-brand-500 focus:border-transparent outline-none transition-all"
            />
          </div>

          <div className="form-group">
            <label htmlFor="password" className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1.5 block">Password</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              disabled={loading}
              required
              className="w-full rounded-xl border border-surface-border px-4 py-3 text-sm focus:ring-2 focus:ring-brand-500 focus:border-transparent outline-none transition-all"
            />
          </div>

          <button
            type="submit"
            className="btn btn-primary w-full bg-brand-600 hover:bg-brand-700 text-white font-semibold py-3 rounded-xl transition-colors text-sm"
            disabled={loading}
          >
            {loading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>

        <p className="auth-link text-xs text-gray-400 text-center mt-6">
          Don't have an account? <Link className="text-brand-600 hover:underline" to="/register">Sign up here</Link>
        </p>
      </div>
    </div>
  );
};

export default Login;

