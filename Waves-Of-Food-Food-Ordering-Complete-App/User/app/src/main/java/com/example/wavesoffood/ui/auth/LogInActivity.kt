//package com.example.wavesoffood.ui.auth
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import com.example.wavesoffood.R
//import com.example.wavesoffood.databinding.ActivityLogInBinding
//import com.example.wavesoffood.model.UserModel
//import com.example.wavesoffood.ui.activities.MainActivity
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.auth.GoogleAuthProvider
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase
//
//class LogInActivity : AppCompatActivity() {
//    val binding by lazy {
//        ActivityLogInBinding.inflate(layoutInflater)
//    }
//
//    private var name: String? = null
//    private lateinit var email: String
//    private lateinit var password: String
//    private lateinit var auth: FirebaseAuth
//    private lateinit var database: DatabaseReference
//    private lateinit var googleSignInClient: GoogleSignInClient
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
//
//        // Initialize all
//        database = Firebase.database.reference
//        auth = FirebaseAuth.getInstance()
//
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//
//        binding.dont.setOnClickListener {
//            startActivity(Intent(this, SignUpActivity::class.java))
//            finish()
//        }
//
//        binding.loginBtn.setOnClickListener {
//
//            // Get text from editText filled
//            email = binding.email.text.toString().trim()
//            password = binding.password.text.toString().trim()
//
//            if (email.isBlank() || password.isBlank()) {
//                Toast.makeText(this, "Please fill the all details", Toast.LENGTH_SHORT).show()
//            } else {
//
//                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val user = auth.currentUser
//                        updateUi(user)
//                        Toast.makeText(this, "Account sign-in successfully", Toast.LENGTH_SHORT)
//                            .show()
//                    } else {
//                        auth.createUserWithEmailAndPassword(email, password)
//                            .addOnCompleteListener { tasek ->
//                                if (tasek.isSuccessful) {
//                                    saveUserData()
//                                    val user = auth.currentUser
//                                    updateUi(user)
//                                    Toast.makeText(
//                                        this,
//                                        "Account create successfully",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                } else {
//                                    Toast.makeText(
//                                        this,
//                                        "Account Failed : ${tasek.exception}",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//                            }
//
//                    }
//                }
//
//            }
//        }
//        binding.googleButton.setOnClickListener {
//            val signInIntent = googleSignInClient.signInIntent
//            launcher.launch(signInIntent)
//        }
//
//    }
//
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
//                            val user = auth.currentUser
//                            updateUi(user)
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
//
//        // Get text from editText filled
//        email = binding.email.text.toString().trim()
//        password = binding.password.text.toString().trim()
//
//        val user = UserModel(name, email, password)
//        val userUid = FirebaseAuth.getInstance().currentUser!!.uid
//
//        //Save data in to database
//        database.child("Users").child(userUid).setValue(user)
//    }
//
//    private fun updateUi(user: FirebaseUser?) {
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
import com.example.wavesoffood.databinding.ActivityLogInBinding
import com.example.wavesoffood.model.repositories.AuthRepository
import com.example.wavesoffood.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser

class LogInActivity : AppCompatActivity() {

    val binding by lazy { ActivityLogInBinding.inflate(layoutInflater) }
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        authRepository = AuthRepository()

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.dont.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            } else {
                authRepository.signInWithEmail(email, password) { success, user ->
                    if (success) {
                        updateUi(user)
                        Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.googleButton.setOnClickListener {
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
                        Toast.makeText(this, "Logged in with Google successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}