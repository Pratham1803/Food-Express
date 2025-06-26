package com.example.foodexpress.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodexpress.adaptar.BuyAgainAdapter
import com.example.foodexpress.model.OrderDetails
import com.example.foodexpress.RecentOrderItems

import com.example.foodexpress.databinding.FragmentHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItem: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        // Initialize Firebase auth
        auth = FirebaseAuth.getInstance()
        // Initialize Firebase database
        database = FirebaseDatabase.getInstance()

        // Retrieve and display the User's Order History
        retrieveBuyHistory()

//        binding.refreshButton.setOnClickListener {
//            retrieveBuyHistory()
//        }

        return binding.root
    }

//    override fun onItemClickListener(position: Int) {
//        val itemPushKey = listOfOrderItem[0].itemPushKey
//        val completeOrderReference =
//            listOfOrderItem[0].shopName?.let { database.reference.child("admin").child(it).child("CompletedOrder").child(itemPushKey!!) }
//        completeOrderReference!!.child("paymentReceived").setValue(true)
//    }

    // Function to see recent buy
    private fun seeItemsRecentBuy() {
        listOfOrderItem.firstOrNull()?.let { recentBuy ->
            val intent = Intent(requireContext(), RecentOrderItems::class.java)
            intent.putExtra("RecentBuyOrderItem",listOfOrderItem)
            startActivity(intent)
        }
    }

    // Function to retrieve items from the user's order history
    private fun retrieveBuyHistory() {
        userId = auth.currentUser?.uid ?: ""

        val buyItemReference: DatabaseReference =
            database.reference.child("user").child(userId).child("BuyHistory")
        val sortingQuery = buyItemReference.orderByChild("currentTime")

        sortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfOrderItem.clear()
                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }

                if (listOfOrderItem.isNotEmpty()) {
                    // Display the most recent order details
                    setDataInRecentBuyItem()
                    // Set up the RecyclerView with previous order details
                    setPreviousBuyItemsRecyclerView()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error if needed
            }
        })
    }

    // Function to display the most recent order details
    @SuppressLint("SetTextI18n")
    private fun setDataInRecentBuyItem() {
        val recentOrderItem = listOfOrderItem[listOfOrderItem.size - 1]
        recentOrderItem.let {
            with(binding) {
                buyAgainFoodName.text = (it.foodNames).toString()
                buyAgainFoodPrice.text = "₹ " + (it.foodPrices).toString()
                val image = it.foodImages
                Glide.with(requireContext()).load(image).into(buyAgainFoodImage)

                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty())
                    setPreviousBuyItemsRecyclerView()
            }
        }
    }

    // Function to set up the RecyclerView with previous order details
    private fun setPreviousBuyItemsRecyclerView() {
        val buyAgainFoodName = mutableListOf<String>()
        val buyAgainFoodPrice = mutableListOf<String>()
        val buyAgainFoodImage = mutableListOf<String>()

        for (i in 0 until listOfOrderItem.size) {
            listOfOrderItem[i].foodNames.let {
                buyAgainFoodName.add(it.toString())
                listOfOrderItem[i].foodPrices.let {
                    buyAgainFoodPrice.add(it.toString())
                    listOfOrderItem[i].foodImages.let {
                        buyAgainFoodImage.add(it.toString())
                    }
                }
            }
        }

        val rv = binding.BuyAgainRecyclerView
        rv.layoutManager = LinearLayoutManager(requireContext())
        buyAgainAdapter = BuyAgainAdapter(
           listOfOrderItem,
            requireContext()
        )
        rv.adapter = buyAgainAdapter
    }
}