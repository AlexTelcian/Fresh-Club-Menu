package Menu

import Menu.drinks.Becks
import Menu.drinks.Lipton
import Menu.drinks.Pepsi
import Menu.drinks.Staropramen
import Menu.plates.pizza.Capriciosa
import Menu.plates.pizza.Diavola
import Menu.plates.pizza.ProsciuttoFunghi
import Menu.plates.soups.CiorbaDeVacuta
import Menu.plates.soups.CiorbaRadauteana
import Menu.plates.specialities.BurgerPui
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.freshclub.MainActivity
import com.example.freshclub.databinding.ActivityRestaurantMenuBinding
import finall.order.FinalOrder
import order.Order

class RestaurantMenu : AppCompatActivity() {
    companion object {
        var totalPrice: Int = 0
        var orderListView: ArrayList<Order> = arrayListOf()
        var newOrderString: String? = ""
    }
    private lateinit var binding: ActivityRestaurantMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ciorbaRadauteanaButton.setOnClickListener{
            startActivity(Intent(this@RestaurantMenu, CiorbaRadauteana::class.java))
        }
        binding.ciorbaVacutaButton.setOnClickListener{
            startActivity(Intent(this@RestaurantMenu,CiorbaDeVacuta::class.java))
        }
        binding.capriciosaButton.setOnClickListener{
            startActivity(Intent(this@RestaurantMenu,Capriciosa::class.java))
        }

        binding.prosciuttoFunghiButton.setOnClickListener{
            startActivity(Intent(this@RestaurantMenu, ProsciuttoFunghi::class.java))
        }

        binding.diavolaButton.setOnClickListener{
            startActivity(Intent(this@RestaurantMenu, Diavola::class.java))
        }

        binding.burgerPuiButton.setOnClickListener{
            startActivity(Intent(this@RestaurantMenu, BurgerPui::class.java))
        }

        binding.liptonButton.setOnClickListener{
            startActivity(Intent(this@RestaurantMenu, Lipton::class.java))
        }

        binding.pepsiButton.setOnClickListener{
            startActivity(Intent(this@RestaurantMenu, Pepsi::class.java))
        }

        binding.becksButton.setOnClickListener{
            startActivity(Intent(this@RestaurantMenu, Becks::class.java))
        }

        binding.staropramenButton.setOnClickListener{
            startActivity(Intent(this@RestaurantMenu, Staropramen::class.java))
        }

        binding.viewOrderButton.setText("View order    -  "+ totalPrice + " RON")
        binding.viewOrderButton.setOnClickListener{
            startActivity(Intent(this,FinalOrder::class.java))
        }
        readSharedPreferences()
    }

    fun readSharedPreferences(){
        val shared = getSharedPreferences("totalPrice", MODE_PRIVATE)
        totalPrice= shared.getInt("total", 0)

        if(totalPrice != 0)
        binding.viewOrderButton.setText("View order    -  "+ totalPrice + " RON")
    }
}