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


        val feedItems = getAllFeedItems() // 모든 피드 항목을 가져오는 함수 호출

        // 어댑터 생성 및 설정
        val adapter = FeedAdapter(feedItems)
        feedRecyclerView.adapter = adapter

    }

    private fun getAllFeedItems(): List<FeedItem> {
        val feedItems = mutableListOf<FeedItem>()

        // SQLite 데이터베이스에서 데이터를 가져옴
        val dbHelper = MyDatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            MyDatabaseHelper.COLUMN_DATE,
            MyDatabaseHelper.COLUMN_HOUR,
            MyDatabaseHelper.COLUMN_MINUTE,
            MyDatabaseHelper.COLUMN_STEPS,
            MyDatabaseHelper.COLUMN_WALK_DISTANCE,
            MyDatabaseHelper.COLUMN_IMAGE_PATH,
            MyDatabaseHelper.COLUMN_MOOD
        )
        val cursor = db.query(
            MyDatabaseHelper.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        // 데이터 리스트에 피드 항목 추가
        with(cursor) {
            while (moveToNext()) {
                val date = getString(getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DATE))
                val hour = getInt(getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_HOUR))
                val minute = getInt(getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_MINUTE))
                val steps = getInt(getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_STEPS))
                val walks = getFloat(getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_WALK_DISTANCE))
                val imagePath = getString(getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_IMAGE_PATH))
                val summary = getString(getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_MOOD))

                val feedItem = FeedItem(date, hour, minute, steps, walks, imagePath, summary)
                feedItems.add(feedItem)
            }
        }

        cursor.close()
        db.close()

        return feedItems
    }
}

