package edu.skku.cs.semester

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class FeedAdapter(private val feedItems: List<StorageActivity.FeedItem>) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    private var deleteListener: OnItemDeleteListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.store_item, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val feedItem = feedItems[position]
        holder.bind(feedItem)
    }

    override fun getItemCount(): Int {
        return feedItems.size
    }

    fun setOnItemDeleteListener(listener: OnItemDeleteListener) {
        deleteListener = listener
    }


    inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(feedItem: StorageActivity.FeedItem) {
            // 피드 항목의 데이터를 사용하여 UI 업데이트
            // 예: TextView 등에 데이터를 설정
            val dateTextView = itemView.findViewById<TextView>(R.id.showdate)
            dateTextView.text = feedItem.date
            val mapImageView = itemView.findViewById<ImageView>(R.id.mapImage)

            val imagePath = feedItem.imagePath
            if (imagePath != null) {
                val bitmap = BitmapFactory.decodeFile(imagePath)
                mapImageView.setImageBitmap(bitmap)
            }

            val timeSpend = itemView.findViewById<TextView>(R.id.timespent)
            val timeString = "${feedItem.hour} : ${feedItem.minute}"
            timeSpend.text = timeString

            val distance = itemView.findViewById<TextView>(R.id.distance)
            val distanceString = "${feedItem.walks} km"
            distance.text = distanceString

            val walk = itemView.findViewById<TextView>(R.id.steps)
            val walkString = "${feedItem.walks} 걸음"
            walk.text = walkString

            val summary = itemView.findViewById<TextView>(R.id.contents)
            summary.text = feedItem.summary

            // 아이템 클릭 시 다이얼로그 띄우기
            itemView.setOnClickListener {
                showDeleteDialog(adapterPosition)
            }

        }
        private fun showDeleteDialog(position: Int) {
            val alertDialog = AlertDialog.Builder(itemView.context)
                .setTitle("Delete Item")
                .setMessage("정말 삭제하시겠습니까?")
                .setPositiveButton("삭제") { _, _ ->
                    deleteListener?.onItemDeleteConfirmed(position)
                }
                .setNegativeButton("취소") { _, _ ->
                    deleteListener?.onItemDeleteCanceled()
                }
                .create()
            alertDialog.show()
        }
    }
    interface OnItemDeleteListener {
        fun onItemDeleteConfirmed(position: Int)
        fun onItemDeleteCanceled()
    }


}
