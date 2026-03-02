import { logoutUser } from "@/src/api/authApi";
import { router } from "expo-router";
import { useContext } from "react";
import { Button, Text, View } from "react-native";
import { AuthContext } from "../src/context/AuthContext";

export default function HomeScreen() {
  const { logout } = useContext(AuthContext);

  return (
    <View style={{ padding: 20 }}>
      <Text>Welcome 🎉</Text>
      <Button title="Logout" onPress={async () => {
        try{
          await logoutUser();
        } catch (error) {
          console.error("Logout failed:", error);
        }
        await logout();
        router.replace("/");
      }} />
    </View>
  );
}