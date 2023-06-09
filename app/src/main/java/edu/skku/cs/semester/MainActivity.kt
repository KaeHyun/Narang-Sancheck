package edu.skku.cs.semester

import android.Manifest
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
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.Field
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import android.os.Handler
import android.os.Looper


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    var find_lat = 37.5665
    var find_lon = 126.9780

    lateinit var startBtn: ImageButton
    lateinit var stopBtn: ImageButton
    lateinit var resetBtn: ImageButton

    var running:Boolean = false
    var pauseTime = 0L //멈춘시간
    var totalDistance: Float = 0f

    private lateinit var distanceEditText: TextView
    private lateinit var stepEditText: TextView
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 200
    private lateinit var fitnessOptions: FitnessOptions

    private var stepCountAtStart = 0
    private var currentStepCount = 0

    private val handler = Handler(Looper.getMainLooper())
    private val updateStepCountRunnable = object : Runnable {
        override fun run() {
            updateStepCount() // 발걸음 수 업데이트 함수 호출
            handler.postDelayed(this, 3000) // 3초마다 실행
        }
    }
    private inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            if (mMap != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                //mMap!!.addMarker(MarkerOptions().position(latLng).title("Current Location")

                // 이동 경로 그리기
                if (running) {
                    drawPolyline(latLng)
                    updateDistance(latLng)
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

        val chronometer: Chronometer = findViewById(R.id.chronometer)

        //페이지 이동 버튼
        val homeButton = findViewById<ImageButton>(R.id.homeButton)
        homeButton.setOnClickListener {
            // 클릭 이벤트에 대한 동작을 여기에 구현합니다.
            // 예: 버튼이 클릭되었을 때 수행할 작업
            Toast.makeText(this, "You are already in main page", Toast.LENGTH_SHORT).show()
        }


        val petButton = findViewById<ImageButton>(R.id.petButton)
        petButton.setOnClickListener {
            val intent = Intent(this, StorageActivity::class.java)
            startActivity(intent)
        }

        //fitnessOptions 초기화
        fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()

        stepEditText = findViewById(R.id.step_value)

        //지도
        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment
        mapFragment.getMapAsync(this)

        distanceEditText = findViewById(R.id.distance_value) //초기화

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
        //Google Fit API 인증 절차 수행
        val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                this,
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                account,
                fitnessOptions
            )
        } else {
            // 위치 업데이트 및 이동 거리 계산 로직 실행
            // 위치 업데이트를 위한 LocationManager 설정 등의 코드를 여기에 추가합니다.
        }

        // onCreate() 메서드 마지막에 다음 코드 추가
        handler.postDelayed(updateStepCountRunnable, 3000) // 3초마다 실행
        //시작, 정지, 리셋 버튼 찾기
        startBtn = findViewById(R.id.start_button)
        stopBtn = findViewById(R.id.stop_button)
        resetBtn = findViewById(R.id.reset_button)
        val endRun = findViewById<ImageButton>(R.id.end_run)


        startBtn.isEnabled = true
        stopBtn.isEnabled = false
        resetBtn.isEnabled = false
        running = false
        var pause_pressed: Boolean = false
        var reset_pressed: Boolean = false

        //시작 버튼 누르면, [
        startBtn.setOnClickListener {

            if (!running && pause_pressed) {
                //startBtn이 눌렸고 pause가 눌린적 있다면
                pause_pressed = false
                chronometer.base = SystemClock.elapsedRealtime() - pauseTime
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

            if (!running && reset_pressed)
            {
                reset_pressed=false
                //달리는 중이고 reset 버튼이 눌렸다면
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
                // 걸음 수 초기화
                val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)
                Fitness.getHistoryClient(this, account)
                    .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                    .addOnSuccessListener { dataSet ->
                        stepCountAtStart = if (dataSet.isEmpty) 0 else dataSet.dataPoints[0].getValue(
                            Field.FIELD_STEPS
                        ).asInt()
                    }
                    .addOnFailureListener { exception ->
                        Log.e("StepCountException", "Failed to retrieve step count", exception)
                    }
            }

            //정지 상태일때만 실행
            if(!running && !pause_pressed &&!reset_pressed)
            {
                //달리는 중이고 pause가 눌린적 없고 reset도 눌린적 없다면
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
                // 걸음 수 초기화
                val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)
                Fitness.getHistoryClient(this, account)
                    .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                    .addOnSuccessListener { dataSet ->
                        stepCountAtStart = if (dataSet.isEmpty) 0 else dataSet.dataPoints[0].getValue(
                            Field.FIELD_STEPS
                        ).asInt()
                    }
                    .addOnFailureListener { exception ->
                        Log.e("StepCountException", "Failed to retrieve step count", exception)
                    }

            }
            // 걸음 수 업데이트 함수 호출
            updateStepCount()
        }
        //정지 버튼 누르면,
        stopBtn.setOnClickListener {
            if(running)
            {
                chronometer.stop()
                pauseTime=SystemClock.elapsedRealtime() - chronometer.base
                startBtn.isEnabled = true
                stopBtn.isEnabled = false
                resetBtn.isEnabled = true
                running = false
                // 걸음 수 초기화하지 않고 이전 값 유지
                stepEditText.text = "${currentStepCount - stepCountAtStart} 걸음"
                pause_pressed=true
            }
        }

        //리셋 버튼 누르면,
        resetBtn.setOnClickListener {

            reset_pressed=true
            chronometer.base = SystemClock.elapsedRealtime()

            pauseTime=0L
            chronometer.stop()
            startBtn.isEnabled = true
            stopBtn.isEnabled = false
            resetBtn.isEnabled = false
            running = false

            mMap?.clear()
            previousLatLng = null
            totalDistance = 0f
            distanceEditText.text = "0.0 km"
            stepEditText.text = "0 걸음"
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

                chronometer.base = SystemClock.elapsedRealtime()
            }
            else if(!running)
            {
                chronometer.base = SystemClock.elapsedRealtime()
            }
            // 이동 경로 캡쳐
            val pauseMinute = (pauseTime / (1000 * 60)).toInt()
            val pauseSecond = (pauseTime / 1000).toInt()

            mMap?.snapshot { snapshot ->
                val imageBitmap = snapshot ?: return@snapshot

                val imageFile = saveBitmapToFile(imageBitmap)
                // EndRunActivity로 화면 전환
                val intent = Intent(this, EndRunActivity::class.java)
                intent.putExtra(EndRunActivity.EXTRA_IMAGE_FILE_PATH, imageFile.absolutePath)
                intent.putExtra(EndRunActivity.EXTRA_PAUSE_TIME_MINUTES, pauseMinute)
                intent.putExtra(EndRunActivity.EXTRA_PAUSE_TIME_SECONDS, pauseSecond)
                intent.putExtra(EndRunActivity.EXTRA_DISTANCE, totalDistance)
                Log.d("메인 액티비티에서!!!!!", totalDistance.toString())
                intent.putExtra(EndRunActivity.EXTRA_STEP_COUNT, currentStepCount - stepCountAtStart)
                startActivity(intent)
                pauseTime=0L
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
            val distance = calculateDistance(previousLatLng!!, latLng)
            totalDistance += distance
            val polylineOptions = PolylineOptions()
                .color(Color.GREEN)
                .width(5f)
                .add(previousLatLng)
                .add(latLng)
            mMap?.addPolyline(polylineOptions)
        }
        previousLatLng = latLng
    }

    private fun calculateDistance(latLng1: LatLng, latLng2: LatLng): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            latLng1.latitude, latLng1.longitude,
            latLng2.latitude, latLng2.longitude,
            results
        )
        return results[0]
    }
    private fun updateDistance(latLng: LatLng) {
        if (previousLatLng != null) {
            val distance = calculateDistance(previousLatLng!!, latLng)
            totalDistance += distance

            val totalDistanceInKm = totalDistance / 1000f
            val formattedDistance = String.format("%.2f", totalDistanceInKm)
            distanceEditText.text = "$formattedDistance km"
        }
        previousLatLng = latLng
    }

    private fun updateStepCount() {
        if (running) {
            val account = GoogleSignIn.getAccountForExtension(this, fitnessOptions)
            Fitness.getHistoryClient(this, account)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener { dataSet ->
                    currentStepCount = if (dataSet.isEmpty) 0 else dataSet.dataPoints[0].getValue(
                        Field.FIELD_STEPS
                    ).asInt()

                    val stepCount = currentStepCount - stepCountAtStart
                    stepEditText.text = "$stepCount 걸음"
                }
                .addOnFailureListener { exception ->
                    Log.e("StepCountException", "Failed to retrieve step count", exception)
                }
        }
    }

}
