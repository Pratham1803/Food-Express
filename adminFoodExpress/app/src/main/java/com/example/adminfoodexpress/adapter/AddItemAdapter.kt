package com.example.adminfoodexpress.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminfoodexpress.databinding.ItemBinding

class AddItemAdapter(private val MenuItemName:ArrayList<String>, private val MenuItemPrice:ArrayList<String>, private val MenuItemImage:ArrayList<Int>) : RecyclerView.Adapter<AddItemAdapter.AddItemViewHolder>() {
//    var create for quantities "+" and "-" and {1} = is indexing start from 1
    private val itemQuantities = IntArray(MenuItemName.size){1}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AddItemViewHolder(binding)
    }
    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int  = MenuItemName.size

    inner class AddItemViewHolder (private val binding: ItemBinding ) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                FoodNameTextView.text=MenuItemName[position]
                PriceTextView.text=MenuItemPrice[position]
                FoodImageView.setImageResource(MenuItemImage[position])
                //click listener for minsu button
//                minsuButton.setOnClickListener {
//                    decreaseQuantitiy()
//                }



            }
        }

    }



}