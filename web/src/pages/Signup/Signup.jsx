import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import useAuth from "../../hooks/useAuth";
import validators from "../../utils/validators";

export default function Signup() {
  const navigate = useNavigate();
  const { signup, isAuthenticated, isLoading, error: authError, clearError } = useAuth();

  // Form state
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [formErrors, setFormErrors] = useState({});
  const [touched, setTouched] = useState({});
  const [passwordFeedback, setPasswordFeedback] = useState([]);

  // Redirect if already authenticated
  useEffect(() => {
    if (isAuthenticated) {
      navigate("/dashboard", { replace: true });
    }
  }, [isAuthenticated, navigate]);

  // Handle input change
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    if (name === "name") {
      setName(value);
    } else if (name === "email") {
      setEmail(value);
    } else if (name === "password") {
      setPassword(value);
      // Update password feedback
      setPasswordFeedback(validators.getPasswordFeedback(value));
    } else if (name === "confirmPassword") {
      setConfirmPassword(value);
    }
  };

  // Handle blur (mark field as touched)
  const handleBlur = (field) => {
    setTouched((prev) => ({ ...prev, [field]: true }));
  };

  // Validate individual fields
  const validateField = (fieldName, value) => {
    switch (fieldName) {
      case "name":
        if (!value.trim()) return "Name is required";
        if (value.trim().length < 2) return "Name must be at least 2 characters";
        return "";

      case "email":
        if (!value.trim()) return "Email is required";
        if (!validators.isValidEmail(value)) return "Please enter a valid email address";
        return "";

      case "password":
        if (!value.trim()) return "Password is required";
        if (!validators.isValidPassword(value)) {
          const feedback = validators.getPasswordFeedback(value);
          return `Password needs: ${feedback.join(", ")}`;
        }
        return "";

      case "confirmPassword":
        if (!value.trim()) return "Please confirm your password";
        if (!validators.passwordsMatch(password, value)) return "Passwords do not match";
        return "";

      default:
        return "";
    }
  };

  // Handle real-time validation on change
  useEffect(() => {
    if (touched.name) {
      const error = validateField("name", name);
      setFormErrors((prev) => ({ ...prev, name: error }));
    }
  }, [name, touched.name]);

  useEffect(() => {
    if (touched.email) {
      const error = validateField("email", email);
      setFormErrors((prev) => ({ ...prev, email: error }));
    }
  }, [email, touched.email]);

  useEffect(() => {
    if (touched.password) {
      const error = validateField("password", password);
      setFormErrors((prev) => ({ ...prev, password: error }));
    }
  }, [password, touched.password]);

  useEffect(() => {
    if (touched.confirmPassword) {
      const error = validateField("confirmPassword", confirmPassword);
      setFormErrors((prev) => ({ ...prev, confirmPassword: error }));
    }
  }, [confirmPassword, touched.confirmPassword, password]);

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    clearError();

    // Validate form
    const validation = validators.validateSignupForm(email, password, confirmPassword);
    if (!validation.isValid) {
      setFormErrors(validation.errors);
      setTouched({
        name: true,
        email: true,
        password: true,
        confirmPassword: true,
      });
      return;
    }

    if (!name.trim()) {
      setFormErrors((prev) => ({ ...prev, name: "Name is required" }));
      setTouched((prev) => ({ ...prev, name: true }));
      return;
    }

    // Call signup
    const result = await signup(email, password, name);
    if (result.success) {
      // Redirect will happen automatically via useEffect
      setName("");
      setEmail("");
      setPassword("");
      setConfirmPassword("");
      setFormErrors({});
      setPasswordFeedback([]);
    }
  };

  const hasErrors = Object.values(formErrors).some((e) => e);

  return (
    <div className="min-h-screen flex items-center justify-center bg-white">
      <div className="w-full max-w-md px-8 py-12">
        <div className="bg-white mx-auto max-w-md p-8 rounded-lg shadow-sm">
          <h2 className="text-2xl font-semibold text-center mb-2">Create Account</h2>
          <p className="text-sm text-center text-gray-500 mb-8">
            Join the platform to get started.
          </p>

          {/* Error message */}
          {authError && (
            <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-md">
              <p className="text-sm text-red-700">{authError}</p>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-5">
            {/* Name Field */}
            <div>
              <label className="block text-xs font-semibold text-gray-500 uppercase mb-2">
                Name
              </label>
              <input
                type="text"
                name="name"
                placeholder="Your full name"
                value={name}
                onChange={handleInputChange}
                onBlur={() => handleBlur("name")}
                className={`w-full px-4 py-3 border rounded-md bg-white text-sm
                           focus:outline-none focus:ring-2 transition duration-150
                           ${
                             touched.name && formErrors.name
                               ? "border-red-300 focus:ring-red-500"
                               : "border-gray-200 focus:ring-blue-500"
                           }`}
              />
              {touched.name && formErrors.name && (
                <p className="mt-1 text-xs text-red-600">{formErrors.name}</p>
              )}
            </div>

            {/* Email Field */}
            <div>
              <label className="block text-xs font-semibold text-gray-500 uppercase mb-2">
                Email
              </label>
              <input
                type="email"
                name="email"
                placeholder="name@example.com"
                value={email}
                onChange={handleInputChange}
                onBlur={() => handleBlur("email")}
                className={`w-full px-4 py-3 border rounded-md bg-white text-sm
                           focus:outline-none focus:ring-2 transition duration-150
                           ${
                             touched.email && formErrors.email
                               ? "border-red-300 focus:ring-red-500"
                               : "border-gray-200 focus:ring-blue-500"
                           }`}
              />
              {touched.email && formErrors.email && (
                <p className="mt-1 text-xs text-red-600">{formErrors.email}</p>
              )}
            </div>

            {/* Password Field */}
            <div>
              <label className="block text-xs font-semibold text-gray-500 uppercase mb-2">
                Password
              </label>
              <input
                type="password"
                name="password"
                placeholder="Create a password"
                value={password}
                onChange={handleInputChange}
                onBlur={() => handleBlur("password")}
                className={`w-full px-4 py-3 border rounded-md bg-white text-sm
                           focus:outline-none focus:ring-2 transition duration-150
                           ${
                             touched.password && formErrors.password
                               ? "border-red-300 focus:ring-red-500"
                               : "border-gray-200 focus:ring-blue-500"
                           }`}
              />
              {touched.password && formErrors.password && (
                <p className="mt-1 text-xs text-red-600">{formErrors.password}</p>
              )}
              {password && passwordFeedback.length > 0 && (
                <div className="mt-2 p-2 bg-blue-50 rounded text-xs text-blue-700">
                  <p className="font-semibold mb-1">Password needs:</p>
                  <ul className="space-y-1">
                    {passwordFeedback.map((item, idx) => (
                      <li key={idx}>• {item}</li>
                    ))}
                  </ul>
                </div>
              )}
            </div>

            {/* Confirm Password Field */}
            <div>
              <label className="block text-xs font-semibold text-gray-500 uppercase mb-2">
                Confirm Password
              </label>
              <input
                type="password"
                name="confirmPassword"
                placeholder="Confirm your password"
                value={confirmPassword}
                onChange={handleInputChange}
                onBlur={() => handleBlur("confirmPassword")}
                className={`w-full px-4 py-3 border rounded-md bg-white text-sm
                           focus:outline-none focus:ring-2 transition duration-150
                           ${
                             touched.confirmPassword && formErrors.confirmPassword
                               ? "border-red-300 focus:ring-red-500"
                               : "border-gray-200 focus:ring-blue-500"
                           }`}
              />
              {touched.confirmPassword && formErrors.confirmPassword && (
                <p className="mt-1 text-xs text-red-600">{formErrors.confirmPassword}</p>
              )}
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              disabled={isLoading || hasErrors}
              className={`w-full py-3 rounded-md font-medium text-white transition duration-150
                         ${
                           isLoading || hasErrors
                             ? "bg-gray-400 cursor-not-allowed"
                             : "bg-blue-600 hover:bg-blue-700"
                         }`}
            >
              {isLoading ? "Creating Account..." : "Create Account"}
            </button>
          </form>

          {/* Back to Login Link */}
          <div className="text-center mt-6">
            <Link
              to="/"
              className="inline-flex items-center text-sm text-gray-500 hover:text-gray-700"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="h-4 w-4 mr-2"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
              </svg>
              Back to login
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}