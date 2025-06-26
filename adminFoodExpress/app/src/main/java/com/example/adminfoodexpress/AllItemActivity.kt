package com.example.adminfoodexpress

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodexpress.adapter.MenuItemAdapter
//import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.adminfoodexpress.databinding.ActivityAllItemBinding
import com.example.adminfoodexpress.model.Admin
import com.example.adminfoodexpress.model.AllMenu
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import com.google.firebase.database.ValueEventListener

class AllItemActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var arrMenuItem: ArrayList<AllMenu> = ArrayList()
    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        FirebaseApp.initializeApp(binding.root.context)
        RetrieveMenuItem()

        binding.BackButton.setOnClickListener {
            finish()
        }
    }

    private fun RetrieveMenuItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("admin").child(Admin.userName).child("menu")

        //data from data base
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear exisiting data before populating
                arrMenuItem.clear()
                //loop for through each food item
                for (foodSnapshot in snapshot.children) {
                    val menuItem: Any? = AllMenu(
                        foodSnapshot.key,
                        foodSnapshot.child("foodName").value.toString(),
                        foodSnapshot.child("foodPrice").value.toString(),
                        foodSnapshot.child("foodDescription").value.toString(),
                        foodSnapshot.child("foodImage").value.toString(),
                        foodSnapshot.child("foodIngredients").value.toString()
                    )
                    menuItem?.let {
                        arrMenuItem.add(it as AllMenu)
                    }
                }

                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ErrorMsg", "Database Error: " + error.message)
            }
        })
    }

    private fun setAdapter() {
        val adapter =
            MenuItemAdapter(this@AllItemActivity, arrMenuItem, databaseReference) { position ->
                deleteMenuItems(position)
            }
        binding.MenuRView.layoutManager = LinearLayoutManager(this)
        binding.MenuRView.adapter = adapter
    }

    //delet button
    private fun deleteMenuItems(position: Int) {
        val menuItemToDelete = arrMenuItem[position]
        val menuItemKey = menuItemToDelete.key
        val foodMenuReference = database.reference.child("menu").child(menuItemKey!!)
        foodMenuReference.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                arrMenuItem.removeAt(position)
                binding.MenuRView.adapter?.notifyItemRemoved(position)
            } else {
                Toast.makeText(this, "Item not Deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}