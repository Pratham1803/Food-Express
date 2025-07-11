package com.example.foodexpress

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.foodexpress.CongratsBottomSheets
import com.example.foodexpress.databinding.ActivityPayOutBinding
import com.example.foodexpress.model.OrderDetails
import com.example.foodexpress.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayOutActivity : AppCompatActivity() {
    lateinit var binding: ActivityPayOutBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var totalAmount: String
    private lateinit var foodItemName: ArrayList<String>
    private lateinit var foodItemPrice: ArrayList<String>
    private lateinit var foodItemImage: ArrayList<String>
    private lateinit var foodItemDescription: ArrayList<String>
    private lateinit var foodItemIngredient: ArrayList<String>
    private lateinit var shopName: ArrayList<String>
    private lateinit var foodItemQuantities: ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase and User details
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()
        // set user data
        setUserData()

        // get user details form Firebase

        val intent = intent
        foodItemName = intent.getStringArrayListExtra("FoodItemName") as ArrayList<String>
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") as ArrayList<String>
        foodItemImage = intent.getStringArrayListExtra("FoodItemImage") as ArrayList<String>
        foodItemDescription =
            intent.getStringArrayListExtra("FoodItemDescription") as ArrayList<String>
        foodItemIngredient =
            intent.getStringArrayListExtra("FoodItemIngredient") as ArrayList<String>
        foodItemQuantities = intent.getIntegerArrayListExtra("FoodItemQuantities") as ArrayList<Int>
        shopName = intent.getStringArrayListExtra("ShopName") as ArrayList<String>

        totalAmount = "₹" + calculateTotalAmount().toString()
        // binding.totalAmount.isEnabled = false
        binding.totalAmount.setText(totalAmount)
        //for back button
        binding.backbtn.setOnClickListener {
            finish()
        }
        binding.PlaceMyOrder.setOnClickListener {
            // get data from textview
            name = binding.name.text.toString().trim()
            address = binding.address.text.toString().trim()
            phone = binding.phone.text.toString().trim()
            if (name.isBlank() && address.isBlank() && phone.isBlank()) {
                Toast.makeText(this, "Please Enter All The Details 😜", Toast.LENGTH_SHORT).show()
            } else {
                placeOrder()
            }
        }
    }

    private fun placeOrder() {
        for (i in 0 until shopName.size) {
            userId = auth.currentUser?.uid ?: ""
            val time = System.currentTimeMillis()
            val db_total_amount = foodItemPrice[i].toInt() * foodItemQuantities[i].toInt()
            totalAmount = totalAmount.replace("₹", "")
            val itemPushKey = databaseReference.child("OrderDetails").push().key
            val orderDetails = OrderDetails(
                userId,
                name,
                foodItemName[i],
                foodItemPrice[i],
                foodItemImage[i],
                foodItemQuantities[i],
                address,
                db_total_amount.toString(),
                phone,
                time,
                itemPushKey.toString(),
                false,
                false,
                shopName[i]
            )
            val orderReference = databaseReference.child("admin").child(shopName[i]).child("OrderDetails")
                .child(itemPushKey!!)
            orderReference.setValue(orderDetails).addOnSuccessListener {
                val bottomSheetDialog = CongratsBottomSheets()
                bottomSheetDialog.show(supportFragmentManager, "Test")
                removeItemFromCart()
                addOrderToHistory(orderDetails)
            }
                .addOnFailureListener {
                    Toast.makeText(this, "failed to order 😒", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("user").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!).setValue(orderDetails).addOnSuccessListener {

            }
    }

    private fun removeItemFromCart() {
        val cartItemsReference = databaseReference.child("user").child(userId).child("CartItems")
        cartItemsReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in 0 until foodItemPrice.size) {
            var price = foodItemPrice[i]
            val firestChar = price.first()
            val priceIntVale = if (firestChar == '₹') {
                price.drop(1).toInt()
            } else {
                price.toInt()
            }
            var quantity = foodItemQuantities[i]
            totalAmount += priceIntVale * quantity
        }
        return totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userReference = databaseReference.child("user").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                        val names = snapshot.child("name").getValue(String::class.java) ?: ""
                        val addresses = snapshot.child("address").getValue(String::class.java) ?: ""
                        val phones = snapshot.child("phone").getValue(String::class.java) ?: ""
                        binding.apply {
                            name.setText(names)
                            address.setText(addresses)
                            phone.setText(phones)
                        }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }
}