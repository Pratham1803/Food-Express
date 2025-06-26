package com.example.adminfoodexpress.adapter

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminfoodexpress.OrderDetailsActivity
import com.example.adminfoodexpress.databinding.DeliveryItemBinding
import com.example.adminfoodexpress.model.OrderDetails

class DeliveryAdapter(private val customerNames:MutableList<String>,private val moneyStatus:MutableList<Boolean>,private val arrFooditem: MutableList<OrderDetails>): RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DeliveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, OrderDetailsActivity::class.java)
            intent.putExtra("UserOrderDetails",arrFooditem[position])
            it.context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int  = customerNames.size

    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                customerName.text=customerNames[position]
                if (moneyStatus[position] == true){
                    statusMoney.text = "Received"
                }else{
                    statusMoney.text = "NotReceived"
                }
                val colorMap = mapOf(
                    true to Color.GREEN,false to Color.RED
                )
                statusMoney.setTextColor(colorMap[moneyStatus[position]]?: Color.BLACK)
                statusColor.backgroundTintList = ColorStateList.valueOf(colorMap[moneyStatus[position]]?: Color.BLACK)
            }
        }

    }
}