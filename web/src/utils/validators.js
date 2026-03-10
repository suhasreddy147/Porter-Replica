/**
 * Validation utilities for authentication forms
 */

export const validators = {
  /**
   * Validate email format
   */
  isValidEmail: (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  },

  /**
   * Validate password strength
   * Requires: min 8 chars, 1 uppercase, 1 lowercase, 1 number, 1 special char
   */
  isValidPassword: (password) => {
    if (password.length < 8) return false;
    if (!/[A-Z]/.test(password)) return false; // Uppercase
    if (!/[a-z]/.test(password)) return false; // Lowercase
    if (!/\d/.test(password)) return false; // Number
    if (!/[!@#$%^&*(),.?":{}|<>]/.test(password)) return false; // Special char
    return true;
  },

  /**
   * Validate password match (for signup)
   */
  passwordsMatch: (password, confirmPassword) => {
    return password === confirmPassword && password.length > 0;
  },

  /**
   * Get password strength feedback
   */
  getPasswordFeedback: (password) => {
    const issues = [];
    if (password.length < 8) issues.push("At least 8 characters");
    if (!/[A-Z]/.test(password)) issues.push("1 uppercase letter");
    if (!/[a-z]/.test(password)) issues.push("1 lowercase letter");
    if (!/\d/.test(password)) issues.push("1 number");
    if (!/[!@#$%^&*(),.?":{}|<>]/.test(password)) issues.push("1 special character");
    return issues;
  },

  /**
   * Validate login form
   */
  validateLoginForm: (email, password) => {
    const errors = {};

    if (!email.trim()) {
      errors.email = "Email is required";
    } else if (!validators.isValidEmail(email)) {
      errors.email = "Please enter a valid email";
    }

    if (!password.trim()) {
      errors.password = "Password is required";
    } else if (password.length < 6) {
      errors.password = "Password must be at least 6 characters";
    }

    return {
      isValid: Object.keys(errors).length === 0,
      errors,
    };
  },

  /**
   * Validate signup form
   */
  validateSignupForm: (email, password, confirmPassword) => {
    const errors = {};

    if (!email.trim()) {
      errors.email = "Email is required";
    } else if (!validators.isValidEmail(email)) {
      errors.email = "Please enter a valid email";
    }

    if (!password.trim()) {
      errors.password = "Password is required";
    } else if (!validators.isValidPassword(password)) {
      const feedback = validators.getPasswordFeedback(password);
      errors.password = `Password needs: ${feedback.join(", ")}`;
    }

    if (!confirmPassword.trim()) {
      errors.confirmPassword = "Please confirm your password";
    } else if (!validators.passwordsMatch(password, confirmPassword)) {
      errors.confirmPassword = "Passwords do not match";
    }

    return {
      isValid: Object.keys(errors).length === 0,
      errors,
    };
  },
};

export default validators;
