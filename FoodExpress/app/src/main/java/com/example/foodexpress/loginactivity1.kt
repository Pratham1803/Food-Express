package com.example.foodexpress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Toast
import com.example.foodexpress.databinding.ActivityLoginactivity1Binding
import com.example.foodexpress.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database


class loginactivity1 : AppCompatActivity() {
    //dec te var for login in app
    private var userName: String? = null
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference


    private val binding: ActivityLoginactivity1Binding by lazy {
        ActivityLoginactivity1Binding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //initialization of firebase auth
        auth = Firebase.auth
        //initialization of firebase databse
        database = Firebase.database.reference


//        when click on login button then go to signup page(//login with email and pass)


        //login with email and password
        binding.LoginButton.setOnClickListener {

            //get data from text field

            email = binding.EmailAddress.text.toString().trim()
            password = binding.Password.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please Enter credentials ", Toast.LENGTH_SHORT).show()
            } else {
                CreateUser()
            }

        }

        //when click on dont have acc then go to signup page
        binding.DontHaveButton.setOnClickListener {
            val intent = Intent(this, signActivity::class.java)
            startActivity(intent)
        }

    }

    private fun CreateUser() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = auth.currentUser
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                UpdateUi(user)
            } else {
                Toast.makeText(this, "In valid Id, Password", Toast.LENGTH_SHORT).show()

            }
        }
    }

//    private fun saveUserData() {
//        email = binding.EmailAddress.text.toString().trim()
//        password = binding.Password.text.toString().trim()
//
//        val user = UserModel( userName,email, password)
//        val userId = FirebaseAuth.getInstance().currentUser!!.uid
//
//
//        //save data in to database
//        database.child("user").child(userId).setValue(user)
//    }

    private fun UpdateUi(user: FirebaseUser?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}