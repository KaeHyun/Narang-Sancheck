package edu.skku.cs.semester

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import java.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class EndRunActivity: AppCompatActivity() {
    companion object {
        const val EXTRA_IMAGE_FILE_PATH = "extra_image_file_path"
        const val EXTRA_PAUSE_TIME_HOURS = "extra_pause_time_hours"
        const val EXTRA_PAUSE_TIME_MINUTES = "extra_pause_time_minutes"
        const val EXTRA_STEP_COUNT = "step_count"
        const val EXTRA_DISTANCE = "extra_distance"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.end_run)

        //저장된 이미지 가져오기
        val imagePath = intent.getStringExtra(EXTRA_IMAGE_FILE_PATH)
        val imageView = findViewById<ImageView>(R.id.route_image)

        val bitmap = BitmapFactory.decodeFile(imagePath)
        imageView.setImageBitmap(bitmap)

        //오늘 날짜 출력하기
        val todayDateEditText = findViewById<TextView>(R.id.todayDate)
        val currentDate = getCurrentDate()
        todayDateEditText.text = currentDate//현재 날짜 표시

        //전달받은 시간, 분 값 가져오기
        val timeHour = intent.getIntExtra(EXTRA_PAUSE_TIME_HOURS,0)
        val timeMinute = intent.getIntExtra(EXTRA_PAUSE_TIME_MINUTES, 0)

        val runTime = findViewById<TextView>(R.id.runtime)
        val stringRuntime = "$timeHour 시간 $timeMinute 분 동안 함께했어요."
        runTime.text = stringRuntime

        //걸음 수 가져오기
        val stepCount = intent.getIntExtra(EXTRA_STEP_COUNT, 0)

        val counted = findViewById<TextView>(R.id.step1)
        val stringCount = "$stepCount 걸음을 걸었어요."
        counted.text = stringCount

        //이동 거리 받아오기
        val howLong = intent.getFloatExtra(EXTRA_DISTANCE, 0f)
        val kmText = findViewById<TextView>(R.id.kilometer)
        val stringKm = "$howLong km를 달렸어요."
        kmText.text = stringKm

        val yourMoodEditText = findViewById<EditText>(R.id.yourMood)

        val registerButton = findViewById<ImageButton>(R.id.imageButton2)
        registerButton.setOnClickListener {

            val intent = Intent(this, StoreItemActivity::class.java)
            intent.putExtra("date", currentDate)
            intent.putExtra("hour", timeHour)
            intent.putExtra("minute", timeMinute)
            intent.putExtra("steps", stepCount)
            intent.putExtra("yourMood", yourMoodEditText.text.toString()) // EditText의 입력값을 추가
            intent.putExtra("walkDistance", howLong)
            intent.putExtra("imagePath", imagePath)


            // Intent를 실행하여 StoreItemActivity로 이동
            startActivity(intent)


        }

    }

    private fun getCurrentDate():String{
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}
