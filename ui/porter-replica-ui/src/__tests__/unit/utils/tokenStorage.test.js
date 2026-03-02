import { describe, it, expect, beforeEach, afterEach, vi } from "vitest";
import { tokenStorage } from "../../../utils/tokenStorage";

describe("tokenStorage - Token Management", () => {
  beforeEach(() => {
    localStorage.clear();
    tokenStorage.clear();
    vi.clearAllMocks();
  });

  afterEach(() => {
    localStorage.clear();
  });

  it("should store token in localStorage", () => {
    const testToken = "test-jwt-token-123";

    tokenStorage.setToken(testToken);

    expect(localStorage.getItem("auth_token")).toBe(testToken);
  });

  it("should retrieve token from localStorage", () => {
    const testToken = "test-jwt-token-456";
    tokenStorage.setToken(testToken);

    const retrieved = tokenStorage.getToken();

    expect(retrieved).toBe(testToken);
  });

  it("should store token in memory only when useMemory=true", () => {
    const testToken = "memory-token";

    tokenStorage.setToken(testToken, true);

    const fromStorage = localStorage.getItem("auth_token");
    expect(fromStorage).toBeNull();
  });

  it("should update token when setToken is called multiple times", () => {
    tokenStorage.setToken("token-1");
    tokenStorage.setToken("token-2");

    expect(tokenStorage.getToken()).toBe("token-2");
  });
});

describe("tokenStorage - RefreshToken Management", () => {
  beforeEach(() => {
    localStorage.clear();
    tokenStorage.clear();
  });

  afterEach(() => {
    localStorage.clear();
  });

  it("should store refresh token", () => {
    const refreshToken = "refresh-token-123";

    tokenStorage.setRefreshToken(refreshToken);

    expect(localStorage.getItem("refresh_token")).toBe(refreshToken);
  });

  it("should retrieve refresh token", () => {
    const refreshToken = "refresh-token-456";
    tokenStorage.setRefreshToken(refreshToken);

    const retrieved = tokenStorage.getRefreshToken();

    expect(retrieved).toBe(refreshToken);
  });

  it("should store refresh token in memory only when useMemory=true", () => {
    const refreshToken = "memory-refresh-token";

    tokenStorage.setRefreshToken(refreshToken, true);

    const fromStorage = localStorage.getItem("refresh_token");
    expect(fromStorage).toBeNull();
  });
});

describe("tokenStorage - User Management", () => {
  beforeEach(() => {
    localStorage.clear();
    tokenStorage.clear();
  });

  afterEach(() => {
    localStorage.clear();
  });

  it("should store user object as JSON", () => {
    const testUser = { id: "123", email: "user@example.com", name: "John" };

    tokenStorage.setUser(testUser);

    const stored = localStorage.getItem("auth_user");
    expect(JSON.parse(stored)).toEqual(testUser);
  });

  it("should retrieve and parse user object", () => {
    const testUser = { id: "456", email: "test@example.com", name: "Jane" };
    tokenStorage.setUser(testUser);

    const retrieved = tokenStorage.getUser();

    expect(retrieved).toEqual(testUser);
  });

  it("should handle user object when passed as string", () => {
    const userString = JSON.stringify({ id: "789", name: "Bob" });

    tokenStorage.setUser(userString);

    const retrieved = tokenStorage.getUser();
    expect(retrieved.id).toBe("789");
  });

  it("should store user in memory only when useMemory=true", () => {
    const testUser = { id: "999", name: "Memory User" };

    tokenStorage.setUser(testUser, true);

    const fromStorage = localStorage.getItem("auth_user");
    expect(fromStorage).toBeNull();
  });

  it("should return null for invalid JSON", () => {
    localStorage.setItem("auth_user", "invalid-json{");

    const result = tokenStorage.getUser();

    expect(result).toBeNull();
  });
});

describe("tokenStorage - hasToken", () => {
  beforeEach(() => {
    localStorage.clear();
    tokenStorage.clear();
  });

  afterEach(() => {
    localStorage.clear();
  });

  it("should return true when token exists", () => {
    tokenStorage.setToken("test-token");

    expect(tokenStorage.hasToken()).toBe(true);
  });

  it("should return false when token does not exist", () => {
    expect(tokenStorage.hasToken()).toBe(false);
  });

  it("should return false when token is null", () => {
    localStorage.setItem("auth_token", null);

    expect(tokenStorage.hasToken()).toBe(false);
  });

  it("should return false when token is empty string", () => {
    tokenStorage.setToken("");

    expect(tokenStorage.hasToken()).toBe(false);
  });
});

describe("tokenStorage - Clear", () => {
  beforeEach(() => {
    localStorage.clear();
    tokenStorage.clear();
  });

  afterEach(() => {
    localStorage.clear();
  });

  it("should clear all stored data", () => {
    tokenStorage.setToken("token-123");
    tokenStorage.setRefreshToken("refresh-456");
    tokenStorage.setUser({ id: "user-1" });

    tokenStorage.clear();

    expect(tokenStorage.getToken()).toBeNull();
    expect(tokenStorage.getRefreshToken()).toBeNull();
    expect(tokenStorage.getUser()).toBeNull();
  });

  it("should clear token from localStorage", () => {
    tokenStorage.setToken("test-token");

    tokenStorage.clear();

    expect(localStorage.getItem("auth_token")).toBeNull();
  });

  it("should clear refresh token from localStorage", () => {
    tokenStorage.setRefreshToken("test-refresh");

    tokenStorage.clear();

    expect(localStorage.getItem("refresh_token")).toBeNull();
  });

  it("should clear user from localStorage", () => {
    tokenStorage.setUser({ id: "123" });

    tokenStorage.clear();

    expect(localStorage.getItem("auth_user")).toBeNull();
  });
});