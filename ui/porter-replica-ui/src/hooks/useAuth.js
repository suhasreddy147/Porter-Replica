/**
 * Custom hook for authentication state management
 * Provides login, signup, logout, and auth status
 */

import { useState, useCallback, useEffect } from "react";
import authService from "../services/auth.service";
import tokenStorage from "../utils/tokenStorage";

export function useAuth() {
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  // Check if user is already authenticated on mount
  useEffect(() => {
    const verifyAuth = async () => {
      try {
        const token = tokenStorage.getToken();
        if (token) {
          const userData = tokenStorage.getUser();
          setUser(userData);
          setIsAuthenticated(true);

          // Optional: Verify token with backend
          const result = await authService.verifyToken();
          if (!result.success) {
            // Token is invalid, clear auth
            tokenStorage.clear();
            setIsAuthenticated(false);
            setUser(null);
          }
        }
      } catch (err) {
        console.error("Auth verification failed:", err);
      } finally {
        setIsLoading(false);
      }
    };

    verifyAuth();
  }, []);

  /**
   * Login user
   */
  const login = useCallback(async (email, password) => {
    setIsLoading(true);
    setError(null);

    try {
      const result = await authService.login(email, password);

      if (result.success) {
        setUser(result.data.user);
        setIsAuthenticated(true);
        return {
          success: true,
          message: result.message,
        };
      } else {
        setError(result.message);
        return {
          success: false,
          message: result.message,
        };
      }
    } catch (err) {
      const errorMessage = err.message || "Login failed";
      setError(errorMessage);
      return {
        success: false,
        message: errorMessage,
      };
    } finally {
      setIsLoading(false);
    }
  }, []);

  /**
   * Signup user
   */
  const signup = useCallback(async (email, password, name = "") => {
    setIsLoading(true);
    setError(null);

    try {
      const result = await authService.signup(email, password, name);

      if (result.success) {
        setUser(result.data.user);
        setIsAuthenticated(true);
        return {
          success: true,
          message: result.message,
        };
      } else {
        setError(result.message);
        return {
          success: false,
          message: result.message,
        };
      }
    } catch (err) {
      const errorMessage = err.message || "Signup failed";
      setError(errorMessage);
      return {
        success: false,
        message: errorMessage,
      };
    } finally {
      setIsLoading(false);
    }
  }, []);

  /**
   * Logout user
   */
  const logout = useCallback(async () => {
    setIsLoading(true);
    try {
      await authService.logout();
      setUser(null);
      setIsAuthenticated(false);
      setError(null);
    } catch (err) {
      console.error("Logout error:", err);
    } finally {
      setIsLoading(false);
    }
  }, []);

  /**
   * Clear error message
   */
  const clearError = useCallback(() => {
    setError(null);
  }, []);

  return {
    user,
    isAuthenticated,
    isLoading,
    error,
    login,
    signup,
    logout,
    clearError,
  };
}

export default useAuth;
