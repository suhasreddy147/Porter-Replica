import { useState } from "react";
import { Link } from "react-router-dom";

export default function Signup() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Signup Data:", { name, email, password });
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-white">
      <div className="w-full max-w-md px-8 py-12">
        <div className="bg-white mx-auto max-w-md p-8 rounded-lg shadow-sm">

          <h2 className="text-2xl font-semibold text-center mb-2">Create Account</h2>
          <p className="text-sm text-center text-gray-500 mb-8">Join the platform to get started.</p>

          <form onSubmit={handleSubmit} className="space-y-5">

            <div>
              <label className="block text-xs font-semibold text-gray-500 uppercase mb-2">Name</label>
              <input
                type="text"
                placeholder="Your full name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="w-full px-4 py-3 border border-gray-200 rounded-md bg-white text-sm
                           focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-150"
              />
            </div>

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
                placeholder="Create a password"
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
              Create Account
            </button>

          </form>

          <div className="text-center mt-6">
            <Link to="/" className="inline-flex items-center text-sm text-gray-500 hover:text-gray-700">
              <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
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