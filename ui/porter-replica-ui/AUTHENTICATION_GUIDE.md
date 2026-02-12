# Client-Side Authentication Implementation

This guide explains the complete client-side authentication flow implemented in this project.

## Architecture Overview

The authentication system consists of the following layers:

```
┌─────────────────────────────────────────┐
│          React Components                │
│      (Login, Signup, Protected Routes)   │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│         useAuth Hook                    │
│  (State management, session handling)   │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│      authService (API layer)            │
│  (Backend communication with axios)     │
└──────────────────┬──────────────────────┘
                   │
┌──────────────────▼──────────────────────┐
│    tokenStorage (Token management)      │
│  (Local storage & memory storage)       │
└─────────────────────────────────────────┘
```

## Project Structure

```
src/
├── hooks/
│   └── useAuth.js                    # Auth hook for state management
├── services/
│   └── auth.service.js               # Backend API calls
├── utils/
│   ├── tokenStorage.js               # Token storage utilities
│   └── validators.js                 # Form validation logic
├── components/
│   └── ProtectedRoute/
│       └── ProtectedRoute.jsx        # Route protection component
├── pages/
│   ├── Login/
│   │   └── Login.jsx                 # Login page with validation
│   └── Signup/
│       └── Signup.jsx                # Signup page (to be updated)
└── app/
    └── routes.jsx                    # App routing with protected routes
```

## File Descriptions

### 1. **tokenStorage.js** - Token & Session Management
Provides secure token storage with dual-layer approach (memory + localStorage):

```javascript
// Usage examples:
tokenStorage.setToken(token);              // Save token
tokenStorage.getToken();                   // Retrieve token
tokenStorage.clear();                      // Clear all auth data
tokenStorage.hasToken();                   // Check if authenticated
```

**Features:**
- Stores JWT tokens securely
- Optional memory-only storage for stricter security
- Stores user data alongside tokens
- Manages refresh tokens for token rotation

### 2. **validators.js** - Form Validation
Provides comprehensive validation utilities:

```javascript
// Email validation
validators.isValidEmail("user@example.com");

// Password strength validation
validators.isValidPassword("SecurePass123!");

// Form validation with error messages
const result = validators.validateLoginForm(email, password);
if (!result.isValid) {
  console.log(result.errors);  // Get error messages
}
```

**Validation Rules:**
- Email: Standard email format
- Password: Min 8 chars, 1 uppercase, 1 lowercase, 1 number, 1 special char
- Real-time field validation on blur/change

### 3. **authService.js** - Backend API Integration
Handles all authentication API calls with axios interceptors:

```javascript
// Login
const result = await authService.login(email, password);

// Signup
const result = await authService.signup(email, password, name);

// Logout
await authService.logout();

// Check authentication
authService.isAuthenticated();

// Get current user
authService.getCurrentUser();
```

**Features:**
- Request interceptor to add JWT token to headers
- Response interceptor to handle 401 (unauthorized) errors
- Automatic token storage on successful login
- Secure logout with server notification

### 4. **useAuth.js** - State Management Hook
Custom React hook for managing authentication state:

```javascript
const { 
  user,                    // Current user object
  isAuthenticated,         // Boolean auth status
  isLoading,              // Loading indicator
  error,                  // Error messages
  login,                  // Login function
  signup,                 // Signup function
  logout,                 // Logout function
  clearError              // Clear error state
} = useAuth();
```

**Features:**
- Persistent authentication state across page refreshes
- Automatic token validation on app load
- Error handling and user feedback
- Loading states for async operations

### 5. **ProtectedRoute.jsx** - Route Protection
Component that guards protected routes:

```javascript
<Route
  path="/dashboard"
  element={
    <ProtectedRoute>
      <Dashboard />
    </ProtectedRoute>
  }
/>
```

**Features:**
- Redirects unauthenticated users to login
- Shows loading indicator while verifying auth
- Prevents accessing protected pages without valid token

### 6. **Login.jsx** - Login Page Example
Complete login implementation with:
- Real-time input validation
- Error display
- Loading states
- Automatic redirect after successful login

## Setup Instructions

### 1. Backend API Configuration
Update the API URL in `authService.js`:

```javascript
const API_BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:3000/api";
```

Create a `.env` file in project root:
```
REACT_APP_API_URL=http://your-backend-url/api
```

### 2. Expected Backend Endpoints

Your backend should provide these endpoints:

#### POST `/auth/login`
**Request:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "jwt-token-here",
  "refreshToken": "refresh-token-here",
  "user": {
    "id": "user-id",
    "email": "user@example.com",
    "name": "User Name"
  }
}
```

#### POST `/auth/signup`
**Request:**
```json
{
  "email": "user@example.com",
  "password": "SecurePass123!",
  "name": "User Name"
}
```

**Response:**
```json
{
  "token": "jwt-token-here",
  "refreshToken": "refresh-token-here",
  "user": {
    "id": "user-id",
    "email": "user@example.com",
    "name": "User Name"
  }
}
```

#### POST `/auth/logout`
Called with Bearer token in header
```
Authorization: Bearer <token>
```

#### GET `/auth/verify`
Verifies current token validity
```
Authorization: Bearer <token>
```

#### POST `/auth/refresh`
**Request:**
```json
{
  "refreshToken": "refresh-token-here"
}
```

**Response:**
```json
{
  "token": "new-jwt-token"
}
```

## Integration Steps

### Step 1: Install Dependencies
```bash
npm install axios react-router-dom
```

### Step 2: Update Your Signup Component
Follow the same pattern as Login.jsx:

```javascript
import useAuth from "../../hooks/useAuth";
import validators from "../../utils/validators";

