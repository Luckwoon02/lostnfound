import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link, useNavigate, Navigate } from 'react-router-dom';
import axios from 'axios';
import LostItemForm from './pages/LostItemForm';
import FoundItemForm from './pages/FoundItemForm';
import Matches from './pages/Matches';
import Login from './pages/Login';
import Register from './pages/Register';
import ProtectedRoute from './components/ProtectedRoute';

const Navbar = () => {
  const isAuthenticated = !!localStorage.getItem('token');
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    delete axios.defaults.headers.common['Authorization'];
    navigate('/login');
  };

  return (
    <nav>
      <h1>Lost & Found Portal</h1>
      <ul>
        {!isAuthenticated ? (
          <>
            <li><Link to="/login">Login</Link></li>
            <li><Link to="/register">Register</Link></li>
          </>
        ) : (
          <>
            <li><Link to="/lost-item">Report Lost Item</Link></li>
            <li><Link to="/found-item">Report Found Item</Link></li>
            <li><Link to="/matches">View Matches</Link></li>
            <li><button onClick={handleLogout}>Logout</button></li>
          </>
        )}
      </ul>
    </nav>
  );
};

function App() {
  // Set up axios to include the token in all requests if it exists
  const token = localStorage.getItem('token');
  if (token) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
  }

  return (
    <Router>
      <div className="container">
        <Navbar />
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          
          {/* Protected Routes */}
          <Route element={<ProtectedRoute />}>
            <Route path="/" element={<div>Welcome to Lost & Found Portal</div>} />
            <Route path="/lost-item" element={<LostItemForm />} />
            <Route path="/found-item" element={<FoundItemForm />} />
            <Route path="/matches" element={<Matches />} />
          </Route>
          
          {/* Redirect to home if no route matches */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
