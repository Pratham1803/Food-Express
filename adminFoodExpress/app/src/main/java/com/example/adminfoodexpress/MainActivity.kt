package com.example.adminfoodexpress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.adminfoodexpress.databinding.ActivityMainBinding
import com.example.adminfoodexpress.model.Admin
import com.example.adminfoodexpress.model.OrderDetails
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val dbRef: DatabaseReference by lazy {
        Firebase.database.reference
    }
    private lateinit var completedOrderReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        FirebaseApp.initializeApp(binding.root.context)

        //add menu ClickListener
        Toast.makeText(this, "Welcome " + Admin.userName, Toast.LENGTH_SHORT).show()

        binding.addMenu.setOnClickListener {
            val intent = Intent(this@MainActivity, addItemActivity::class.java)
            startActivity(intent)
        }
        //all items ClickListener
        binding.allItemMenu.setOnClickListener {
            val intent = Intent(this@MainActivity, AllItemActivity::class.java)
            startActivity(intent)
        }
        //editprofile ClickListener
        binding.profile.setOnClickListener {
            val intent = Intent(this@MainActivity, AdminProfileActivity::class.java)
            startActivity(intent)
        }

        binding.pendingOrderTextView.setOnClickListener {
            val intent = Intent(this@MainActivity, PendingOrderActivity::class.java)
            startActivity(intent)
        }

        binding.outForDeliveryButton.setOnClickListener {
            val intent = Intent(this, OutForDeliveryActivity::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            binding.logoutButton.setOnClickListener {
                val builder = AlertDialog.Builder(binding.root.context)
                builder.setTitle("Food Express")
                builder.setIcon(R.drawable.adminlogo)
                builder.setMessage("Are you sure you want to log out?")

                builder.setPositiveButton("Yes") { _, _ ->
                    val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", false)
                    editor.putString("userNameString", "")
                    editor.apply()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }

                builder.setNegativeButton("No") { _, _ -> }
                builder.show()
            }
        }

        dbRef.child("admin").child(Admin.userName).get().addOnSuccessListener {
            if (it.exists()) {
                Admin.password = it.child("password").value.toString()
                Admin.address = it.child("address").value.toString()
                Admin.emailId = it.child("emailId").value.toString()
                Admin.phoneNum = it.child("phoneNum").value.toString()
            }
        }.addOnFailureListener {
            Log.d("ErrorMsg", "onCreate: " + it.message)
        }

        wholeTimeEarning()
        competedOrders()
        pendingOrders()
    }

    private fun wholeTimeEarning() {
        var listPfTotalPay = mutableListOf<Int>()
        completedOrderReference =
            FirebaseDatabase.getInstance().reference.child("admin").child(Admin.userName)
                .child("CompletedOrder")
        completedOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    var completeOrder = orderSnapshot.getValue(OrderDetails::class.java)

                    completeOrder?.totalPrice?.replace("$", "")?.toIntOrNull()
                        ?.let { i ->
                            listPfTotalPay.add(i)
                        }
                }
                binding.wholeTimeEarning.text = "₹ " + listPfTotalPay.sum().toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun competedOrders() {
        val completeOrderReference =
            dbRef.child("admin").child(Admin.userName).child("CompletedOrder")
        var completeOrderItemCount = 0
        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                completeOrderItemCount = snapshot.childrenCount.toInt()
                binding.completeOrders.text = completeOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun pendingOrders() {
        val pendingOrderReference: DatabaseReference =
            dbRef.child("admin").child(Admin.userName).child("OrderDetails")
        var pendingOrderItemCount = 0
        pendingOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                pendingOrderItemCount = snapshot.childrenCount.toInt()
                binding.pendingOrders.text = pendingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}