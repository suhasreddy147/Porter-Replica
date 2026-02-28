import { validatePassword, validatePhone } from "@/src/utils/validators";
import { CountryPicker } from "@betterdev/react-native-country-codes-picker";
import { Ionicons } from "@expo/vector-icons";
import AsyncStorage from '@react-native-async-storage/async-storage';
import { router } from "expo-router";
import { useContext, useState } from "react";
import { StyleSheet, Text, TextInput, TouchableOpacity, View } from "react-native";
import { loginUser } from "../src/api/authApi";
import { AuthContext } from "../src/context/AuthContext";

export default function LoginScreen() {
  const { login } = useContext(AuthContext);
  const [submitted, setSubmitted] = useState(false);

  const [identifier, setIdentifier] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [passwordVisible, setPasswordVisible] = useState(false);

  const [countryCode, setCountryCode] = useState("+91");
  const [showPicker, setShowPicker] = useState(false);
  const [callingCode, setCallingCode] = useState("1");
  const [visible, setVisible] = useState(false);

  const handleLogin = async () => {
    setSubmitted(true);
    if ( !validatePhone(identifier) || !validatePassword(password) ) {
          return;
        }
    // testStorage();

  //   try {
  //   const res = await loginUser({
  //     identifier: "test@test.com",
  //     password: "123456",
  //   });

  //   console.log("API Response:", res.data);
  // } catch (err) {
  //   console.log("API Error:", err);
  // }

    // if (!validatePhone(phone)) return setError("Invalid phone");
    // if (!validatePassword(password)) return setError("Password too short");

    try {
      setLoading(true);
      const res = await loginUser({ identifier, password });
      await login(res.data.accessToken);
      router.replace("/home");
    } catch (err: any) {
      setError(err.response?.data?.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  const testStorage = async () => {
    await AsyncStorage.setItem('testKey', 'hello');
    const value = await AsyncStorage.getItem('testKey');
    console.log("Stored value:", value);
  };

   return (
    <View style={styles.container}>
      {/* Top Icon */}
      <View style={styles.iconWrapper}>
        <View style={styles.iconCircle}>
          <Ionicons name="lock-closed" size={36} color="#2563EB" />
        </View>
      </View>

      {/* Phone */}
      <Text style={styles.label}>Phone Number</Text>
      <View style={styles.phoneInput}>
  
  {/* Country Picker */}
<TouchableOpacity
  style={styles.countryPicker}
  onPress={() => setShowPicker(true)}
>
  <Text style={styles.countryCode}>{countryCode}</Text>
  <Ionicons name="chevron-down" size={16} color="#6B7280" />
</TouchableOpacity>
<CountryPicker
  show={showPicker}
  pickerButtonOnPress={(item) => {
    setCountryCode(item.dial_code);
    setShowPicker(false);
  }}
  onBackdropPress={() => setShowPicker(false)}
  lang="en"
/>

  {/* Divider */}
  <View style={styles.divider} />

  {/* Phone Input */}
  <TextInput
    value={identifier}
    onChangeText={setIdentifier}
      placeholder="(555) 000-0000"
      placeholderTextColor="#9CA3AF"
      style={[
      styles.input,
      submitted && !validatePhone(identifier) && styles.errorBorder,
    ]}
      keyboardType="phone-pad"
    />
    
</View>

{submitted && !validatePhone(identifier) && (
    <Text style={styles.errorText}>Invalid phone</Text>
  )}

      {/* Password */}
            <Text style={styles.label}>Password</Text>
            <View style={[
          styles.passwordInput,
          submitted && !validatePassword(password) && styles.errorBorder,
        ]}>
              <TextInput
                value={password}
                onChangeText={setPassword}
                placeholder="Enter your password"
                placeholderTextColor="#9CA3AF"
                style={styles.input}
                secureTextEntry={!passwordVisible}
              />
              <TouchableOpacity onPress={() => setPasswordVisible(!passwordVisible)}>
    <Ionicons
      name={passwordVisible ? "eye" : "eye-off"}
      size={20}
      color="#9CA3AF"
    />
  </TouchableOpacity>
            </View>

            {submitted && !validatePassword(password) && (
        <Text style={styles.errorText}>Password must be at least 6 characters</Text>
      )}

      {/* Forgot */}
      <Text style={styles.forgot}>Forgot password?</Text>

      {/* Button */}
      <TouchableOpacity style={styles.button} onPress={handleLogin}>
        <Text style={styles.buttonText}>Login →</Text>
      </TouchableOpacity>

      {/* Footer */}
      <View style={styles.footer}>
        <Text style={styles.footerText}>Don't have an account? </Text>
        <Text
          style={styles.link}
          onPress={() => router.push("/signup")}
        >
          Sign up
        </Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#F5F7FB",
    padding: 24,
    justifyContent: "center",
  },

  iconWrapper: {
    alignItems: "center",
    marginBottom: 40,
  },

  iconCircle: {
    backgroundColor: "#E0E7FF",
    padding: 20,
    borderRadius: 50,
  },

  countryPicker: {
  flexDirection: "row",
  alignItems: "center",
  marginRight: 8,
},

countryCode: {
  marginLeft: 5,
  color: "#111827",
  fontWeight: "500",
},

  label: {
    fontSize: 16,
    fontWeight: "600",
    marginBottom: 8,
    color: "#111827",
  },

  phoneInput: {
    flexDirection: "row",
    alignItems: "center",
    backgroundColor: "#fff",
    borderRadius: 12,
    borderWidth: 1,
    borderColor: "#E5E7EB",
    paddingHorizontal: 12,
    height: 55,
  },

  divider: {
    width: 1,
    height: 25,
    backgroundColor: "#E5E7EB",
    marginRight: 10,
  },

  passwordInput: {
    flexDirection: "row",
    alignItems: "center",
    backgroundColor: "#fff",
    borderRadius: 12,
    borderWidth: 1,
    borderColor: "#E5E7EB",
    paddingHorizontal: 12,
    height: 55,
  },

  input: {
    flex: 1,
    color: "#111827",
  },

  forgot: {
    textAlign: "right",
    color: "#2563EB",
    marginTop: 10,
  },

  button: {
    backgroundColor: "#2563EB",
    height: 55,
    borderRadius: 12,
    justifyContent: "center",
    alignItems: "center",
    marginTop: 20,
    shadowColor: "#2563EB",
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 6,
    elevation: 5,
  },

  buttonText: {
    color: "#fff",
    fontSize: 16,
    fontWeight: "600",
  },

  footer: {
    flexDirection: "row",
    justifyContent: "center",
    marginTop: 40,
  },

  footerText: {
    color: "#6B7280",
  },

  link: {
    color: "#2563EB",
    fontWeight: "600",
  },
  codeContainer: {
  flexDirection: "row",
  alignItems: "center",
  marginLeft: 5,
},
 errorBorder: {
  borderColor: "red",
},

errorText: {
  color: "red",
  fontSize: 12,
  marginTop: 4,
  marginBottom: 8,
},
});