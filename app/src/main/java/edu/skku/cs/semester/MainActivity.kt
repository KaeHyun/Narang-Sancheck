package edu.skku.cs.semester

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.provider.ContactsContract.CommonDataKinds.Im
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var find_lat = 37.5665
    var find_lon = 126.9780

    lateinit var startBtn: Button
    lateinit var stopBtn: Button
    lateinit var resetBtn: Button

    var running:Boolean = false;
    var pauseTime = 0L //멈춘시간

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        https://mimisongsong.tistory.com/33
        val chronometer: Chronometer = findViewById(R.id.chronometer)
        //페이지 이동 버튼
        val homeButton = findViewById<ImageButton>(R.id.homeButton)
        homeButton.setOnClickListener {
            // 클릭 이벤트에 대한 동작을 여기에 구현합니다.
            // 예: 버튼이 클릭되었을 때 수행할 작업
            Toast.makeText(this, "You are already in main page", Toast.LENGTH_SHORT).show()
        }

        val storeButton = findViewById<ImageButton>(R.id.storageButton)
        storeButton.setOnClickListener{
            val intent = Intent(this, StorageActivity::class.java)
            startActivity(intent)
        }

        val petButton = findViewById<ImageButton>(R.id.petButton)
        petButton.setOnClickListener {
            val intent = Intent(this, PetInformationActivity::class.java)
            startActivity(intent)
        }

        //지도
        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //날씨
        val client = OkHttpClient()
        val host = "https://api.weatherapi.com/v1/current.json"
        
        //현재 위치
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isLocationEnabled) {
            Toast.makeText(this, "위치 서비스가 비활성화 되었습니다!", Toast.LENGTH_SHORT).show()
            val defaultLocation = LatLng(find_lat, find_lon) // Default location: Seoul, Korea
            mMap.addMarker(MarkerOptions().position(defaultLocation).title("Default Location"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
        }

        //시작, 정지, 리셋 버튼 찾기
        startBtn = findViewById(R.id.start_button)
        stopBtn = findViewById(R.id.stop_button)
        resetBtn = findViewById(R.id.reset_button)
        val endRun = findViewById<Button>(R.id.end_run)


        startBtn.isEnabled = true
        stopBtn.isEnabled = false
        resetBtn.isEnabled = false
        running = false

        //시작 버튼 누르면, [
        startBtn.setOnClickListener{
            //정지 상태일때만 실행
            if(!running)
            {
                chronometer.base= SystemClock.elapsedRealtime() - pauseTime

                //시작
                chronometer.start()
                startBtn.isEnabled = false
                stopBtn.isEnabled = true
                resetBtn.isEnabled = true
                running = true
            }
        }
        //정지 버튼 누르면,
        stopBtn.setOnClickListener {
            if(running)
            {
                chronometer.stop()
                pauseTime=SystemClock.elapsedRealtime() - chronometer.base
                startBtn.isEnabled = true
                stopBtn.isEnabled = false
                resetBtn.isEnabled = false
                running = false
            }
        }

        //리셋 버튼 누르면,
        resetBtn.setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime()

            pauseTime=0L
            chronometer.stop()
            startBtn.isEnabled = true
            stopBtn.isEnabled = false
            resetBtn.isEnabled = false
            running = false
        }

        endRun.setOnClickListener {
            //산책 끝내기 버튼을 누르면, 상관없이 정지
            if(running)
            {
                chronometer.stop()
                pauseTime=SystemClock.elapsedRealtime() - chronometer.base
                startBtn.isEnabled = true
                stopBtn.isEnabled = false
                resetBtn.isEnabled = false
                running = false
                Toast.makeText(this, "Pause Time: $pauseTime milliseconds", Toast.LENGTH_SHORT).show()
                chronometer.base = SystemClock.elapsedRealtime()
                pauseTime=0L
            }
            else if(!running)
            {
                Toast.makeText(this, "Pause Time: $pauseTime milliseconds", Toast.LENGTH_SHORT).show()
                chronometer.base = SystemClock.elapsedRealtime()
                pauseTime=0L
            }
        }


    }
    
    
    //지도
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val defaultLocation = LatLng(find_lat, find_lon) // Default location: Seoul, Korea
        mMap.addMarker(MarkerOptions().position(defaultLocation).title("Default Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
    }
}