package com.example.foodexpress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Toast
import com.example.foodexpress.databinding.ActivitySignBinding
import com.example.foodexpress.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class signActivity : AppCompatActivity() {

    //var for signup
    private lateinit var Email: String
    private lateinit var Password: String
    private lateinit var UserName: String

    //for firebase var
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    //start binding
    private val binding: ActivitySignBinding by lazy {
        ActivitySignBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //intialize firebase database
        database = Firebase.database.reference

        //intialize firebase auth
        auth = Firebase.auth

        //create acc button
        binding.CreateAccButton.setOnClickListener {
            UserName = binding.UserName.text.toString()
            Email = binding.EmailAddress.text.toString().trim()
            Password = binding.Password.text.toString().trim()

            //input text is check empty or not
            if (Email.isEmpty() || Password.isBlank() || UserName.isBlank()) {
                Toast.makeText(this, "Plaase Fill All Details ", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(Email, Password)
            }

        }

        //click listener
        binding.AlreadyHaveButton.setOnClickListener {
            val intent = Intent(this, loginactivity1::class.java)
            startActivity(intent)

            //when click on signup then o to login page
            binding.CreateAccButton.setOnClickListener {
                val intent = Intent(this, loginactivity1::class.java)
                startActivity(intent)
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account Create Successfully 😍", Toast.LENGTH_SHORT).show()

                //call the fun save user data
                SaveUserData()
                startActivity(Intent(this, loginactivity1::class.java))
                finish()
            } else {
                Toast.makeText(this, "Account creation Falied", Toast.LENGTH_SHORT).show()
                Log.d("ErrorMsg", "createAccount: Failure",task.exception)
            }
        }
    }

    //create fun for save user data in database
    private fun SaveUserData() {
        //retrieve data from input filed
        UserName = binding.UserName.text.toString()
        Password = binding.Password.text.toString().trim()
        Email = binding.EmailAddress.text.toString().trim()

        val user = UserModel(UserName, Email, Password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        //save data to fire base data base
        database.child("user").child(userId).setValue(user).addOnSuccessListener {
            Toast.makeText(this, "Data Added", Toast.LENGTH_SHORT).show()
        }
    }
}