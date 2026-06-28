import React, { useState, useEffect } from "react";
import { Route, Routes, useNavigate, useLocation } from "react-router-dom";
import firebase from './db/firebaseConfig';

import Navbar from "./components/navbar";
import RecordList from "./components/recordList";
import Edit from "./components/edit";
import Create from "./components/create";
import Login from "./Screens/Auth/Login";
import Register from "./Screens/Auth/Register";
import './css/App.css';

const App = () => {
    const [user, setUser] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        const unsubscribe = firebase.auth().onAuthStateChanged(user => {
            if (user) {
                // User connected
                setUser(user);
                setIsLoggedIn(true);
            } else {
                // User disconnected
                setUser(null);
                setIsLoggedIn(false);
            }
        });

        return () => unsubscribe(); // Cleanup subscription
    }, []);

    useEffect(() => {
        const currentPath = location.pathname;

        if (!isLoggedIn) {
            if (currentPath === '/') {
                navigate('/login');
            } else if (currentPath !== '/login' && currentPath !== '/register') {
                navigate('/login');
            }
        } else {
            if (currentPath === '/' || currentPath === '/login' || currentPath === '/register') {
                navigate('/home');
            }
        }
    }, [isLoggedIn, navigate, location.pathname]);

    const ProtectedRoute = ({ children }) => {
        if (!isLoggedIn) {
            return <Login setIsLoggedIn={setIsLoggedIn} setUser={setUser} />;
        }
        return children;
    };

    return (
        <div>
            {isLoggedIn && <Navbar />}
            {isLoggedIn && <button id="logout-btn" onClick={() => firebase.auth().signOut()}>Logout</button>}
            <Routes>
                <Route exact path="/home" element={<ProtectedRoute><RecordList /></ProtectedRoute>} />
                <Route path="/edit/:id" element={<ProtectedRoute><Edit /></ProtectedRoute>} />
                <Route path="/create" element={<ProtectedRoute><Create /></ProtectedRoute>} />
                <Route path="/login" element={<Login setIsLoggedIn={setIsLoggedIn} setUser={setUser} />} />
                <Route path="/register" element={<Register setIsLoggedIn={setIsLoggedIn} setUser={setUser} />} />
            </Routes>
        </div>
    );
};

export default App;