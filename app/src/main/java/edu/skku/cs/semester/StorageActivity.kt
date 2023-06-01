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


class StorageActivity : AppCompatActivity(), FeedAdapter.OnItemDeleteListener {

    // FeedItem 클래스 정의
    data class FeedItem(
        val date: String?,
        val minute: Int,
        val second: Int,
        val steps: Int,
        val walks: Float,
        val imagePath: String?,
        val summary: String?,
    )

    private lateinit var adapter: FeedAdapter
    private lateinit var feedItems: MutableList<FeedItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // PetInformationActivity의 동작 및 레이아웃 설정 등을 추가해주세요.
        setContentView(R.layout.activity_storage)


        val homeButton = findViewById<ImageButton>(R.id.homeButton2)
        homeButton.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val petButton = findViewById<ImageButton>(R.id.petButton2)
        petButton.setOnClickListener {
            feedItems = getAllFeedItems() as MutableList<FeedItem>
            adapter.notifyDataSetChanged()

            val intent = Intent(this, StorageActivity::class.java)
            startActivity(intent)
        }

        feedItems = getAllFeedItems() as MutableList<FeedItem>

        // RecyclerView 초기화
        val feedRecyclerView = findViewById<RecyclerView>(R.id.showRecycleView)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        feedRecyclerView.layoutManager = layoutManager


        // 어댑터 생성 및 설정
        adapter = FeedAdapter(feedItems)

        adapter.setOnItemDeleteListener(this) // 삭제 이벤트 리스너 설정

        feedRecyclerView.adapter = adapter

    }

    override fun onItemDeleteConfirmed(position: Int) {
        val dbHelper = MyDatabaseHelper(this)
        val db = dbHelper.writableDatabase

        // 삭제할 아이템의 정보 가져오기
        val item = feedItems[position]

        // 데이터베이스에서 해당 아이템 삭제
        val selection = "${MyDatabaseHelper.COLUMN_DATE} = ? AND " +
                "${MyDatabaseHelper.COLUMN_MINUTE} = ? AND " +
                "${MyDatabaseHelper.COLUMN_SECOND} = ?"
        val selectionArgs = arrayOf(item.date, item.minute.toString(), item.second.toString())
        db.delete(MyDatabaseHelper.TABLE_NAME, selection, selectionArgs)

        db.close()

        // 어댑터에서 삭제된 아이템 제거
        feedItems.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    override fun onItemDeleteCanceled() {
        // 아이템 삭제 취소 시 필요한 동작 수행
        // 예: 다이얼로그 닫기 등
    }
    private fun getAllFeedItems(): List<FeedItem> {
        val feedItems = mutableListOf<FeedItem>()

        // SQLite 데이터베이스에서 데이터를 가져옴
        val dbHelper = MyDatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            MyDatabaseHelper.COLUMN_DATE,
            MyDatabaseHelper.COLUMN_MINUTE,
            MyDatabaseHelper.COLUMN_SECOND,
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
                val minute = getInt(getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_MINUTE))
                val second = getInt(getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_SECOND))
                val steps = getInt(getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_STEPS))
                val walks = getFloat(getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_WALK_DISTANCE))
                val imagePath = getString(getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_IMAGE_PATH))
                val summary = getString(getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_MOOD))

                val feedItem = FeedItem(date, minute, second, steps, walks, imagePath, summary)
                feedItems.add(feedItem)
            }
        }

        cursor.close()
        db.close()

        return feedItems
    }


}

