package com.example.foodexpress.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
//import com.example.foodexpress.R
//import com.example.foodexpress.StartActivity
import com.example.foodexpress.databinding.FragmentProfileBinding
import com.example.foodexpress.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var isEditable = false
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        reset()
        setUserData()
        binding.btnSave.setOnClickListener {
            val name = binding.edName.text.toString()
            val email = binding.edEmail.text.toString()
            val address = binding.edInstitute.text.toString()
            val phone = binding.edPhone.text.toString()

            updateUserData(name, email, address, phone)
        }

        binding.btnEdit.setOnClickListener {
            isEditable = !isEditable
            reset()
        }

        return binding.root
    }

    fun reset() {
        binding.edName.isEnabled = isEditable
        binding.edEmail.isEnabled = isEditable
        binding.edInstitute.isEnabled = isEditable
        binding.edPhone.isEnabled = isEditable

        if (isEditable) {
            binding.btnSave.visibility = View.VISIBLE
        } else {
            binding.btnSave.visibility = View.GONE
        }
    }

    private fun updateUserData(name: String, email: String, address: String, phone: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.getReference("user").child(userId)

            val userData = hashMapOf<String,Any>(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone
            )
            userReference.updateChildren(userData).addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "Profile Update successfully 😊",
                    Toast.LENGTH_SHORT
                ).show()
            }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Profile Update Failed 😒", Toast.LENGTH_SHORT)
                        .show()
                }

        }

    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.getReference("user").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if (userProfile != null) {
                            binding.edName.setText(userProfile.name)
                            binding.edInstitute.setText(userProfile.address)
                            binding.edEmail.setText(userProfile.email)
                            binding.edPhone.setText(userProfile.phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

}