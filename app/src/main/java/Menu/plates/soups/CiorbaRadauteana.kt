package Menu.plates.soups

import Menu.RestaurantMenu
import Menu.RestaurantMenu.Companion.newOrderString
import Menu.RestaurantMenu.Companion.orderListView
import Menu.RestaurantMenu.Companion.totalPrice
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.freshclub.MainActivity.Companion.numberTable
import com.example.freshclub.R
import com.example.freshclub.databinding.ActivityPlatesBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_final_order.*
import kotlinx.android.synthetic.main.list_item.*
import order.Order


class CiorbaRadauteana : AppCompatActivity() {
    private lateinit var binding: ActivityPlatesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlatesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var price = 10
        var count = 1

        binding.platesView.setImageResource(R.drawable.ciorbaradauteana)
        binding.namePlatesView.setText("Ciorba Radauteana")
        binding.ingredientsPlateView.setText("Carne de pui, smantana, pastarnac, ceapa, morocov, patrunjel, usturoi")
        binding.priceView.setText("$price RON")
        binding.minusButton.setOnClickListener{
            count--
            price -= 10
            if(count < 1) {
                count = 1
                price = 10
            }
            binding.countPlates.setText(count.toString())
            binding.priceView.setText("$price RON")
        }
        binding.plusButton.setOnClickListener{
            count++
            price += 10
            binding.countPlates.setText(count.toString())
            binding.priceView.setText("$price RON")
        }
        binding.addToOrderButton.setOnClickListener{
            val imageCode = R.drawable.ciorbaradauteana
            val order = Order(imageCode, count.toString() + "x Ciorba Radauteana", binding.personalizationView.text.toString(), price.toString())
            orderListView.add(order)
            saveData()

            newOrderString = newOrderString + order.namePlate + " " + order.detailsPlate + "\n"

            totalPrice += price
            writeSharePreferences()
            Toast.makeText(this, "Added to order", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@CiorbaRadauteana, RestaurantMenu::class.java))
        }
    }

    fun writeSharePreferences(){
        val shared: SharedPreferences
        shared = getSharedPreferences("totalPrice", MODE_PRIVATE)
        val edit: SharedPreferences.Editor = shared.edit()
        edit.putInt("total", totalPrice)
        edit.apply()
        edit.commit()
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("listMenu", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(orderListView)
        editor.putString("menuListTask", json)
        editor.apply()
    }

}