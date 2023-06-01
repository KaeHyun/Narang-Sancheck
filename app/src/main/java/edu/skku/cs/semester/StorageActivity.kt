package edu.skku.cs.semester

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class StorageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // PetInformationActivity의 동작 및 레이아웃 설정 등을 추가해주세요.
        setContentView(R.layout.activity_storage)

        val homeButton = findViewById<ImageButton>(R.id.homeButton2)
        homeButton.setOnClickListener {
            // 클릭 이벤트에 대한 동작을 여기에 구현합니다.
            // 예: 버튼이 클릭되었을 때 수행할 작업
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val petButton = findViewById<ImageButton>(R.id.petButton2)
        petButton.setOnClickListener {
            // 클릭 이벤트에 대한 동작을 여기에 구현합니다.
            // 예: 버튼이 클릭되었을 때 수행할 작업
            val intent = Intent(this, StorageActivity::class.java)
            startActivity(intent)
        }

        // Intent로 전달된 데이터 받기
        val date = intent.getStringExtra("date")
        val hour= intent.getIntExtra("hour", 0)
        val minute = intent.getIntExtra("minute", 0)
        val steps  = intent.getIntExtra("steps", 0)
        val walks = intent.getFloatExtra("walkDistance", 0f)
        val imagePath = intent.getStringExtra("imagePath")
        val summary = intent.getStringExtra("yourMood")
        val backgroundImage = intent.getIntExtra("backgroundImage", R.drawable.background)

    }
}
