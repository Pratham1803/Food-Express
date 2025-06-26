package com.example.foodexpress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.foodexpress.model.FoodItems
import com.example.foodexpress.model.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class splash_screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        dbGetAllFoodItem()
    }

    private fun dbGetAllFoodItem() {
        FoodItems.arrAllFoodItems.clear()
        FoodItems.mapFoodItem.clear()

        val foodRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("admin")

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (adminPost in snapshot.children) {
                    FoodItems.mapFoodItem.put(
                        adminPost.key.toString(),
                        ArrayList<MenuItem>()
                    )
                    for (foodSnapshot in adminPost.child("menu").children) {
                        val menuItem: Any? = MenuItem(
                            foodSnapshot.child("foodName").value.toString(),
                            foodSnapshot.child("foodPrice").value.toString(),
                            foodSnapshot.child("foodDescription").value.toString(),
                            foodSnapshot.child("foodImage").value.toString(),
                            foodSnapshot.child("foodIngredient").value.toString(),
                            adminPost.key.toString()
                        )
                        FoodItems.mapFoodItem[adminPost.key.toString()]?.add(menuItem as MenuItem)
                        menuItem?.let {
                            FoodItems.arrAllFoodItems.add(it as MenuItem)
                        }
                    }
                }
                val auth: FirebaseAuth = FirebaseAuth.getInstance()
                if (auth.currentUser == null) {
                    var intent = Intent(this@splash_screen, StartActivity::class.java)
                    startActivity(intent)
                } else {
                    var intent = Intent(this@splash_screen, MainActivity::class.java)
                    startActivity(intent)
                }
                finish()
                Log.d("SuccessMsg", "onDataChange: Map = " + FoodItems.mapFoodItem)
                Log.d("SuccessMsg", "onDataChange: List = " + FoodItems.arrAllFoodItems)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ErrorMsg", error.toString())
            }
        })
    }
}