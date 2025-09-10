package com.example.wavesoffood.model.repositories

import com.example.wavesoffood.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = Firebase.database.reference

    fun signInWithEmail(email: String, password: String, callback: (Boolean, FirebaseUser?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, auth.currentUser)
            } else {
                callback(false, null)
            }
        }
    }

    fun signUpWithEmail(email: String, password: String, name: String?, callback: (Boolean, FirebaseUser?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                saveUserData(user, UserModel(name, email, password))
                callback(true, user)
            } else {
                callback(false, null)
            }
        }
    }

    fun signInWithGoogle(account: GoogleSignInAccount?, callback: (Boolean, FirebaseUser?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, auth.currentUser)
            } else {
                callback(false, null)
            }
        }
    }

    private fun saveUserData(user: FirebaseUser?, userModel: UserModel) {
        user?.uid?.let {
            database.child("Users").child(it).setValue(userModel)
        }
    }
}