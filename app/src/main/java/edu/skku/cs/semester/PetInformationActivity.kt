package edu.skku.cs.semester

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PetInformationActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // PetInformationActivity의 동작 및 레이아웃 설정 등을 추가해주세요.
        setContentView(R.layout.pet_information)

        val homeButton = findViewById<ImageButton>(R.id.homeButton3)
        homeButton.setOnClickListener {
            // 클릭 이벤트에 대한 동작을 여기에 구현합니다.
            // 예: 버튼이 클릭되었을 때 수행할 작업
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val storeButton = findViewById<ImageButton>(R.id.storageButton3)
        storeButton.setOnClickListener {
            // 클릭 이벤트에 대한 동작을 여기에 구현합니다.
            // 예: 버튼이 클릭되었을 때 수행할 작업
            val intent = Intent(this, StorageActivity::class.java)
            startActivity(intent)
        }


    }
}
