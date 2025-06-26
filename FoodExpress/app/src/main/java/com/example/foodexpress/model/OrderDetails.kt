package com.example.foodexpress.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

class OrderDetails(): Serializable {
    var userUid: String = ""
    var userName: String = ""
    var foodNames: String = ""
    var foodImages: String = ""
    var foodPrices: String = ""
    var foodQuantities: Int = 0
    var address: String = ""
    var totalPrice: String = ""
    var phoneNumber: String = ""
    var orderAccepted: Boolean = false
    var paymentReceived: Boolean = false
    var itemPushKey: String = ""
    var currentTime: Long = 0
    var shopName : String = ""

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString().toString()
        userName = parcel.readString().toString()
        address = parcel.readString().toString()
        totalPrice = parcel.readString().toString()
        phoneNumber = parcel.readString().toString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString().toString()
        currentTime = parcel.readLong()
        shopName = parcel.readString().toString()
    }

    constructor(
        userId: String,
        name: String,
        foodItemName: String,
        foodItemPrice: String,
        foodItemImage: String,
        foodItemQuantities: Int,
        address: String,
        totalAmount: String,
        phone: String,
        time: Long,
        itemPushKey: String,
        b: Boolean,
        b1: Boolean,
        shopName: String
    ) : this(){
        this.userUid = userId
        this.userName = name
        this.foodNames = foodItemName
        this.foodPrices = foodItemPrice
        this.foodImages = foodItemImage
        this.foodQuantities = foodItemQuantities
        this.address =address
        this.totalPrice = totalAmount
        this.phoneNumber = phone
        this.currentTime = time
        this.itemPushKey = itemPushKey
        this.orderAccepted = orderAccepted
        this.paymentReceived = paymentReceived
        this.shopName = shopName
    }


    fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
        parcel.writeString(shopName)
    }

    fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }
}
