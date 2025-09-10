//package com.example.wavesoffood.ui.auth
//
//import android.app.Activity
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import com.example.wavesoffood.R
//import com.example.wavesoffood.databinding.ActivitySignUpBinding
//import com.example.wavesoffood.model.UserModel
//import com.example.wavesoffood.ui.activities.MainActivity
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase
//
//class SignUpActivity : AppCompatActivity() {
//
//    private lateinit var userName: String
//    private lateinit var userEmail: String
//    private lateinit var userPassword: String
//    private lateinit var auth: FirebaseAuth
//    private lateinit var database: DatabaseReference
//    private lateinit var googleSignInClient: GoogleSignInClient
//
//    val binding by lazy {
//        ActivitySignUpBinding.inflate(layoutInflater)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
//        // Initialize all
//        database = Firebase.database.reference
//        auth = Firebase.auth
//
//        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
//
//        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
//
//        binding.already.setOnClickListener {
//
//            startActivity(Intent(this, LogInActivity::class.java))
//            finish()
//        }
//
//        binding.signupBtn.setOnClickListener {
//
//            // Get test from editText filled
//            userName = binding.name.text.toString()
//            userEmail = binding.email.text.toString().trim()
//            userPassword = binding.password.text.toString().trim()
//
//            if (userName.isBlank() || userEmail.isBlank() || userPassword.isBlank()) {
//
//                Toast.makeText(this, "Please fill the Filled", Toast.LENGTH_SHORT).show()
//            } else {
//                auth.createUserWithEmailAndPassword(userEmail, userPassword)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            saveUserData()
//                            updateUi()
//                            Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT)
//                                .show()
//                        } else {
//                            Toast.makeText(
//                                this,
//                                "Account create failed : ${task.exception}",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//            }
//        }
//
//        binding.googleBtn2.setOnClickListener {
//            val signInIntent = googleSignInClient.signInIntent
//            launcher.launch(signInIntent)
//        }
//
//
//    }
//
//    // launcher for google sign-in
//    private val launcher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//
//                if (task.isSuccessful) {
//                    val account: GoogleSignInAccount? = task.result
//                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
//                    auth.signInWithCredential(credential).addOnCompleteListener { tasek ->
//                        if (tasek.isSuccessful) {
//                            updateUi()
//                            Toast.makeText(
//                                this,
//                                "Account sign-in successfully with Google",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else {
//                            Toast.makeText(
//                                this,
//                                "Account creation failed : ${tasek.exception}",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                } else {
//                    Toast.makeText(
//                        this,
//                        "Account creation failed : ${task.exception}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                }
//            } else {
//                Toast.makeText(
//                    this,
//                    "Account creation failed",
//                    Toast.LENGTH_SHORT
//                ).show()
//
//            }
//
//        }
//
//    private fun saveUserData() {
//        userName = binding.name.text.toString()
//        userPassword = binding.password.text.toString().trim()
//        userEmail = binding.email.text.toString().trim()
//
//        val user = UserModel(userName, userEmail, userPassword)
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//
//        // Save data to firebase database
//        database.child("Users").child(userId!!).setValue(user)
//
//    }
//
//    private fun updateUi() {
//        startActivity(Intent(this, MainActivity::class.java))
//        finish()
//    }
//}


package com.example.wavesoffood.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.wavesoffood.R
import com.example.wavesoffood.databinding.ActivitySignUpBinding
import com.example.wavesoffood.model.repositories.AuthRepository
import com.example.wavesoffood.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        authRepository = AuthRepository()

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.already.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }

        binding.signupBtn.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                authRepository.signUpWithEmail(email, password, name) { success, user ->
                    if (success) {
                        updateUi(user)
                        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.googleBtn2.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.result

                authRepository.signInWithGoogle(account) { success, user ->
                    if (success) {
                        updateUi(user)
                        Toast.makeText(this, "Signed up with Google successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Google sign-up failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}