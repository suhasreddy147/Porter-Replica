import { fireEvent, render, waitFor } from "@testing-library/react-native";
import SignupScreen from "../app/signup";
import * as authApi from "../src/api/authApi";
import { AuthContext } from "../src/context/AuthContext";

// Mock API
jest.spyOn(authApi, "signupUser").mockResolvedValue({} as any);

// Mock router
jest.mock("expo-router", () => ({
  router: {
    replace: jest.fn(),
    push: jest.fn(),
    back: jest.fn(),
  },
}));

describe("Signup Screen", () => {
  const mockContext = {
    accessToken: null,
    login: jest.fn(),
    logout: jest.fn(),
    loading: false,
  };

  it("shows validation errors when fields are empty", async () => {
    const { getByPlaceholderText, getByText, getByTestId } = render(
      <AuthContext.Provider value={mockContext}>
        <SignupScreen />
      </AuthContext.Provider>
    );

    fireEvent.press(getByText("Create Account"));

    await waitFor(() => {
      expect(getByText("Name is required")).toBeTruthy();
    });
  });

  it("calls signup API on valid input", async () => {
    const { getByPlaceholderText, getByText, getByTestId } = render(
      <AuthContext.Provider value={mockContext}>
        <SignupScreen />
      </AuthContext.Provider>
    );

    fireEvent.changeText(
      getByPlaceholderText("Enter your full name"),
      "Test User"
    );

    fireEvent.changeText(
      getByPlaceholderText("(555) 000-0000"),
      "6362959439"
    );

    fireEvent.changeText(
      getByPlaceholderText("Enter your password"),
      "password123"
    );

    fireEvent.changeText(
      getByPlaceholderText("Confirm your password"),
      "password123"
    );

    // Accept terms (important!)
    fireEvent.press(getByTestId("terms-checkbox"));

    fireEvent.press(getByText("Create Account"));

    await waitFor(() => {
      expect(authApi.signupUser).toHaveBeenCalled();
    });
  });
});