// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app"
import { getFirestore } from "firebase/firestore"
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyB2AADxEkTU4MOaFMDfWXt02UlwRkUDkSY",
    authDomain: "hol-up-43e47.firebaseapp.com",
    projectId: "hol-up-43e47",
    storageBucket: "hol-up-43e47.appspot.com",
    messagingSenderId: "1029190940940",
    appId: "1:1029190940940:web:23f4be5335d3905d8bf129",
    measurementId: "G-B08JBKLPKR",
}

// Initialize Firebase
const app = initializeApp(firebaseConfig)

// Initialize Firestore
export const db = getFirestore(app)
