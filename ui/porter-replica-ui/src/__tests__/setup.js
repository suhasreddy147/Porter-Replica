import { expect, afterEach, vi } from "vitest";
import { cleanup } from "@testing-library/react";
import { beforeAll } from "vitest";

// Cleanup after each test
afterEach(() => {
  cleanup();
  global.localStorage?.clear();
  vi.clearAllMocks();
});

// MUST run immediately (not inside beforeAll)
if (typeof window !== "undefined" && window.localStorage) {
  global.localStorage = window.localStorage;
}

// Mock window.matchMedia
Object.defineProperty(window, "matchMedia", {
  writable: true,
  value: vi.fn().mockImplementation((query) => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: vi.fn(),
    removeListener: vi.fn(),
    addEventListener: vi.fn(),
    removeEventListener: vi.fn(),
    dispatchEvent: vi.fn(),
  })),
});