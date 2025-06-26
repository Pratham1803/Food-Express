package com.example.foodexpress

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.foodexpress.databinding.ActivityMainBinding
import com.example.foodexpress.fragment.*
import com.example.foodexpress.model.FoodItems
import com.example.foodexpress.model.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    //fragment bind in in screen
    private lateinit var binding: ActivityMainBinding
    private var homeFragment: HomeFragment = HomeFragment()
    private var historyFragment: HistoryFragment = HistoryFragment()
    private var cartFragment: CartFragment = CartFragment()
    private var profileFragment: ProfileFragment = ProfileFragment()
    private var searchFragment: searchFragment = searchFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCurrentFragment(homeFragment, "Food Express")

        setSupportActionBar(binding.toolbar)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {

                //set id in frafment
                R.id.homeFragment -> setCurrentFragment(homeFragment, "Food Express")
                R.id.cartFragment -> setCurrentFragment(cartFragment, "Cart")
                R.id.searchFragment -> setCurrentFragment(searchFragment, "Menu")
                R.id.historyFragment -> setCurrentFragment(historyFragment, "History")
                R.id.profileFragment -> setCurrentFragment(profileFragment, "Profile")

            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment, title: String) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_main_layout, fragment)
            commit()
            supportActionBar?.title = title
        }

    //logout start
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        menu?.findItem(R.id.imgLogOut)?.setOnMenuItemClickListener {
            val builder = AlertDialog.Builder(binding.root.context)
            builder.setTitle("Food Express")
            builder.setIcon(R.drawable.food_express_logo)
            builder.setMessage("Are you sure you want to log out?")

            builder.setPositiveButton("Yes") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, StartActivity::class.java)
                startActivity(intent)
                finish()
            }

            builder.setNegativeButton("No") { _, _ -> }
            builder.show()
            true
        }
        //notification start

//        menu?.findItem(R.id.imgNotification)?.setOnMenuItemClickListener {
//            val notificationFragment = NotificationBottomFragment()
//            notificationFragment.show(supportFragmentManager, "Notify")
//            true
//        }

        return super.onCreateOptionsMenu(menu)
    }

}

