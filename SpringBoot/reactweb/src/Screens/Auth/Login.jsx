import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';

import firebase from '../../db/firebaseConfig';
import './css/Login.css';

export default function Login({ setIsLoggedIn, setUser }) {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const auth = firebase.auth();

    const handleEmailChange = (e) => {
        setEmail(e.target.value);
    }

    const handlePasswordChange = (e) => {
        setPassword(e.target.value)
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log(email + "\n" + password);

        signin(email, password)
            .then((userCredential) => {
                var user = userCredential.user;
                setUser(user);
                setIsLoggedIn(true);
                localStorage.setItem('user', JSON.stringify(user));
            })
            .catch((error) => {
                var errorCode = error.code;
                var errorMessage = error.message;
                alert(errorMessage);
            });
    }

    function signin(email, password) {
        return auth.signInWithEmailAndPassword(email, password);
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
                    body: JSON.stringify({ username: user.displayName, email: user.email })
                })
                    .then(response => response.json())
                    .then(data => console.log(data));
            }).catch((error) => {
                var errorMessage = error.message;
                alert(errorMessage);
            });
    }

    return (
        <div>
            <div className="login-container">
                <form onSubmit={handleSubmit} className="login-form">
                    <h1>Login</h1>
                    <div className="form-group">
                        <label htmlFor="exampleInputEmail1">Email address</label>
                        <input type="email" onChange={handleEmailChange} className="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email" />
                        <small id="emailHelp" className="form-text">We'll never share your email  or password with anyone else.</small>
                    </div>
                    <div className="form-group">
                        <label htmlFor="exampleInputPassword1">Password</label>
                        <input type="password" onChange={handlePasswordChange} className="form-control" id="exampleInputPassword1" placeholder="Password" />
                        <small className="form-text">Don't have an account? <small id="accountHelp" className="form-text"><Link to="/register">Register</Link></small></small>
                    </div>
                    <button type="submit" className="btn btn-primary">Submit</button>
                </form>
                <button onClick={signinWithGoogle} className="btn btn-google">Sign in with Google</button>
            </div>
        </div>
    )
}