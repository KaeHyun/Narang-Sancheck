package edu.skku.cs.semester

import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import edu.skku.cs.semester.MyDatabaseHelper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.auth.api.signin.internal.Storage


class StoreItemActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.store_popup)

        // Intent로 전달된 데이터 받기
        val date = intent.getStringExtra("date")
        val minute= intent.getIntExtra("minute", 0)
        val second = intent.getIntExtra("second", 0)
        val steps  = intent.getIntExtra("steps", 0)
        var walks = intent.getFloatExtra("walkDistance", 0f)
        val imagePath = intent.getStringExtra("imagePath")
        val summary = intent.getStringExtra("yourMood")

        val showMap = findViewById<ImageView>(R.id.mapImage)
        val bitmap = BitmapFactory.decodeFile(imagePath)
        showMap.setImageBitmap(bitmap)


        var showdate = findViewById<TextView>(R.id.showdate)
        showdate.text = date.toString()
        var time = findViewById<TextView>(R.id.timespent)
        time.text = "$minute m $second s"

        var dist = findViewById<TextView>(R.id.distance)
        val formattedDistance = String.format("%.2f", walks)
        Log.d("이게 뭐누!! ", formattedDistance)
        dist.text = "$formattedDistance km"

        var count = findViewById<TextView>(R.id.steps)
        count.text = "$steps 걸음"

        var sum = findViewById<TextView>(R.id.contents)
        sum.text = summary


        val postBtn = findViewById<ImageButton>(R.id.postBtn) // 등록 버튼
        postBtn.setOnClickListener {

            val intent = Intent(this, StorageActivity::class.java)

            intent.putExtra("date", date)
            intent.putExtra("minute", minute)
            intent.putExtra("second", second)
            intent.putExtra("steps", steps)
            intent.putExtra("yourMood", summary.toString()) // EditText의 입력값을 추가
            intent.putExtra("walkDistance", walks)
            intent.putExtra("imagePath", imagePath)

            // MyDatabaseHelper 클래스의 인스턴스 생성
            val dbHelper = MyDatabaseHelper(this)

            // 쓰기 가능한 데이터베이스 가져오기
            val db = dbHelper.writableDatabase

            // ContentValues 객체를 생성하여 데이터 저장
            val values = ContentValues().apply {
                put(MyDatabaseHelper.COLUMN_DATE, date)
                put(MyDatabaseHelper.COLUMN_MINUTE, minute)
                put(MyDatabaseHelper.COLUMN_SECOND, second)
                put(MyDatabaseHelper.COLUMN_STEPS, steps)
                put(MyDatabaseHelper.COLUMN_WALK_DISTANCE, walks)
                put(MyDatabaseHelper.COLUMN_IMAGE_PATH, imagePath)
                put(MyDatabaseHelper.COLUMN_MOOD, summary)
            }

            // 데이터를 데이터베이스에 삽입
            db.insert(MyDatabaseHelper.TABLE_NAME, null, values)

            // 데이터베이스 연결 닫기
            db.close()

            // Intent를 실행하여 StorageActivity로 이동
            startActivity(intent)

            val tempIntent = Intent(this, TempActivity::class.java)
            // 데이터 전달을 위한 putExtra() 호출
            tempIntent.putExtra("date", date)
            tempIntent.putExtra("minute", minute)
            tempIntent.putExtra("second", second)
            tempIntent.putExtra("steps", steps)
            tempIntent.putExtra("yourMood", summary.toString())
            tempIntent.putExtra("walkDistance", walks)
            tempIntent.putExtra("imagePath", imagePath)
            // StoreItemActivity로 이동
            //startActivity(tempIntent)
        }
        val cancelBtn = findViewById<ImageButton>(R.id.cancelBtn) //취소 버튼
        cancelBtn.setOnClickListener {
            finish()
        }


    }
}