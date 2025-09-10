package com.example.wavesoffood.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wavesoffood.databinding.ActivityStartBinding
import com.example.wavesoffood.ui.auth.LogInActivity
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    val binding by lazy{
        ActivityStartBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.nextBtn.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if (user != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}