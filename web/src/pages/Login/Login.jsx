import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import useAuth from "../../hooks/useAuth";
import validators from "../../utils/validators";

export default function Login() {
  const navigate = useNavigate();
  const { login, isAuthenticated, isLoading, error: authError, clearError } = useAuth();

  // Form state
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [formErrors, setFormErrors] = useState({});
  const [touched, setTouched] = useState({});

  // Redirect if already authenticated
  useEffect(() => {
    if (isAuthenticated) {
      navigate("/dashboard", { replace: true });
    }
  }, [isAuthenticated, navigate]);

  // Handle input change
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    if (name === "email") {
      setEmail(value);
    } else if (name === "password") {
      setPassword(value);
    }
  };

  // Handle blur (mark field as touched)
  const handleBlur = (field) => {
    setTouched((prev) => ({ ...prev, [field]: true }));
  };

  // Validate individual fields
  const validateField = (fieldName, value) => {
    if (fieldName === "email") {
      if (!value.trim()) {
        return "Email is required";
      } else if (!validators.isValidEmail(value)) {
        return "Please enter a valid email address";
      }
    } else if (fieldName === "password") {
      if (!value.trim()) {
        return "Password is required";
      } else if (value.length < 6) {
        return "Password must be at least 6 characters";
      }
    }
    return "";
  };

  // Handle real-time validation on change
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

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    clearError();

    // Validate form
    const validation = validators.validateLoginForm(email, password);
    if (!validation.isValid) {
      setFormErrors(validation.errors);
      setTouched({ email: true, password: true });
      return;
    }

    // Call login
    const result = await login(email, password);
    if (result.success) {
      // Redirect will happen automatically via useEffect
      setEmail("");
      setPassword("");
      setFormErrors({});
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-white">
      <div className="w-full max-w-md px-8 py-12">
        <div className="bg-white mx-auto max-w-md p-8 rounded-lg shadow-sm">
          <h2 className="text-2xl font-semibold text-center mb-2">Welcome back</h2>
          <p className="text-sm text-center text-gray-500 mb-8">Sign in to your account.</p>

          {/* Error message */}
          {authError && (
            <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-md">
              <p className="text-sm text-red-700">{authError}</p>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-5">
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
                placeholder="Enter your password"
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
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              disabled={isLoading || Object.values(formErrors).some((e) => e)}
              className={`w-full py-3 rounded-md font-medium text-white transition duration-150
                         ${
                           isLoading || Object.values(formErrors).some((e) => e)
                             ? "bg-gray-400 cursor-not-allowed"
                             : "bg-blue-600 hover:bg-blue-700"
                         }`}
            >
              {isLoading ? "Signing in..." : "Login"}
            </button>
          </form>

          {/* Footer Links */}
          <div className="flex items-center justify-between mt-6 text-sm">
            <Link to="/signup" className="text-blue-600 hover:underline">
              Create account
            </Link>
            <Link to="/forgot" className="text-gray-500 hover:underline">
              Forgot password?
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}