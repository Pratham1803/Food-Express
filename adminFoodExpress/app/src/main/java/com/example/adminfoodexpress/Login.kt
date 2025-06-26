package com.example.adminfoodexpress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.adminfoodexpress.databinding.ActivityLoginBinding
import com.example.adminfoodexpress.model.Admin
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val dbRef: DatabaseReference by lazy {
        Firebase.database.reference
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.LoginButton.setOnClickListener {
            Admin.userName = binding.UserName.text.toString()
            Admin.password = binding.Password.text.toString()

            dbRef.child("admin").child(Admin.userName).get().addOnSuccessListener {
                if (it.exists()) {
                    val admin = it.getValue(Admin::class.java)
                    if (admin != null) {
                        if (admin.userName == Admin.userName && admin.password == Admin.password) {
                            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            saveLoginStatus(true)
                            finish()
                        } else {
                            Toast.makeText(this, "Login Failed, InValid User Name Or Password", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Login Failed, InValid User Name Or Password", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Login Failed, InValid User Name Or Password", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun saveLoginStatus(isLoggedIn: Boolean) {
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.putString("userNameString",Admin.userName)
        editor.apply()
    }
}