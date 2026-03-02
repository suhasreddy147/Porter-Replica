import { fireEvent, render, waitFor } from "@testing-library/react-native";
import HomeScreen from "../app/home";
import * as authApi from "../src/api/authApi";
import { AuthContext } from "../src/context/AuthContext";

// Mock API
jest.spyOn(authApi, "logoutUser").mockResolvedValue({} as any);

jest.mock("expo-router", () => ({
  router: {
    replace: jest.fn(),
  },
}));

const logoutMock = jest.fn();

const mockContext = {
  accessToken: "token",
  login: jest.fn(),
  logout: logoutMock,
  loading: false,
};

describe("Home Screen", () => {

  it("logs out user on button click", async () => {
    const { getByText } = render(
      <AuthContext.Provider value={mockContext}>
        <HomeScreen />
      </AuthContext.Provider>
    );

    fireEvent.press(getByText("Logout"));

    await waitFor(() => {
      expect(authApi.logoutUser).toHaveBeenCalled();
      expect(logoutMock).toHaveBeenCalled();
    });
  });
});