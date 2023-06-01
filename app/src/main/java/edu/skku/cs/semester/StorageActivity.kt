package edu.skku.cs.semester

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class StorageActivity : AppCompatActivity() {

    // FeedItem 클래스 정의
    data class FeedItem(
        val date: String?,
        val hour: Int,
        val minute: Int,
        val steps: Int,
        val walks: Float,
        val imagePath: String?,
        val summary: String?,
    )

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


        // RecyclerView 초기화
        val feedRecyclerView = findViewById<RecyclerView>(R.id.showRecycleView)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        feedRecyclerView.layoutManager = layoutManager

        // 데이터 리스트 생성
        val feedItems = createFeedItems() // 피드 항목 리스트를 생성하는 함수 호출

        // 어댑터 생성 및 설정
        val adapter = FeedAdapter(feedItems)
        feedRecyclerView.adapter = adapter

    }

    private fun createFeedItems(): List<FeedItem> {
        val feedItems = mutableListOf<FeedItem>()

        val date = intent.getStringExtra("date")
        val hour = intent.getIntExtra("hour", 0)
        val minute = intent.getIntExtra("minute", 0)
        val steps = intent.getIntExtra("steps", 0)
        val walks = intent.getFloatExtra("walkDistance", 0f)
        val imagePath = intent.getStringExtra("imagePath")
        val summary = intent.getStringExtra("yourMood")

        // 데이터 리스트에 피드 항목 추가
        val feedItem = FeedItem(date, hour, minute, steps, walks, imagePath, summary)
        feedItems.add(feedItem)

        return feedItems

    }
}
