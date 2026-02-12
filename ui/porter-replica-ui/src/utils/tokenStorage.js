/**
 * Secure token storage utility
 * Uses localStorage with optional fallback to memory for sensitive data
 */

const TOKEN_KEY = "auth_token";
const USER_KEY = "auth_user";
const REFRESH_TOKEN_KEY = "refresh_token";

// In-memory fallback for stricter security (comment out localStorage usage to only use memory)
let memoryStore = {
  [TOKEN_KEY]: null,
  [USER_KEY]: null,
  [REFRESH_TOKEN_KEY]: null,
};

export const tokenStorage = {
  /**
   * Set auth token
   * @param {string} token - JWT token
   * @param {boolean} useMemory - If true, uses memory only; if false, uses localStorage
   */
  setToken: (token, useMemory = false) => {
    if (useMemory) {
      memoryStore[TOKEN_KEY] = token;
    } else {
      localStorage.setItem(TOKEN_KEY, token);
      memoryStore[TOKEN_KEY] = token; // Also keep in memory
    }
  },

  /**
   * Get auth token
   */
  getToken: () => {
    return memoryStore[TOKEN_KEY] || localStorage.getItem(TOKEN_KEY);
  },

  /**
   * Set refresh token
   */
  setRefreshToken: (token, useMemory = false) => {
    if (useMemory) {
      memoryStore[REFRESH_TOKEN_KEY] = token;
    } else {
      localStorage.setItem(REFRESH_TOKEN_KEY, token);
      memoryStore[REFRESH_TOKEN_KEY] = token;
    }
  },

  /**
   * Get refresh token
   */
  getRefreshToken: () => {
    return memoryStore[REFRESH_TOKEN_KEY] || localStorage.getItem(REFRESH_TOKEN_KEY);
  },

  /**
   * Set user data
   */
  setUser: (user, useMemory = false) => {
    const userData = typeof user === "string" ? user : JSON.stringify(user);
    if (useMemory) {
      memoryStore[USER_KEY] = userData;
    } else {
      localStorage.setItem(USER_KEY, userData);
      memoryStore[USER_KEY] = userData;
    }
  },

  /**
   * Get user data
   */
  getUser: () => {
    const user = memoryStore[USER_KEY] || localStorage.getItem(USER_KEY);
    try {
      return user ? JSON.parse(user) : null;
    } catch {
      return null;
    }
  },

  /**
   * Clear all auth data
   */
  clear: () => {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
    memoryStore = {
      [TOKEN_KEY]: null,
      [USER_KEY]: null,
      [REFRESH_TOKEN_KEY]: null,
    };
  },

  /**
   * Check if token exists
   */
  hasToken: () => {
    return !!tokenStorage.getToken();
  },
};

export default tokenStorage;
