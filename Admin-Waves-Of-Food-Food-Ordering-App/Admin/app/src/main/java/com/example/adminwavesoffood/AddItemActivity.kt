package com.example.adminwavesoffood

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.adminwavesoffood.databinding.ActivityAddItemBinding
import com.example.adminwavesoffood.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddItemActivity : AppCompatActivity() {

    // Food item details
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredient: String

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var binding: ActivityAddItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Set up button listeners
        setupListeners()
    }

    private fun setupListeners() {
        binding.addItemBtn.setOnClickListener {
            // Get all text from EditText
            foodName = binding.enterFoodName.text.toString().trim()
            foodPrice = binding.enterFoodPrice.text.toString().trim()
            foodDescription = binding.description.text.toString().trim()
            foodIngredient = binding.ingredients.text.toString().trim()

            if (foodName.isBlank() || foodPrice.isBlank() || foodDescription.isBlank() || foodIngredient.isBlank()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            } else {
                uploadData()
            }
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun uploadData() {
        // Reference to Firestore "Menu" collection
        val menuRef = firestore.collection("Menu")

        binding.progressBar.visibility = View.VISIBLE // Show progress bar during upload

        // Create a new menu item without the image
        val menuItem = AllMenu(
            id = null,
            foodName = foodName,
            foodPrice = foodPrice,
            foodDescription = foodDescription,
            foodIngredient = foodIngredient,
            foodImage = "" // No image URL
        )

        // Add to Firestore
        menuRef.add(menuItem)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()
                navigateToMainActivity()
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to add item: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
