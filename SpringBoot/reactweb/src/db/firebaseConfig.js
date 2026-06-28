import firebase from 'firebase/compat/app';
import 'firebase/compat/firestore';
import 'firebase/compat/auth';

const firebaseConfig = {
    apiKey: "REMOVED_FIREBASE_API_KEY",
    authDomain: "area-407612.firebaseapp.com",
    projectId: "area-407612",
    storageBucket: "area-407612.appspot.com",
    messagingSenderId: "685011442945",
    appId: "1:685011442945:web:3b8d3f147b40e5fbbd0f00",
    measurementId: "G-SMD9VS87VW"
};

if (!firebase.apps.length) {
    firebase.initializeApp(firebaseConfig);
}

export default firebase;
