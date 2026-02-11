import { useState } from "react";
import { Link } from "react-router-dom";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Login Data:", { email, password });
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-white">
      <div className="w-full max-w-md px-8 py-12">
        <div className="bg-white mx-auto max-w-md p-8 rounded-lg shadow-sm">

          <h2 className="text-2xl font-semibold text-center mb-2">Welcome back</h2>
          <p className="text-sm text-center text-gray-500 mb-8">Sign in to your account.</p>

          <form onSubmit={handleSubmit} className="space-y-5">

            <div>
              <label className="block text-xs font-semibold text-gray-500 uppercase mb-2">Email</label>
              <input
                type="email"
                placeholder="name@example.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full px-4 py-3 border border-gray-200 rounded-md bg-white text-sm
                           focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-150"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold text-gray-500 uppercase mb-2">Password</label>
              <input
                type="password"
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full px-4 py-3 border border-gray-200 rounded-md bg-white text-sm
                           focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-150"
              />
            </div>

            <button
              type="submit"
              className="w-full bg-blue-600 text-white py-3 rounded-md hover:bg-blue-700 transition duration-150 font-medium"
            >
              Login
            </button>

          </form>

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