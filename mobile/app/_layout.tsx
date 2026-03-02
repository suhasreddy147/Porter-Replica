import { Stack } from "expo-router";
import { useContext } from "react";
import { AuthContext, AuthProvider } from "../src/context/AuthContext";

function RootLayout() {
  const { token, loading } = useContext(AuthContext);

  if (loading) return null;

  return (
    <Stack screenOptions={{ headerShown: false }}>
      {!token ? (
        <>
          <Stack.Screen name="index" />
          <Stack.Screen name="signup" />
        </>
      ) : (
        <Stack.Screen name="home" />
      )}
    </Stack>
  );
}

export default function Layout() {
  return (
    <AuthProvider>
      <RootLayout />
    </AuthProvider>
  );
}