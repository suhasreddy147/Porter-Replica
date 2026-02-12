/**
 * Authentication API service
 * Handles all backend authentication API calls
 */

import axios from "axios";
import tokenStorage from "../utils/tokenStorage";

// Configure API base URL (update with your backend URL)
const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:3000/api";

// Create axios instance with default config
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Add request interceptor to include auth token
axiosInstance.interceptors.request.use(
  (config) => {
    const token = tokenStorage.getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor to handle 401 errors
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expired or invalid - clear storage and redirect
      tokenStorage.clear();
      window.location.href = "/";
    }
    return Promise.reject(error);
  }
);

export const authService = {
  /**
   * Login user
   * @param {string} email - User email
   * @param {string} password - User password
   * @returns {Promise} Response with token and user data
   */
  login: async (email, password) => {
    try {
      const response = await axiosInstance.post("/auth/login", {
        email,
        password,
      });
      const { token, refreshToken, user } = response.data;

      // Store tokens and user data
      tokenStorage.setToken(token);
      if (refreshToken) tokenStorage.setRefreshToken(refreshToken);
      tokenStorage.setUser(user);

      return {
        success: true,
        data: response.data,
        message: "Login successful",
      };
    } catch (error) {
      return {
        success: false,
        message: error.response?.data?.message || "Login failed. Please try again.",
        error: error.response?.data || error.message,
      };
    }
  },

  /**
   * Signup user
   * @param {string} email - User email
   * @param {string} password - User password
   * @param {string} name - User name (optional)
   * @returns {Promise} Response with token and user data
   */
  signup: async (email, password, name = "") => {
    try {
      const response = await axiosInstance.post("/auth/signup", {
        email,
        password,
        name,
      });
      const { token, refreshToken, user } = response.data;

      // Store tokens and user data
      tokenStorage.setToken(token);
      if (refreshToken) tokenStorage.setRefreshToken(refreshToken);
      tokenStorage.setUser(user);

      return {
        success: true,
        data: response.data,
        message: "Signup successful",
      };
    } catch (error) {
      return {
        success: false,
        message: error.response?.data?.message || "Signup failed. Please try again.",
        error: error.response?.data || error.message,
      };
    }
  },

  /**
   * Logout user
   * Clears stored tokens and user data
   */
  logout: async () => {
    try {
      // Optional: Call backend logout endpoint
      await axiosInstance.post("/auth/logout");
    } catch (error) {
      console.error("Logout error:", error);
    } finally {
      // Clear local storage regardless of API response
      tokenStorage.clear();
    }
  },

  /**
   * Get current user
   */
  getCurrentUser: () => {
    return tokenStorage.getUser();
  },

  /**
   * Check if user is authenticated
   */
  isAuthenticated: () => {
    return tokenStorage.hasToken();
  },

  /**
   * Refresh authentication token
   * @returns {Promise} New token
   */
  refreshToken: async () => {
    try {
      const refreshToken = tokenStorage.getRefreshToken();
      if (!refreshToken) {
        throw new Error("No refresh token available");
      }

      const response = await axios.post(`${API_BASE_URL}/auth/refresh`, {
        refreshToken,
      });
      const { token } = response.data;

      tokenStorage.setToken(token);
      return {
        success: true,
        token,
      };
    } catch (error) {
      tokenStorage.clear();
      return {
        success: false,
        message: "Token refresh failed",
        error,
      };
    }
  },

  /**
   * Verify token validity
   */
  verifyToken: async () => {
    try {
      const response = await axiosInstance.get("/auth/verify");
      return {
        success: true,
        data: response.data,
      };
    } catch (error) {
      tokenStorage.clear();
      return {
        success: false,
        error,
      };
    }
  },
};

export default authService;
