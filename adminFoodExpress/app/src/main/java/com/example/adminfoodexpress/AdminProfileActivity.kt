package com.example.adminfoodexpress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.adminfoodexpress.databinding.ActivityAdminProfileBinding
import com.example.adminfoodexpress.model.Admin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReference = database.reference.child("admin")

        binding.BackButton.setOnClickListener {
            finish()
        }
        binding.SaveButton.setOnClickListener {
            updateUserData()
        }
        binding.Name.isEnabled = false
        binding.canteen.isEnabled = false
        binding.Email.isEnabled = false
        binding.Phone.isEnabled = false

        binding.SaveButton.isEnabled = false

        var isEnable = false
        binding.EditButton.setOnClickListener {
            isEnable = !isEnable
            binding.canteen.isEnabled = isEnable
            binding.Email.isEnabled = isEnable
            binding.Phone.isEnabled = isEnable

            if (isEnable) {
                binding.Name.requestFocus()
            }
            binding.SaveButton.isEnabled = isEnable
        }

//        binding.LogOut.setOnClickListener {
//            val builder = AlertDialog.Builder(binding.root.context)
//            builder.setTitle("Food Express")
//            builder.setIcon(R.drawable.adminlogo)
//            builder.setMessage("Are you sure you want to log out?")
//
//            builder.setPositiveButton("Yes") { _, _ ->
//                val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
//                val editor = sharedPreferences.edit()
//                editor.putBoolean("isLoggedIn", false)
//                editor.putString("userNameString","")
//                editor.apply()
//                startActivity(Intent(this, Login::class.java))
//                finish()
//            }
//
//            builder.setNegativeButton("No") { _, _ -> }
//            builder.show()
//        }

        retrieveUserData()
    }

    private fun retrieveUserData() {
        binding.Name.setText(Admin.userName)
        binding.Phone.setText(Admin.phoneNum)
        binding.canteen.setText(Admin.address)
        binding.Email.setText(Admin.emailId)
    }


    private fun updateUserData() {
        var updateEmail = binding.Email.text.toString()
        var updatePhone = binding.Phone.text.toString()
        var updateAddress = binding.canteen.text.toString()

        val userReference = adminReference.child(Admin.userName)
        HashMap<String, Any>().apply {
            put("emailId", updateEmail)
            put("phoneNum", updatePhone)
            put("address", updateAddress)
            userReference.updateChildren(this).addOnSuccessListener {
                Toast.makeText(binding.root.context, "Profile Updated Success Full 😊", Toast.LENGTH_SHORT).show()
            }
        }
        // update the email and password for firebase Authentication
    }
}