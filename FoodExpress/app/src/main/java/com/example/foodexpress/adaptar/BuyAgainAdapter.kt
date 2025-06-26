package com.example.foodexpress.adaptar

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodexpress.databinding.BuyAgainItemBinding
import com.example.foodexpress.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.snapshots

class BuyAgainAdapter(
    private val lsOrders: ArrayList<OrderDetails>,
    requireContext: Context
) : RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {

    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(lsOrders[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding =
            BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuyAgainViewHolder(binding)
    }

    override fun getItemCount(): Int = lsOrders.size

    class BuyAgainViewHolder(private val binding: BuyAgainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(orderDetails: OrderDetails) {
            binding.buyAgainFoodName.text = orderDetails.foodNames
            binding.buyAgainFoodPrice.text = "₹ ${orderDetails.foodPrices}"
            // Context can be obtained using binding.root.context
            Glide.with(binding.root.context)
                .load(orderDetails.foodImages)
                .into(binding.buyAgainFoodImage)

            if (orderDetails.orderAccepted && orderDetails.paymentReceived) {
                binding.buyAgainFoodButton.text = "Buy Again"
            } else {
                if(orderDetails.orderAccepted)
                    binding.buyAgainFoodButton.text = "Payment Pending"
                else {
                    binding.buyAgainFoodButton.text = "Order Pending"
                    return
                }

                binding.buyAgainFoodButton.setOnClickListener {
                    val database = FirebaseDatabase.getInstance()
                    val itemPushKey = orderDetails.itemPushKey
                    val completeOrderReference =
                        orderDetails.shopName.let {
                            database.reference.child("admin").child(orderDetails.shopName).child("CompletedOrder")
                                .child(itemPushKey)
                        }

                    if(completeOrderReference == null) {
                        Toast.makeText(binding.root.context, "Order not found", Toast.LENGTH_SHORT)
                            .show()
                    }else {
                        completeOrderReference.child("paymentReceived").setValue(true)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    binding.root.context,
                                    "Payment Done",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                binding.buyAgainFoodButton.text = "Buy Again"
                            }
                        val auth = FirebaseAuth.getInstance()
                        database.reference.child("user").child(auth.uid.toString())
                            .child("BuyHistory")
                            .child(itemPushKey).child("paymentReceived").setValue(true)
                    }
                }
            }
        }
    }
}