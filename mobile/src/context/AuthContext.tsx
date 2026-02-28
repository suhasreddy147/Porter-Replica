import AsyncStorage from "@react-native-async-storage/async-storage";
import React, { createContext, useEffect, useState } from "react";

type AuthContextType = {
  accessToken: string | null;
  login: (accessToken: string) => Promise<void>;
  logout: () => Promise<void>;
  loading: boolean;
};

export const AuthContext = createContext<AuthContextType>({
  accessToken: null,
  login: async () => {},
  logout: async () => {},
  loading: true,
});

export const AuthProvider = ({ children }: any) => {
  const [accessToken, setAccessToken] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadToken();
  }, []);

  const loadToken = async () => {
    const storedToken = await AsyncStorage.getItem("accessToken");
    if (storedToken) setAccessToken(storedToken);
    setLoading(false);
  };

  const login = async (accessToken: string) => {
    await AsyncStorage.setItem("accessToken", accessToken);
    setAccessToken(accessToken);
  };

  const logout = async () => {
    await AsyncStorage.removeItem("accessToken");
    setAccessToken(null);
  };

  return (
    <AuthContext.Provider value={{ accessToken, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};