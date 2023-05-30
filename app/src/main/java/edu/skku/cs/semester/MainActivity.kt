package edu.skku.cs.semester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        https://mimisongsong.tistory.com/33
        val homeButton = findViewById<ImageButton>(R.id.homeButton)
        homeButton.setOnClickListener {
            // 클릭 이벤트에 대한 동작을 여기에 구현합니다.
            // 예: 버튼이 클릭되었을 때 수행할 작업
            Toast.makeText(this, "Home button clicked", Toast.LENGTH_SHORT).show()
        }

    }
}