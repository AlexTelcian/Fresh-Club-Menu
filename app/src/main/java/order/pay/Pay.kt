package order.pay

import Menu.RestaurantMenu
import Menu.RestaurantMenu.Companion.totalPrice
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.freshclub.MainActivity.Companion.numberTable
import com.example.freshclub.databinding.ActivityPayBinding
import com.example.freshclub.databinding.ActivityRestaurantMenuBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_pay.*

class Pay : AppCompatActivity() {
    private lateinit var binding: ActivityPayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.totalPayment.setText("$totalPrice RON")

        clearData()
    }

     fun clearData(){
        val editor: SharedPreferences.Editor = getSharedPreferences("infoTable", MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
        val e: SharedPreferences.Editor = getSharedPreferences("totalPrice", MODE_PRIVATE).edit()
        e.clear()
        e.apply()
        val ed: SharedPreferences.Editor = getSharedPreferences("saveFinalOrder", MODE_PRIVATE).edit()
        ed.clear()
        ed.apply()
        val edit: SharedPreferences.Editor = getSharedPreferences("ListMenu", MODE_PRIVATE).edit()
        edit.clear()
        edit.apply()
        val edi: SharedPreferences.Editor = getSharedPreferences("savePlaceOrder", MODE_PRIVATE).edit()
        edi.clear()
        edi.apply()

    }

}