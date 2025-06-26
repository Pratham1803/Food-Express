package com.example.adminfoodexpress.model

import java.security.Key

data class AllMenu(
    val key :String? = null ,
    val foodName :String? = null,
    val foodPrice :String? = null,
    val foodDescription :String? = null,
    val foodImage :String? = null,
    val foodIngredient :String? = null
)
