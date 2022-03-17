package Menu.plates.pizza

import Menu.RestaurantMenu
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.freshclub.R
import com.example.freshclub.databinding.ActivityPlatesBinding
import com.google.gson.Gson
import order.Order

class Diavola : AppCompatActivity() {
    private lateinit var binding: ActivityPlatesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlatesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var price = 19
        var count = 1

        binding.platesView.setImageResource(R.drawable.diavola)
        binding.namePlatesView.setText("Diavola 30 cm")
        binding.ingredientsPlateView.setText("Sos de rosii, salam, pepperoncini, masline, mozzarella")
        binding.priceView.setText("$price RON")
        binding.minusButton.setOnClickListener{
            count--
            price -= 19
            if(count < 1) {
                count = 1
                price = 19
            }
            binding.countPlates.setText(count.toString())
            binding.priceView.setText("$price RON")
        }
        binding.plusButton.setOnClickListener{
            count++
            price += 19
            binding.countPlates.setText(count.toString())
            binding.priceView.setText("$price RON")
        }
        binding.addToOrderButton.setOnClickListener{
            val imageCode = R.drawable.diavola
            val order = Order(imageCode, count.toString() + "x Diavola 30 cm", binding.personalizationView.text.toString(), price.toString())
            RestaurantMenu.orderListView.add(order)
            saveData()

            RestaurantMenu.newOrderString = RestaurantMenu.newOrderString + order.namePlate + " " + order.detailsPlate + "\n"

            RestaurantMenu.totalPrice += price
            writeSharePreferences()
            Toast.makeText(this, "Added to order", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@Diavola, RestaurantMenu::class.java))
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