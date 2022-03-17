package Menu.drinks

import Menu.RestaurantMenu
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.freshclub.R
import com.example.freshclub.databinding.ActivityPlatesBinding
import com.google.gson.Gson
import order.Order

class Staropramen : AppCompatActivity() {
    private lateinit var binding: ActivityPlatesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlatesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var price = 9
        var count = 1

        binding.personalizationView.visibility = View.INVISIBLE
        binding.ingredients.visibility = View.INVISIBLE
        binding.platesView.setImageResource(R.drawable.staropramen)
        binding.namePlatesView.setText("Staropramen 0.5l")
        binding.ingredientsPlateView.visibility = View.INVISIBLE
        binding.priceView.setText("$price RON")
        binding.minusButton.setOnClickListener{
            count--
            price -= 9
            if(count < 1) {
                count = 1
                price = 9
            }
            binding.countPlates.setText(count.toString())
            binding.priceView.setText("$price RON")
        }
        binding.plusButton.setOnClickListener{
            count++
            price += 9
            binding.countPlates.setText(count.toString())
            binding.priceView.setText("$price RON")
        }
        binding.addToOrderButton.setOnClickListener{
            val imageCode = R.drawable.staropramen
            val order = Order(imageCode, count.toString() + "x Staropramen", binding.personalizationView.text.toString(), price.toString())
            RestaurantMenu.orderListView.add(order)
            saveData()

            RestaurantMenu.newOrderString = RestaurantMenu.newOrderString + order.namePlate + " " + order.detailsPlate + "\n"

            RestaurantMenu.totalPrice += price
            writeSharePreferences()
            Toast.makeText(this, "Added to order", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@Staropramen, RestaurantMenu::class.java))
        }
    }

    fun writeSharePreferences(){
        val shared: SharedPreferences
        shared = getSharedPreferences("totalPrice", MODE_PRIVATE)
        val edit: SharedPreferences.Editor = shared.edit()
        edit.putInt("total", RestaurantMenu.totalPrice)
        edit.apply()
        edit.commit()
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("listMenu", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(RestaurantMenu.orderListView)
        editor.putString("menuListTask", json)
        editor.apply()
    }




}