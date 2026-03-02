/**
 * Custom hook for authentication state management
 * Provides login, signup, logout, and auth status
 */

import { useState, useCallback, useEffect } from "react";
import authService from "../services/auth.service";
import tokenStorage from "../utils/tokenStorage";

export function useAuth() {
 //Suhas Reddy | commented out user state for now, can be implemented later if needed
  // const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  // Check if user is already authenticated on mount
  useEffect(() => {
    const verifyAuth = async () => {
      try {
        const token = tokenStorage.getToken();
        if (token) {
          //Suhas Reddy | commented out user data retrieval logic for now, can be implemented later if needed
          // const userData = tokenStorage.getUser();
          // setUser(userData);
          setIsAuthenticated(true);

          // Optional: Verify token with backend
          //Suhas Reddy | commented out token verification logic for now, can be implemented later if needed
          // const result = await authService.verifyToken();
          // if (!result.success) {
          //   // Token is invalid, clear auth
          //   tokenStorage.clear();
          //   setIsAuthenticated(false);
          //   setUser(null);
          // }
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
  const login = useCallback(async (identifier, password) => {
    setIsLoading(true);
    setError(null);

    try {
      const result = await authService.login(identifier, password);

      if (result.success) {
        //Suhas Reddy | commented out user data retrieval logic for now, can be implemented later if needed
        //setUser(result.data.user);
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
  const signup = useCallback(async (email, password, name, role="CUSTOMER") => {
    setIsLoading(true);
    setError(null);

    try {
      const result = await authService.signup(email, password, name, role);

      if (result.success) {
        //Suhas Reddy | commented out user data retrieval logic for now, can be implemented later if needed
        //setUser(result.data.user);
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
      //Suhas Reddy | commented out user data clearing logic for now, can be implemented later if needed
      //setUser(null);
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
   //Suhas Reddy | commented out user data retrieval logic for now, can be implemented later if needed
    // user,
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
