package edu.skku.cs.semester

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import okhttp3.OkHttpClient
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    var find_lat = 37.5665
    var find_lon = 126.9780

    lateinit var startBtn: ImageButton
    lateinit var stopBtn: ImageButton
    lateinit var resetBtn: ImageButton

    var running:Boolean = false
    var pauseTime = 0L //멈춘시간

    private val LOCATION_PERMISSION_REQUEST_CODE = 100

    private inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            if (mMap != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                //mMap!!.addMarker(MarkerOptions().position(latLng).title("Current Location")

                // 이동 경로 그리기
                if (running) {
                    drawPolyline(latLng)
                }

                //파란 점 업데이트
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                mMap?.isMyLocationEnabled = true
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}
    }



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

        //처음 위치
//        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        if (!isLocationEnabled) {
//            Toast.makeText(this, "위치 서비스가 비활성화 되었습니다!", Toast.LENGTH_SHORT).show()
//            val defaultLocation = LatLng(find_lat, find_lon) // Default location: Seoul, Korea
//            mMap.addMarker(MarkerOptions().position(defaultLocation).title("Default Location"))
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
//        }

        // 위치 업데이트를 위한 LocationManager 설정
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = MyLocationListener()

        // 위치 권한 요청
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // 위치 업데이트 시작

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10000, // 10초마다 업데이트
                0f,
                locationListener
            )
        }


        //시작, 정지, 리셋 버튼 찾기
        startBtn = findViewById(R.id.start_button)
        stopBtn = findViewById(R.id.stop_button)
        resetBtn = findViewById(R.id.reset_button)
        val endRun = findViewById<ImageButton>(R.id.end_run)


        startBtn.isEnabled = true
        stopBtn.isEnabled = false
        resetBtn.isEnabled = false
        running = false

        //시작 버튼 누르면, [
        startBtn.setOnClickListener{

            //정지 상태일때만 실행
            if(!running)
            {
                mMap?.clear() // 이동 경로 초기화
                chronometer.base= SystemClock.elapsedRealtime() - pauseTime
                //시작
                chronometer.start()
                startBtn.isEnabled = false
                stopBtn.isEnabled = true
                resetBtn.isEnabled = true
                running = true

                //위치 업데이트 시작
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000,
                    0f,
                    locationListener
                )
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

            mMap?.clear()
            previousLatLng = null
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
            // 이동 경로 캡쳐
            mMap?.snapshot { snapshot ->
                val imageBitmap = snapshot ?: return@snapshot

                val imageFile = saveBitmapToFile(imageBitmap)
                // EndRunActivity로 화면 전환
                val intent = Intent(this, EndRunActivity::class.java)
                intent.putExtra(EndRunActivity.EXTRA_IMAGE_FILE_PATH, imageFile.absolutePath)
                startActivity(intent)
            }
        }

    }

    private fun saveBitmapToFile(bitmap: Bitmap): File {
        val cachePath = File(cacheDir, "images")
        cachePath.mkdirs()

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        val file = File(cachePath, "temp_image.png")
        val fos = FileOutputStream(file)
        fos.write(byteArray)
        fos.close()

        return file
    }


    //지도
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val defaultLocation = LatLng(find_lat, find_lon) // Default location: Seoul, Korea
        mMap?.addMarker(MarkerOptions().position(defaultLocation).title("Default Location"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))

        // 현재 위치 표시 활성화
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
        }
        mMap?.isMyLocationEnabled = true

    }
    private var previousLatLng: LatLng? = null

    private fun drawPolyline(latLng: LatLng) {
        if (previousLatLng != null) {
            val polylineOptions = PolylineOptions()
                .color(Color.GREEN)
                .width(5f)
                .add(previousLatLng)
                .add(latLng)
            mMap?.addPolyline(polylineOptions)
        }
        previousLatLng = latLng
    }


}