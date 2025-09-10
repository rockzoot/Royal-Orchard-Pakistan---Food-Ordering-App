package com.example.wavesoffood.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wavesoffood.R
import com.example.wavesoffood.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val navController=findNavController(R.id.fragmentContainerView)

        bottomNav.setupWithNavController(navController)

//        binding.imageButton.setOnClickListener {
//            val bottomSheetDialog = NotificationFragment()
//            bottomSheetDialog.show(supportFragmentManager,"Test")
//        }



    }
}