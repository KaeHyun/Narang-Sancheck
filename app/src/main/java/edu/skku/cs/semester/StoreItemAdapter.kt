package edu.skku.cs.semester

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

data class StoreItem(
    val date: String?,
    val hour: Int,
    val minute: Int,
    val steps: Int,
    val walks: Float,
    val imagePath: String?,
    val summary: String?,
    val backgroundImage: Int
)


class StoreItemAdapter(
    private val context: Context,
    private val storeItems: ArrayList<StoreItem>,
    private val listView: ListView
) : BaseAdapter() {

    override fun getCount(): Int {
        return storeItems.size
    }

    override fun getItem(position: Int): Any {
        return storeItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val storeItem = getItem(position)

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.store_item, parent, false)
        }

        val postBtn = convertView?.findViewById<ImageButton>(R.id.postBtn)
        postBtn?.setOnClickListener {
            // Handle postBtn click event
            // Add the storeItem to the listView
            listView.adapter = this
            if (storeItem != null) {
                storeItems.add(storeItem as StoreItem)
            }
            notifyDataSetChanged()
        }

        return convertView!!
    }
}
