package com.example.freshclub

import Menu.RestaurantMenu
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.freshclub.databinding.ActivityMainBinding
import com.example.freshclub.databinding.ActivityRestaurantMenuBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import finall.order.FinalOrder
import order.pay.Pay

class MainActivity : AppCompatActivity() {
    companion object{
        var numberTable: String? = ""
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        readingSharePreferences()

                    binding.orderFoodButton.setOnClickListener {
                        writeSharePreferences()

                        if(numberTable == "")
                            Toast.makeText(applicationContext,"Please insert table's number",Toast.LENGTH_SHORT).show()
                        else {
                            startActivity(Intent(this@MainActivity, RestaurantMenu::class.java))
                        }
                    }


        checkPlaceOrder()

        binding.viewOrder.setOnClickListener{
            startActivity(Intent(this@MainActivity,FinalOrder::class.java))
        }

    }

    private fun checkPlaceOrder(){
        val shared: SharedPreferences
        shared = getSharedPreferences("savePlaceOrder", MODE_PRIVATE)
        val ok = shared.getInt("ok",0)

        if(ok == 1) {
            binding.viewOrder.visibility = View.VISIBLE
            binding.orderFoodButton.visibility = View.INVISIBLE
            binding.tableNumber.visibility = View.INVISIBLE
        }
        else {
            binding.viewOrder.visibility = View.INVISIBLE
            binding.orderFoodButton.visibility = View.VISIBLE
            binding.tableNumber.visibility = View.VISIBLE
        }
    }
    private fun writeSharePreferences(){
        val pref: SharedPreferences
        pref = getSharedPreferences("infoTable", MODE_PRIVATE);
        val editor = pref.edit()
            numberTable = binding.tableNumber.text.toString()
            editor.putString("numberTable", numberTable)
            editor.apply()
            editor.commit()
        }

    private fun readingSharePreferences(){
        val shared = getSharedPreferences("infoTable", MODE_PRIVATE)
        numberTable = shared.getString("numberTable", "")

        if(numberTable == "") {
            binding.tableNumber.visibility = View.VISIBLE
            binding.orderFoodButton.visibility = View.VISIBLE
        }
        else{
            binding.viewOrder.visibility = View.VISIBLE
            binding.orderFoodButton.visibility = View.INVISIBLE
            binding.tableNumber.visibility = View.INVISIBLE
        }
    }

}