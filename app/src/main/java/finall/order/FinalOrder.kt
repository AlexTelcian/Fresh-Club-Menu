package finall.order

import Menu.RestaurantMenu
import Menu.RestaurantMenu.Companion.newOrderString
import Menu.RestaurantMenu.Companion.orderListView
import Menu.RestaurantMenu.Companion.totalPrice
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.icu.number.IntegerWidth
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.text.isDigitsOnly
import com.example.freshclub.MainActivity
import com.example.freshclub.MainActivity.Companion.numberTable
import com.example.freshclub.R
import com.example.freshclub.databinding.ActivityFinalOrderBinding
import com.example.freshclub.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_final_order.*
import kotlinx.android.synthetic.main.activity_main.*
import listadapter.ListAdapter
import order.Order
import order.pay.Pay
import java.util.*
import kotlin.collections.ArrayList

class FinalOrder : AppCompatActivity() {
    var orderFinal: String? = "Table " + numberTable + "\n"
    private lateinit var binding: ActivityFinalOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)


        orderFinal += newOrderString

        saveFinalOrder()
        checkPlaceOrder()
        readSharedPreferences()
        loadData()
        removeItemFromList()


        var adapter = ListAdapter(this, orderListView)
        binding.listviewId.adapter = adapter

        binding.cancelButton.setOnClickListener{
            newOrderString = ""
            totalPrice = 0
            orderListView.clear()
            binding.listviewId.adapter = null
            val edit: SharedPreferences.Editor = getSharedPreferences("ListMenu", MODE_PRIVATE).edit()
            edit.clear()
            val e: SharedPreferences.Editor = getSharedPreferences("totalPrice", MODE_PRIVATE).edit()
            e.clear()
            e.apply()

            saveData()
            adapter.notifyDataSetChanged()
            startActivity(Intent(this@FinalOrder,RestaurantMenu::class.java))

        }
        binding.placeOrderButton.setText("Place the Order")
        numberPlateIndex()

        binding.payButton.setOnClickListener{
            startActivity(Intent(this@FinalOrder, Pay::class.java))
        }

        binding.newOrder.setOnClickListener {
            startActivity(Intent(this@FinalOrder, RestaurantMenu::class.java))
        }


    }
    private fun placeOrderDialog(){
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Place order?")
        dialog.setMessage("You have to pay: $totalPrice RON \uD83D\uDCB0")
        dialog.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
            writeToDB()
            binding.payButton.visibility = View.VISIBLE
            binding.cancelButton.visibility = View.INVISIBLE
            dialog.dismiss()
        })

        dialog.setNegativeButton("No", DialogInterface.OnClickListener{
            dialog, which ->
            dialog.dismiss()
        })

        dialog.show()
    }

    private fun preparationTime(number: Int?){
        val database = Firebase.database
        val doneDB = database.getReference("Orders")
        doneDB.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                var done = snapshot.child("order $number").child("done").getValue<Boolean>()

                if(done != true) {
                    binding.preparationLayout.setBackgroundColor(resources.getColor(R.color.appcolor))
                    binding.waitinPreparationIcon.setImageResource(R.drawable.ic_time)
                    binding.preparationText.setText("Your order is in preparation")
                }else {
                    createNotification()
                    onStop()
                    binding.waitinPreparationIcon.setImageResource(R.drawable.ic_check)
                    binding.preparationText.setText("Your order is ready")
                    binding.preparationLayout.setBackgroundColor(resources.getColor(R.color.green))
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun writeToDB() {
        val database = Firebase.database
        val orderDB = database.getReference("Orders")
        val numberPlate = database.getReference("numberplate")
        numberPlate.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                    var number = snapshot.getValue<Int>()
                    orderDB.child("order $number").child("details").setValue(orderFinal)
                    orderDB.child("order $number").child("done").setValue(false)

                    preparationTime(number)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun readSharedPreferences(){
        val shared = getSharedPreferences("totalPrice", MODE_PRIVATE)
        totalPrice= shared.getInt("total", 0)

        if(totalPrice != 0)
        binding.placeOrderButton.setText("Place Order " + totalPrice + " Ron")
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("listMenu", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("menuListTask", "")
        val type = object: TypeToken<ArrayList<Order>>() {
        }.type

        if(json != "")
            orderListView = gson.fromJson(json, type)
    }

    private fun checkPlaceOrder(){
        val shared: SharedPreferences
        shared = getSharedPreferences("savePlaceOrder", MODE_PRIVATE)
        val ok = shared.getInt("ok",0)

        if(ok == 1) {
            loadFinalOrder()
        }
        else {
            val editor: SharedPreferences.Editor = getSharedPreferences("infoTable", MODE_PRIVATE).edit()
            editor.clear()
            editor.apply()
        }
    }

    private fun loadFinalOrder(){
        val shared: SharedPreferences
        shared = getSharedPreferences("saveFinalOrder", MODE_PRIVATE)
        orderFinal = shared.getString("finalOrder","")

        if(orderFinal != "") {
            binding.payButton.visibility = View.VISIBLE
            binding.cancelButton.visibility = View.INVISIBLE
        }
        val database = Firebase.database
        val numberPlate = database.getReference("numberplate")
        numberPlate.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                var number = snapshot.getValue<Int>()
                preparationTime(number)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })

    }

    private fun saveCheckPlaceOrder(ok: Int){
        val shared: SharedPreferences
        shared = getSharedPreferences("savePlaceOrder", MODE_PRIVATE)
        val edit: SharedPreferences.Editor = shared.edit()
        edit.putInt("ok", ok)
        edit.apply()
        edit.commit()
    }


    fun saveFinalOrder(){
        val shared: SharedPreferences
        shared = getSharedPreferences("saveFinalOrder", MODE_PRIVATE)
        val edit: SharedPreferences.Editor = shared.edit()
        edit.putString("finalOrder", orderFinal)
        edit.apply()
        edit.commit()
    }

    private fun createNotification() {
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        val mBuilder = NotificationCompat.Builder(applicationContext, default_notification_channel_id)
        mBuilder.setContentTitle("Your order is ready \uD83C\uDF7D️")
        mBuilder.setContentText("The order will reach you shortly. Good appetite !")
        mBuilder.setSmallIcon(R.drawable.ic_launcher_round)
        mBuilder.setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance)
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            assert(mNotificationManager != null)
            mNotificationManager!!.createNotificationChannel(notificationChannel)
        }
        assert(mNotificationManager != null)
        mNotificationManager!!.notify(System.currentTimeMillis().toInt(), mBuilder.build())
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "10001"
        private const val default_notification_channel_id = "default"
    }

    private fun numberPlateIndex(){
        val database = Firebase.database
        val numberPlate = database.getReference("numberplate")
        numberPlate.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                var number = snapshot.getValue<Int>()
                if (number != null)
                    number = number.inc()
                binding.placeOrderButton.setOnClickListener{
                    saveCheckPlaceOrder(1)
                    numberPlate.setValue(number)

                    if(orderFinal == "") {
                        binding.preparationLayout.setBackgroundColor(resources.getColor(R.color.white))
                        Toast.makeText(applicationContext,"You didn't order anything",Toast.LENGTH_SHORT).show()
                    }else {
                        placeOrderDialog()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun removeItemFromList(){
        val adapter = ListAdapter(this, orderListView)

        binding.listviewId.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val shared: SharedPreferences
            shared = getSharedPreferences("savePlaceOrder", MODE_PRIVATE)
            val ok = shared.getInt("ok",0)

            if(ok == 1) {
                Toast.makeText(this,"Unable to remove, the order has been sent",Toast.LENGTH_SHORT).show()
            }else {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Are you sure?")
                dialog.setMessage("Do you want to delete ${orderListView.get(i).namePlate}?")
                dialog.setNegativeButton("No", null)
                dialog.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    totalPrice -= Integer.parseInt(orderListView.get(i).price)
                    writeSharePreferences()
                    orderListView.removeAt(i)
                    adapter.notifyDataSetChanged()
                    saveData()
                    dialog.dismiss()
                    startActivity(Intent(this, FinalOrder::class.java))
                })
                dialog.show()
            }
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("listMenu", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(RestaurantMenu.orderListView)
        editor.putString("menuListTask", json)
        editor.apply()
    }
    fun writeSharePreferences(){
        val shared: SharedPreferences
        shared = getSharedPreferences("totalPrice", MODE_PRIVATE)
        val edit: SharedPreferences.Editor = shared.edit()
        edit.putInt("total", RestaurantMenu.totalPrice)
        edit.apply()
        edit.commit()
    }

}