package com.example.foodexpress

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.LocaleList
import android.renderscript.ScriptGroup.Binding
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.foodexpress.databinding.ActivityChooseYourLocationBinding
import com.example.foodexpress.databinding.ActivityMainBinding
import com.example.foodexpress.databinding.ActivityStartBinding

class Choose_your_location : AppCompatActivity() {
    private val binding: ActivityChooseYourLocationBinding by lazy {
        ActivityChooseYourLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val locationList = arrayOf("Cmpica","Iiim","Cspit","Rpcp","Pdpis")
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,locationList)
        val autoCompleteTextView  : AutoCompleteTextView = binding.ListOfLocation
        autoCompleteTextView.setAdapter(adapter)
    }
}