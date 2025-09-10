package com.example.adminwavesoffood.model

data class AllMenu(
    val id: String? = null,
    val foodName: String,
    val foodPrice: String,
    val foodDescription: String,
    val foodIngredient: String,
    val foodImage: String,
    var key: String? = null
)
