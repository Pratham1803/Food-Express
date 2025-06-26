package com.example.foodexpress.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodexpress.MenuBottomSheetFragment
import com.example.foodexpress.R
import com.example.foodexpress.adaptar.MenuAdapter
import com.example.foodexpress.databinding.FragmentHomeBinding
import com.example.foodexpress.model.FoodItems
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var MenuItems: MutableList<com.example.foodexpress.model.MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewAllMenu.setOnClickListener {
            val bottomSheetDialog = MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager, "Test")
        }

        // Retrieve and display popular menu items
        retrieveAndDisplayPopularItems()
        return binding.root
    }

    private fun retrieveAndDisplayPopularItems() {
        /// get reference to the database
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        MenuItems = mutableListOf()

        // retrieve menu items from the database
        MenuItems = mutableListOf()
        MenuItems.addAll(FoodItems.arrAllFoodItems)

        val index = MenuItems.indices.toList().shuffled()
        val numItemToShow = 6
        val subsetMenuItems = index.take(numItemToShow).map { MenuItems[it] }

        setPopularItemsAdapter(subsetMenuItems)
    }

    private fun setPopularItemsAdapter(subsetMenuItems: List<com.example.foodexpress.model.MenuItem>) {
        val adapter = MenuAdapter(subsetMenuItems, requireContext())
        binding.populerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.populerRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//image slider
        val imageList = ArrayList<SlideModel>()
        //add the image slider on home
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))
//binding the image
        val imgSlider = binding.imageSlider
        imgSlider.setImageList(imageList)
        imgSlider.setImageList(imageList, ScaleTypes.FIT)
        imgSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            //get position know which slider is run on home
            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "selected image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}