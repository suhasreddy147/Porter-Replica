import { fireEvent, render, waitFor } from "@testing-library/react-native";
import LoginScreen from "../app/index";
import * as authApi from "../src/api/authApi";
import { AuthContext } from "../src/context/AuthContext";

// Mock API
jest.spyOn(authApi, "loginUser").mockResolvedValue({
  data: { accessToken: "fake-token" },
}as any);

// Mock router
jest.mock("expo-router", () => ({
  router: {
    replace: jest.fn(),
  },
}));

describe("Login Screen", () => {
  const loginMock = jest.fn();
  const mockContext = {
  accessToken: null,
  login: loginMock,
  logout: jest.fn(),
  loading: false,
};

  it("logs in successfully with valid credentials", async () => {
    const { getByPlaceholderText, getByText } = render(
      <AuthContext.Provider value={mockContext}>
        <LoginScreen />
      </AuthContext.Provider>
    );

    fireEvent.changeText(
      getByPlaceholderText("(555) 000-0000"),
      "9999999999"
    );

    fireEvent.changeText(
      getByPlaceholderText("Enter your password"),
      "password123"
    );

    fireEvent.press(getByText("Login →"));

    await waitFor(() => {
      expect(authApi.loginUser).toHaveBeenCalled();
      expect(loginMock).toHaveBeenCalledWith("fake-token");
    });
  });
});