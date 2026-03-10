vi.mock("axios", () => {
  const mockAxiosInstance = {
    post: vi.fn(),
    get: vi.fn(),
    interceptors: {
      request: { use: vi.fn() },
      response: { use: vi.fn() },
    },
  };

  const mockAxios = {
    create: vi.fn(() => mockAxiosInstance),
    __mockInstance: mockAxiosInstance, // 👈 attach here
  };

  return {
    default: mockAxios,
  };
});

vi.mock("../../../utils/tokenStorage", () => ({
  tokenStorage: {
    getToken: vi.fn(() => null),
    setToken: vi.fn(),
    setRefreshToken: vi.fn(),
    setUser: vi.fn(),
    clear: vi.fn(),
    hasToken: vi.fn(() => false),
    getUser: vi.fn(),
  },
}));

import axios from "axios";
import { authService } from "../../../services/auth.service";
import { tokenStorage } from "../../../utils/tokenStorage";
import { describe, it, expect, beforeEach, vi } from "vitest";

beforeEach(() => {
  vi.clearAllMocks();
  vi.spyOn(console, "error").mockImplementation(() => {});
});

const getMockAxiosInstance = () => {
  const mockAxios = axios.create;
  const createCalls = mockAxios.mock.results;
  if (createCalls.length > 0) {
    return createCalls[createCalls.length - 1].value;
  }
  return null;
};

describe("authService - Login", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    // Recreate the instance after clearing mocks
    
  });

  it("should successfully login with valid credentials", async () => {
    const mockInstance = axios.__mockInstance;
    
    mockInstance.post.mockResolvedValueOnce({
      data: {
        accessToken: "test-token-123",
        tokenType: "Bearer",
      },
    });

    const result = await authService.login("user@example.com", "password123");

    expect(result.success).toBe(true);
    expect(result.message).toBe("Login successful");
  });

  it("should store accessToken on successful login", async () => {
    const mockInstance = axios.__mockInstance;
    
    mockInstance.post.mockResolvedValueOnce({
      data: {
        accessToken: "test-token-456",
        tokenType: "Bearer",
      },
    });

    await authService.login("user@example.com", "password123");

    expect(tokenStorage.setToken).toHaveBeenCalledWith("test-token-456");
  });

  it("should call /auth/login endpoint with identifier and password", async () => {
    const mockInstance = axios.__mockInstance;
    
    mockInstance.post.mockResolvedValueOnce({
      data: { accessToken: "token", tokenType: "Bearer" },
    });

    await authService.login("user@example.com", "password123");

    expect(mockInstance.post).toHaveBeenCalledWith("/auth/login", {
      identifier: "user@example.com",
      password: "password123",
    });
  });

  it("should return error on invalid credentials", async () => {
    const mockInstance = axios.__mockInstance;
    
    mockInstance.post.mockRejectedValueOnce({
      response: {
        status: 401,
        data: { message: "Invalid credentials" },
      },
    });

    const result = await authService.login("user@example.com", "wrongpassword");

    expect(result.success).toBe(false);
    expect(result.message).toBe("Invalid credentials");
  });

  it("should include error object in response on failure", async () => {
    const mockInstance = axios.__mockInstance;
    
    mockInstance.post.mockRejectedValueOnce({
      response: {
        status: 400,
        data: { message: "Login failed" },
      },
    });

    const result = await authService.login("user@example.com", "wrongpassword");

    expect(result.message).toBeDefined();
    expect(result.error).toBeDefined();
  });

  it("should handle network error", async () => {
    const mockInstance = axios.__mockInstance;
    
    mockInstance.post.mockRejectedValueOnce(new Error("Network Error"));

    const result = await authService.login("user@example.com", "password123");

    expect(result.success).toBe(false);
  });
});

describe("authService - Signup", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("should successfully signup new user", async () => {
    const mockInstance = axios.__mockInstance;
    
    mockInstance.post.mockResolvedValueOnce({
      data: { message: "User registered successfully" },
    });

    const result = await authService.signup(
      "newuser@example.com",
      "password123",
      "John Doe",
      "CUSTOMER"
    );

    expect(result.success).toBe(true);
    expect(result.message).toBe("Signup successful");
  });

  it("should call POST /auth/register endpoint", async () => {
    const mockInstance = axios.__mockInstance;
    
    mockInstance.post.mockResolvedValueOnce({ data: {} });

    await authService.signup(
      "test@example.com",
      "password123",
      "Jane Smith",
      "CUSTOMER"
    );

    expect(mockInstance.post).toHaveBeenCalledWith("/auth/register", {
      email: "test@example.com",
      password: "password123",
      name: "Jane Smith",
      role: "CUSTOMER",
    });
  });

  it("should handle signup errors gracefully", async () => {
    const mockInstance = axios.__mockInstance;
    
    mockInstance.post.mockRejectedValueOnce({
      response: {
        data: { message: "Email already registered" },
      },
    });

    const result = await authService.signup(
      "existing@example.com",
      "password123",
      "John Doe",
      "CUSTOMER"
    );

    expect(result.success).toBe(false);
    expect(result.message).toBe("Email already registered");
  });

  it("should not store user data after signup", async () => {
    const mockInstance = axios.__mockInstance;
    
    mockInstance.post.mockResolvedValueOnce({ data: {} });

    await authService.signup("user@example.com", "password123", "John", "CUSTOMER");

    expect(tokenStorage.setUser).not.toHaveBeenCalled();
  });
});

describe("authService - Logout", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    
  });

  it("should clear tokens on logout", async () => {
    const mockInstance = axios.__mockInstance;
    
    mockInstance.post.mockResolvedValueOnce({});

    await authService.logout();

    expect(tokenStorage.clear).toHaveBeenCalled();
  });

  it("should return success message", async () => {
    const mockInstance = axios.__mockInstance;
    
    mockInstance.post.mockResolvedValueOnce({});

    const result = await authService.logout();

    expect(result.success).toBe(true);
    expect(result.message).toBe("Logout successful");
  });

  it("should clear tokens even if API fails", async () => {
    const mockInstance = axios.__mockInstance;
    
    mockInstance.post.mockRejectedValueOnce(new Error("API Error"));

    const result = await authService.logout();

    expect(tokenStorage.clear).toHaveBeenCalled();
    expect(result.success).toBe(true);
  });

  it("should call POST /auth/logout endpoint", async () => {
    const mockInstance = axios.__mockInstance;
    
    mockInstance.post.mockResolvedValueOnce({});

    await authService.logout();

    expect(mockInstance.post).toHaveBeenCalledWith("/auth/logout");
  });
});

describe("authService - IsAuthenticated", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("should return true when token exists", () => {
    tokenStorage.hasToken.mockReturnValue(true);

    const result = authService.isAuthenticated();

    expect(result).toBe(true);
  });

  it("should return false when token does not exist", () => {
    tokenStorage.hasToken.mockReturnValue(false);

    const result = authService.isAuthenticated();

    expect(result).toBe(false);
  });

  it("should call tokenStorage.hasToken()", () => {
    tokenStorage.hasToken.mockReturnValue(true);

    authService.isAuthenticated();

    expect(tokenStorage.hasToken).toHaveBeenCalled();
  });
});