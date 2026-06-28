import React, { useState } from 'react';
import { Link } from 'react-router-dom';

import firebase from '../../db/firebaseConfig';
import './css/Register.css';

export default function Register({ setIsLoggedIn, setUser }) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [username, setUsername] = useState('');

    const auth = firebase.auth();

    const handleUsernameChange = (e) => {
        setUsername(e.target.value);
    }

    const handleEmailChange = (e) => {
        setEmail(e.target.value);
    }

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log(email + "\n" + password);

        signup(email, password)
            .then((userCredential) => {
                var user = userCredential.user;
                setUser(user);
                setIsLoggedIn(true);
                fetch('http://localhost:8080/users/add', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ username: username, email: email })
                })
                    .then(response => response.json())
                    .then(data => console.log(data));
            })
            .catch((error) => {
                var errorMessage = error.message;
                alert(errorMessage);
            });
    }

    function signup(email, password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }

    function signinWithGoogle() {
        const provider = new firebase.auth.GoogleAuthProvider();
        auth.signInWithPopup(provider)
            .then((result) => {
                var user = result.user;
                setIsLoggedIn(true);
                setUser(user);
                localStorage.setItem('user', JSON.stringify(user));
                fetch('http://localhost:8080/users/add', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ username: user.username, email: user.email })
                })
                    .then(response => response.json())
                    .then(data => console.log(data));
            }).catch((error) => {
                var errorMessage = error.message;
                alert(errorMessage);
            });
    }

    return (
        <div className="register-container">
            <form onSubmit={handleSubmit} className="register-form">
                <h1>Register</h1>
                <div className="form-group">
                    <label htmlFor="usernameInput">Username</label>
                    <input type="username" onChange={handleUsernameChange} className="form-control" id="usernameInput" aria-describedby="usernameHelp" placeholder="Enter username" />
                </div>
                <div className="form-group">
                    <label htmlFor="exampleInputEmail1">Email address</label>
                    <input type="email" onChange={handleEmailChange} className="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email" />
                    <small id="emailHelp" className="form-text">We'll never share your email or password with anyone else.</small>
                </div>
                <div className="form-group">
                    <label htmlFor="exampleInputPassword1">Password</label>
                    <input type="password" onChange={handlePasswordChange} className="form-control" id="exampleInputPassword1" placeholder="Password" />
                    <small className="form-text">Already have an account? <small id="accountHelp" className="form-text"><Link to="/login">Login</Link></small></small>
                </div>
                <button type="submit" className="btn btn-primary" disabled={!email || !password || !username}>Submit</button>
            </form>
            <button onClick={signinWithGoogle} className="btn btn-google">Sign in with Google</button>
        </div>
    )
}
