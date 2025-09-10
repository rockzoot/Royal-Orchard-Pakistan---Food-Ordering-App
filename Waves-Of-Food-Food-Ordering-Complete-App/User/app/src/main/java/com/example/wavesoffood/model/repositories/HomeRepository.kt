package com.example.wavesoffood.model.repositories

import com.example.wavesoffood.model.MenuItems
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class HomeRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun fetchMenuPopularItems(
        onSuccess: (List<MenuItems>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection("Menu")
            .get()
            .addOnSuccessListener { documents: QuerySnapshot ->
                val menuItems = documents.map { it.toObject(MenuItems::class.java) }
                onSuccess(menuItems)
            }
            .addOnFailureListener { exception: java.lang.Exception ->
                onFailure(exception)
            }
    }
}
