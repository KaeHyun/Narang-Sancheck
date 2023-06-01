package edu.skku.cs.semester

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TempActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.store_item)

        // Intent로 전달된 데이터 받기
        val date = intent.getStringExtra("date")
        val hour = intent.getIntExtra("hour", 0)
        val minute = intent.getIntExtra("minute", 0)
        val steps = intent.getIntExtra("steps", 0)
        val walks = intent.getFloatExtra("walkDistance", 0f)
        val imagePath = intent.getStringExtra("imagePath")
        val summary = intent.getStringExtra("yourMood")

        val showMap = findViewById<ImageView>(R.id.mapImage)
        val bitmap = BitmapFactory.decodeFile(imagePath)
        showMap.setImageBitmap(bitmap)


        var showdate = findViewById<TextView>(R.id.showdate)
        showdate.text = date.toString()
        var time = findViewById<TextView>(R.id.timespent)
        time.text = "$hour : $minute"

        var dist = findViewById<TextView>(R.id.distance)
        val formattedDistance = String.format("%.2f", walks)
        dist.text = "$formattedDistance km"

        var count = findViewById<TextView>(R.id.steps)
        count.text = "$steps 걸음"

        var sum = findViewById<TextView>(R.id.contents)
        sum.text = summary
    }
}