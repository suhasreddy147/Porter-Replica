import AsyncStorage from "@react-native-async-storage/async-storage";
import axios from "axios";

const API = axios.create({
  baseURL: "http://192.168.68.108:8081/api",
  timeout: 10000 // replace with your IP
});

type AuthResponse = {
  accessToken: string;
};

// Attach token automatically
API.interceptors.request.use(async (config) => {
  const accessToken = await AsyncStorage.getItem("accessToken");

  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }
  //console.log("TOKEN: ", accessToken);
  return config;
});

export const loginUser = (data: { identifier: string; password: string }) =>
  API.post("/auth/login", data);

export const signupUser = (data: {
  name: string;
  phone: string;
  password: string;
  role: string;
}) => API.post("/auth/register", data);

export const logoutUser = () =>
  API.post("/auth/logout");