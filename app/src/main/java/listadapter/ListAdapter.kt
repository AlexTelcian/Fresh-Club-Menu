package listadapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.freshclub.R
import order.Order

class ListAdapter(private val context: Context,private val dataSource: ArrayList<Order>) : BaseAdapter() {

        private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    //1
    override fun getCount(): Int {
        return dataSource.size
    }

    //2
    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    //4
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.list_item, parent, false)

        val namePlateView = rowView.findViewById(R.id.namePlate) as TextView
        val pricePlateView = rowView.findViewById(R.id.pricePlate) as TextView
        val detailPlateView = rowView.findViewById(R.id.detailsPlate) as TextView
        val plateImageView = rowView.findViewById(R.id.plateImage) as ImageView

        val order = getItem(position) as Order

        namePlateView.text = order.namePlate
        pricePlateView.text = order.price + " RON"
        detailPlateView.text = order.detailsPlate
        plateImageView.setImageDrawable(plateImageView.resources.getDrawable(order.imageCode))

        return rowView
    }

}

