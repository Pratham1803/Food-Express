package com.example.foodexpress.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodexpress.adaptar.MenuAdapter
import com.example.foodexpress.databinding.FragmentSearchBinding
import com.example.foodexpress.model.FoodItems
import com.example.foodexpress.model.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class searchFragment : Fragment(){
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var lsFilter: Array<String>;

    private lateinit var MenuItems: ArrayList<MenuItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        // retrieve menu items from firebase
        MenuItems = ArrayList()
        adapter = MenuAdapter(MenuItems, requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter

        // setting Filter values
        lsFilter = FoodItems.mapFoodItem.keys.toTypedArray()
        lsFilter.set(0, "All Items")
        val filterAdapter =
            ArrayAdapter(binding.root.context, android.R.layout.simple_list_item_1, lsFilter)
        binding.FilterSpinner.adapter = filterAdapter
        binding.FilterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(lsFilter[position] == "All Items"){
                    MenuItems.clear()
                    MenuItems.addAll(FoodItems.arrAllFoodItems as ArrayList<MenuItem>)
                    adapter.notifyDataSetChanged()
                    return
                }
                MenuItems.clear()
                MenuItems.addAll(FoodItems.mapFoodItem[lsFilter[position]] as ArrayList<MenuItem>)
                adapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                MenuItems.clear()
                MenuItems.addAll(FoodItems.mapFoodItem[lsFilter[0]] as ArrayList<MenuItem>)
                adapter.notifyDataSetChanged()
            }
        }
        // setup for search view
        setupSearchView()
        return binding.root
    }

    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot: DataSnapshot in snapshot.children) {
                    val menuItem: MenuItem? = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { MenuItems.add(it as MenuItem) }
                }
                adapter.notifyDataSetChanged()
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setupSearchView() {
    }

    companion object {

    }
}




