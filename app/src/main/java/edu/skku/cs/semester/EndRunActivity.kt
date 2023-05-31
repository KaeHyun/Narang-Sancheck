package edu.skku.cs.semester

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import java.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class EndRunActivity: AppCompatActivity() {
    companion object {
        const val EXTRA_IMAGE_FILE_PATH = "extra_image_file_path"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.end_run)

        //저장된 이미지 가져오기
        val imagePath = intent.getStringExtra(EXTRA_IMAGE_FILE_PATH)
        val imageView = findViewById<ImageView>(R.id.route_image)

        val bitmap = BitmapFactory.decodeFile(imagePath)
        imageView.setImageBitmap(bitmap)

        //오늘 날짜 출력하기
        val todayDateEditText = findViewById<EditText>(R.id.todayDate)
        val currentDate = getCurrentDate()
        todayDateEditText.setText(currentDate)//현재 날짜 표시
    }

    private fun getCurrentDate():String{
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}
