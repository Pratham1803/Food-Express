package com.example.adminfoodexpress

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.renderscript.ScriptGroup.Binding
import android.widget.Toast
//import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminfoodexpress.databinding.ActivityAddItemBinding
import com.example.adminfoodexpress.model.Admin
import com.example.adminfoodexpress.model.AllMenu
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

//import com.google.firebase.storage.FirebaseStorageKtxRegistrar

class addItemActivity : AppCompatActivity() {
    //create a var for send and rec the data from database
    //food item details
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodInagridient: String
    private var foodImageUri: Uri? = null

    //firebase var
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase


    //add binding in additem

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        FirebaseApp.initializeApp(binding.root.context)
        //initialize firebase
        auth = FirebaseAuth.getInstance()
        //initialize firebase database instanse
        database = FirebaseDatabase.getInstance()

        binding.AddItemButton.setOnClickListener {
            //get data from filed
            foodName = binding.FoodName.text.toString().trim()
            foodPrice = binding.FoodPrice.text.toString().trim()
            foodDescription = binding.Description.text.toString().trim()
            foodInagridient = binding.Ingredint.text.toString().trim()
//            foodName = binding.FoodName.text.toString().trim()

            if (!(foodName.isBlank() || foodPrice.isBlank() || foodDescription.isBlank() || foodInagridient.isBlank())) {
                uploadData()
                Toast.makeText(this, "item add Successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Fill All Details  ", Toast.LENGTH_SHORT).show()
            }
        }

        //add image
        binding.SelectImage.setOnClickListener {
            picImage.launch("image/*")
        }

//        binding.SelectImage.setOnClickListener {
//            picImage.launch(PickVisualMediaRequest(ActivityResultContracts.GetContent.ImageAndVideo))
//
//        }
        binding.BackButton.setOnClickListener {
            finish()
        }
    }

    private fun uploadData() {
        val menuReference: DatabaseReference = database.getReference().child("admin").child(Admin.userName).child("menu")
        val newItemKey: String? = menuReference.push().key

        if (foodImageUri != null) {

            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef: StorageReference = storageRef.child("menu_Images/${newItemKey}.jpg")
            val uploadTask: UploadTask = imageRef.putFile(foodImageUri!!)

            //UploadTask
            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    //create  a new menuitem
                    val newItem = AllMenu(

                        foodName = foodName,
                        foodPrice = foodPrice,
                        foodDescription = foodDescription,
                        foodIngredient = foodInagridient,
                        foodImage = downloadUri.toString(),

                        )
                    //create unic key for all
                    newItemKey?.let { key ->
                        ///////19:00
                        menuReference.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "data uploade successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                            .addOnFailureListener {
                                Toast.makeText(this, "data uploade Failed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }

                }

            }
                .addOnFailureListener {
                    Toast.makeText(this, "Image Upload failed", Toast.LENGTH_SHORT).show()
                }

        } else {
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show()
        }
    }

    //picimage var is start
    private val picImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.SelecetedImage.setImageURI(uri)
            foodImageUri = uri
        }
    }
}