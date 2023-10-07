// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app"
import { getFirestore } from "firebase/firestore"
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyAmZMOW3Z4v9LeIBXL6FrtfwF29bvYJGvY",
    authDomain: "hol-up.firebaseapp.com",
    projectId: "hol-up",
    storageBucket: "hol-up.appspot.com",
    messagingSenderId: "572411675726",
    appId: "1:572411675726:web:c3cebfa04071fe9921af1f",
}

// Initialize Firebase
const app = initializeApp(firebaseConfig)

// Initialize Firestore
export const db = getFirestore(app)
