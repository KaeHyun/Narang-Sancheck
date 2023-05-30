package edu.skku.cs.semester

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton


class StorageActivity : Activity() {
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
            val intent = Intent(this, PetInformationActivity::class.java)
            startActivity(intent)
        }


    }
}
