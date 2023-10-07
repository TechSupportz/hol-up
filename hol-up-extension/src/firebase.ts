// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app"
import { getAnalytics } from "firebase/analytics"
import { getFirestore } from "firebase/firestore"
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyB2AADxEkTU4MOaFMDfWXt02UlwRkUDkSY",
  authDomain: "hol-up-43e47.firebaseapp.com",
  projectId: "hol-up-43e47",
  storageBucket: "hol-up-43e47.appspot.com",
  messagingSenderId: "1029190940940",
  appId: "1:1029190940940:web:8c965cb74ac570148bf129",
  measurementId: "G-R201HN76D5"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig)

// Initialize Analytics
const analytics = getAnalytics(app)

// Initialize Firestore
export const db = getFirestore(app)