export default function Signup() {
  const { signup, isLoading, error } = useAuth();
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    const validation = validators.validateSignupForm(
      email, 
      password, 
      confirmPassword
    );
    
    if (validation.isValid) {
      await signup(email, password, name);
    }
  };
  
  // Rest of component implementation
}
```

### Step 3: Add More Protected Routes
Create page components and wrap with ProtectedRoute:

```javascript
<Route
  path="/profile"
  element={
    <ProtectedRoute>
      <ProfilePage />
    </ProtectedRoute>
  }
/>
```

### Step 4: Add Logout Button
Add logout functionality to your dashboard/navbar:

```javascript
import useAuth from "../hooks/useAuth";

export default function NavBar() {
  const { logout, user } = useAuth();
  
  const handleLogout = async () => {
    await logout();
    navigate("/");  // Redirect to login
  };
  
  return (
    <nav>
      <span>Welcome, {user?.name}</span>
      <button onClick={handleLogout}>Logout</button>
    </nav>
  );
}
```

## Security Best Practices

### 1. Token Storage
The implementation uses a dual-layer approach:
- **Memory Storage**: Default, cleared on page refresh (most secure)
- **LocalStorage**: Persists tokens (convenient but less secure)

Toggle between them:
```javascript
tokenStorage.setToken(token, useMemory = true);  // Memory only
tokenStorage.setToken(token, useMemory = false); // Memory + LocalStorage
```

### 2. Password Requirements
- Minimum 8 characters
- Must include uppercase and lowercase letters
- Must include at least one number
- Must include at least one special character

### 3. Token Refresh
Implement token rotation for enhanced security:
```javascript
// Call periodically or on 401 response
const result = await authService.refreshToken();
```

### 4. HTTPS Only
Always use HTTPS in production for token transmission.

### 5. CORS Configuration
Configure your backend to accept requests only from your frontend:
```javascript
// Backend CORS example (Node.js/Express)
app.use(cors({
  origin: process.env.FRONTEND_URL,
  credentials: true
}));
```

## Error Handling

The system provides detailed error feedback:

### Validation Errors
```javascript
const validation = validators.validateLoginForm(email, password);
if (!validation.isValid) {
  console.log(validation.errors);
  // {
  //   email: "Please enter a valid email",
  //   password: "Password must be at least 6 characters"
  // }
}
```

### API Errors
```javascript
const result = await authService.login(email, password);
if (!result.success) {
  console.log(result.message);  // Error message from backend
  console.log(result.error);    // Full error object
}
```

### Hook Errors
```javascript
const { error, clearError } = useAuth();
console.log(error);  // Current error
clearError();        // Clear error state
```

## Testing

### Test Authentication Flow
```javascript
// Test login endpoint
POST http://localhost:3000/api/auth/login
{
  "email": "test@example.com",
  "password": "TestPass123!"
}

// Test protected route with token
GET http://localhost:3000/api/auth/verify
Headers:
  Authorization: Bearer <token-received>
```

### Test in Browser Console
```javascript
// Check stored token
localStorage.getItem('auth_token');

// Check user data
JSON.parse(localStorage.getItem('auth_user'));

// Check authentication status
import tokenStorage from './utils/tokenStorage';
tokenStorage.hasToken();
```

## Common Issues & Solutions

### Issue: "Cannot find module" errors
**Solution:** Ensure all file paths are correct and files exist in the specified locations.

### Issue: Tokens not persisting between page refreshes
**Solution:** Check if `tokenStorage` is properly saving to localStorage or memory.

### Issue: Protected routes showing blank page
**Solution:** Verify API is responding correctly to `/auth/verify` endpoint.

### Issue: CORS errors when calling backend
**Solution:** Configure backend CORS to accept requests from your frontend URL.

## Next Steps

1. **Integrate with your backend** - Update API endpoints and configuration
2. **Enhance Signup page** - Follow Login pattern with password strength indicator
3. **Add password recovery** - Implement forgot password flow
4. **Add role-based access** - Extend ProtectedRoute for role-based authorization
5. **Implement 2FA** - Add two-factor authentication support
6. **Add session timeout** - Auto-logout on inactivity
7. **Setup refresh token rotation** - Implement automatic token refresh

## Additional Resources

- [JWT Best Practices](https://tools.ietf.org/html/rfc8949)
- [OWASP Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html)
- [React Router v7 Documentation](https://reactrouter.com/)
- [Axios Documentation](https://axios-http.com/)
