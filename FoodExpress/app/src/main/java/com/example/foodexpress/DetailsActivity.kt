package com.example.foodexpress

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.foodexpress.databinding.ActivityDetailsBinding
import com.example.foodexpress.model.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private var foodName: String? = null
    private var foodImage: String? = null
    private var foodDescriptions: String? = null
    private var foodIngredients: String? = null
    private var foodPrice: String? = null
    private var shop:String? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()
        foodName = intent.getStringExtra("MenuItemName")
        foodDescriptions = intent.getStringExtra("MenuItemDescription")
        foodIngredients = intent.getStringExtra("MenuItemIngredients")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodImage = intent.getStringExtra("MenuItemImage")
        shop = intent.getStringExtra("ShopName")

        with(binding) {
            detailFoodName.text = foodName
            descriptiontextView.text = foodDescriptions
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailFoodimageView)
            ingredientTextView.text = foodIngredients
        }

        binding.imageButton.setOnClickListener {
            finish()
        }
        binding.addItemButton.setOnClickListener {
            addItemToCart()
        }
    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid ?: ""

        //Create a cartItems object
        val cartItem = CartItem(
            foodName.toString(),
            foodPrice.toString(),
            foodDescriptions.toString(),
            foodImage.toString(),
            foodQuantity = 1,
            foodIngredients.toString(),
            shop.toString()
        )

        // save data to cart item to firebase database
        database.child("user").child(userId).child("CartItems").push().setValue(cartItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Items added into cart successFully 😁", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(this, "Item Not added 😒", Toast.LENGTH_SHORT).show()
            }
    }
}