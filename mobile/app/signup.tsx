import { CountryPicker } from "@betterdev/react-native-country-codes-picker";
import { Ionicons } from "@expo/vector-icons";
import { router } from "expo-router";
import { useContext, useState } from "react";
import { StyleSheet, Text, TextInput, TouchableOpacity, View } from "react-native";
import { signupUser } from "../src/api/authApi";
import { AuthContext } from "../src/context/AuthContext";
import { validatePassword, validatePhone } from "../src/utils/validators";

export default function SignupScreen() {
  const { login } = useContext(AuthContext);

  const [name, setName] = useState("");
  const [phone, setPhone] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [passwordVisible, setPasswordVisible] = useState(false);
  const [confirmPasswordVisible, setConfirmPasswordVisible] = useState(false);
  const [countryCode, setCountryCode] = useState("+91");
  const [callingCode, setCallingCode] = useState("1");
  const [visible, setVisible] = useState(false);
  const [acceptedTerms, setAcceptedTerms] = useState(false);
  const [submitted, setSubmitted] = useState(false);
  const [showPicker, setShowPicker] = useState(false);

  const handleSignup = async () => {
    setSubmitted(true);
    if (!name || !validatePhone(phone) || !validatePassword(password) || !acceptedTerms) {
      return;
    }

    try {
      setLoading(true);
      await signupUser({ name, phone, password, role:"CUSTOMER" });
      router.replace({
  pathname: "/",
  params: { signupSuccess: "true" },
});
    } catch (err: any) {
      setError(err.response?.data?.message || "Signup failed");
    } finally {
      setLoading(false);
    }
  };

   return (
    <View style={styles.container}>
      {/* Back */}
      <TouchableOpacity onPress={() => router.back()}>
        <Ionicons name="arrow-back" size={24} />
      </TouchableOpacity>

      {/* Header */}
      <Text style={styles.title}>Join us today</Text>
      <Text style={styles.subtitle}>
        Enter your details to create your account
      </Text>

      {/* Name */}
      <Text style={styles.label}>Full Name</Text>
      <TextInput
        value={name}
        onChangeText={setName}
        placeholder="Enter your full name"
        placeholderTextColor="#9CA3AF"
        style={[
          styles.inputBox,
          submitted && !name && styles.errorBorder,
      ]}
      />

      {submitted && !name && (
        <Text style={styles.errorText}>Name is required</Text>
      )}
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
  value={phone}
  onChangeText={setPhone}
    placeholder="(555) 000-0000"
    placeholderTextColor="#9CA3AF"
    style={[
    styles.inputBox,
    submitted && !validatePhone(phone) && styles.errorBorder,
  ]}
    keyboardType="phone-pad"
  />
</View>
{submitted && !validatePhone(phone) && (
  <Text style={styles.errorText}>Invalid phone</Text>
)}

      {/* Password */}
      <Text style={styles.label}>Password</Text>
      <View style={[
    styles.inputBoxRow,
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

      {/* Confirm */}
      <Text style={styles.label}>Confirm Password</Text>
      <View style={[
    styles.inputBoxRow,
    submitted && password!=confirmPassword && styles.errorBorder,
  ]}>
        <TextInput
        value={confirmPassword}
          onChangeText={setConfirmPassword}
          placeholder="Confirm your password"
          placeholderTextColor="#9CA3AF"
          style={styles.input}
          secureTextEntry={!confirmPasswordVisible}
        />
        <TouchableOpacity onPress={() => setConfirmPasswordVisible(!confirmPasswordVisible)}>
    <Ionicons
      name={confirmPasswordVisible ? "eye" : "eye-off"}
      size={20}
      color="#9CA3AF"
    />
  </TouchableOpacity>
      </View>
      {submitted && password!=confirmPassword && (
  <Text style={styles.errorText}>Passwords do not match</Text>
)}

      {/* Checkbox */}
      <View style={styles.termsRow}>
  <TouchableOpacity
  testID="terms-checkbox"
    style={[
      styles.checkbox,
      {
        backgroundColor: acceptedTerms ? "#2563EB" : "#fff",
        borderColor:
          submitted && !acceptedTerms ? "red" : "#9CA3AF",
      },
    ]}
    onPress={() => setAcceptedTerms(!acceptedTerms)}
  >
    {acceptedTerms && (
      <Ionicons name="checkmark" size={14} color="#fff" />
    )}
  </TouchableOpacity>

  <Text style={styles.termsText}>
    I agree to the{" "}
    <Text style={styles.link}>Terms of Service</Text> and{" "}
    <Text style={styles.link}>Privacy Policy</Text>
  </Text>
</View>

{submitted && !acceptedTerms && (
  <Text style={styles.errorText}>You must accept the terms</Text>
)}

      {/* Button */}
      <TouchableOpacity style={styles.button}  onPress={() => {
        handleSignup();
      }}>
        <Text style={styles.buttonText}>Create Account</Text>
      </TouchableOpacity>

      {/* Footer */}
      <View style={styles.footer}>
        <Text style={styles.footerText}>Already have an account? </Text>
        <Text style={styles.link} onPress={() => router.push("/")}>
          Back to login
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
    paddingTop: 50,
  },

  title: {
    fontSize: 28,
    fontWeight: "700",
    marginTop: 10,
  },

  subtitle: {
    color: "#6B7280",
    marginBottom: 20,
  },

  label: {
    marginTop: 15,
    marginBottom: 5,
    fontWeight: "600",
  },

  inputBox: {
    backgroundColor: "#fff",
    borderRadius: 12,
    borderWidth: 1,
    borderColor: "#E5E7EB",
    padding: 15,
  },

  inputBoxRow: {
    flexDirection: "row",
    alignItems: "center",
    backgroundColor: "#fff",
    borderRadius: 12,
    borderWidth: 1,
    borderColor: "#E5E7EB",
    paddingHorizontal: 12,
    height: 55,
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

  divider: {
    width: 1,
    height: 25,
    backgroundColor: "#E5E7EB",
    marginRight: 10,
  },

  input: {
    flex: 1,
  },

  termsRow: {
    flexDirection: "row",
    marginTop: 15,
    alignItems: "center",
  },

  checkbox: {
  width: 18,
  height: 18,
  borderWidth: 1,
  borderColor: "#9CA3AF",
  borderRadius: 4,
  marginRight: 10,
  justifyContent: "center",
  alignItems: "center",
  backgroundColor: "#2563EB", // default blue
},

  termsText: {
    color: "#6B7280",
    flex: 1,
  },

  link: {
    color: "#2563EB",
    fontWeight: "600",
  },

  button: {
    backgroundColor: "#2563EB",
    height: 55,
    borderRadius: 12,
    justifyContent: "center",
    alignItems: "center",
    marginTop: 20,
    elevation: 5,
  },

  buttonText: {
    color: "#fff",
    fontWeight: "600",
    fontSize: 16,
  },

  footer: {
    flexDirection: "row",
    justifyContent: "center",
    marginTop: 20,
  },

  footerText: {
    color: "#6B7280",
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