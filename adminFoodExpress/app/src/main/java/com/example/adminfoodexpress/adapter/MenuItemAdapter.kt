package com.example.adminfoodexpress.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfoodexpress.AllItemActivity
import com.example.adminfoodexpress.databinding.ItemBinding
import com.example.adminfoodexpress.model.AllMenu
import com.google.firebase.database.DatabaseReference

class MenuItemAdapter(
    private val context: AllItemActivity,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener:(position :Int) ->Unit
) : RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {
    private val itemQuantities = IntArray(menuList.size) { 1 }
//    var create for quantities "+" and "-" and {1} = is indexing start from 1
//    private val itemQuantities = IntArray(MenuItemName.size){1}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size

    inner class AddItemViewHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                binding.apply {
                    binding.DeleteButton.setOnClickListener {  }
                    //create var
                    val menuItem: AllMenu = menuList[position]
                    val uriString: String? = menuItem.foodImage
                    val uri: Uri = Uri.parse(uriString)

                    FoodNameTextView.text = menuItem.foodName
                    PriceTextView.text = menuItem.foodPrice

                    Glide.with(context).load(uri).into(FoodImageView)

                    DeleteButton.setOnClickListener {
                        Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                        onDeleteClickListener(position)
                    }
                }
            }
        }

    }


}