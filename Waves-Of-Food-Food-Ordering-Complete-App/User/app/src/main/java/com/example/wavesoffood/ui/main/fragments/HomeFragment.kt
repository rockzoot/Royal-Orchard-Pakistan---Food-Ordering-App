//package com.example.wavesoffood.ui.fragments
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.denzcoskun.imageslider.constants.ScaleTypes
//import com.denzcoskun.imageslider.interfaces.ItemClickListener
//import com.denzcoskun.imageslider.models.SlideModel
//import com.example.wavesoffood.ui.components.MenuBottomSheetFragment
//import com.example.wavesoffood.R
//import com.example.wavesoffood.ui.adapters.MenuBottomAdapter
//import com.example.wavesoffood.databinding.FragmentHomeBinding
//import com.example.wavesoffood.model.MenuItems
//import com.google.firebase.firestore.FirebaseFirestore
//
//class HomeFragment : Fragment() {
//    private lateinit var binding: FragmentHomeBinding
//    private lateinit var firestore: FirebaseFirestore
//    private lateinit var menuItems: MutableList<MenuItems>
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//        binding = FragmentHomeBinding.inflate(inflater, container, false)
//        binding.viewMenu.setOnClickListener {
//            val bottomSheetDialog = MenuBottomSheetFragment()
//            bottomSheetDialog.show(parentFragmentManager, "Test")
//        }
//
//        // Retrieve and display menu popular items
//        retrieveAndDisplayMenuPopularItems()
//
//        return binding.root
//    }
//
//    private fun retrieveAndDisplayMenuPopularItems() {
//        firestore = FirebaseFirestore.getInstance()
//        menuItems = mutableListOf()
//
//        // Access the "Menu" collection in Firestore
//        firestore.collection("Menu")
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    val menuItem = document.toObject(MenuItems::class.java)
//                    menuItems.add(menuItem)
//                }
//                // Display random popular items
//                randomPopularItems()
//            }
//            .addOnFailureListener { exception ->
//                Toast.makeText(requireContext(), "Failed to load menu items: ${exception.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    private fun randomPopularItems() {
//        // Shuffle and pick a subset of items
//        val index = menuItems.indices.toList().shuffled()
//        val numItemToShow = 10
//        val subsetMenuItems = index.take(numItemToShow).map { menuItems[it] }
//
//        setPopularItemsAdapter(subsetMenuItems)
//    }
//
//    private fun setPopularItemsAdapter(subsetMenuItems: List<MenuItems>) {
//        val adapter = MenuBottomAdapter(subsetMenuItems, requireContext())
//        binding.rv.layoutManager = LinearLayoutManager(requireContext())
//        binding.rv.adapter = adapter
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val imageList = ArrayList<SlideModel>()
//        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
//        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
//        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))
//
//        val imageSlider = binding.imageSlider
//
//        imageSlider.setImageList(imageList)
//        imageSlider.setImageList(imageList, ScaleTypes.FIT)
//
//        imageSlider.setItemClickListener(object : ItemClickListener {
//            override fun doubleClick(position: Int) {}
//
//            override fun onItemSelected(position: Int) {
//                val itemMessage = "Selected Image $position"
//                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_LONG).show()
//            }
//        })
//    }
//}

package com.example.wavesoffood.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.wavesoffood.ui.components.MenuBottomSheetFragment
import com.example.wavesoffood.R
import com.example.wavesoffood.ui.adapters.MenuBottomAdapter
import com.example.wavesoffood.databinding.FragmentHomeBinding
import com.example.wavesoffood.model.MenuItems
import com.example.wavesoffood.model.repositories.HomeRepository

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var menuItems: MutableList<MenuItems>
    private val repository = HomeRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewMenu.setOnClickListener {
            val bottomSheetDialog = MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager, "Test")
        }

        // Retrieve and display menu popular items
        retrieveAndDisplayMenuPopularItems()

        return binding.root
    }

    private fun retrieveAndDisplayMenuPopularItems() {
        menuItems = mutableListOf()

        repository.fetchMenuPopularItems(
            onSuccess = { fetchedMenuItems ->
                menuItems.addAll(fetchedMenuItems)
                randomPopularItems()
            },
            onFailure = { exception ->
                Toast.makeText(requireContext(), "Failed to load menu items: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun randomPopularItems() {
        val index = menuItems.indices.toList().shuffled()
        val numItemToShow = 10
        val subsetMenuItems = index.take(numItemToShow).map { menuItems[it] }
        setPopularItemsAdapter(subsetMenuItems)
    }

    private fun setPopularItemsAdapter(subsetMenuItems: List<MenuItems>) {
        val adapter = MenuBottomAdapter(subsetMenuItems, requireContext())
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider

        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {}

            override fun onItemSelected(position: Int) {
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_LONG).show()
            }
        })
    }
}

