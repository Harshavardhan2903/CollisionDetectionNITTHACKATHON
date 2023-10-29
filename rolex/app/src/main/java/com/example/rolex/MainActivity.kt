package com.example.rolex

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.widget.TextView
import retrofit2.http.GET



class Audioplayer : Service() {

    private lateinit var player: MediaPlayer
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI)
        player.start()
        Log.d("log","music started")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        player.stop()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}


class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var editTextLatitude: EditText
    private val handler = Handler(Looper.getMainLooper())
    val ngrok = "https://2d7a-203-129-195-130.ngrok-free.app"
    var latlat:String = "l"
    var longlong:String = "k"

    data class FlaskData(val bu1: String ,val bu2:String)

    interface FlaskApi {
        @GET("/")
        fun getDataFromFlask(): Call<FlaskData>
    }


/*
    fun checkclose(){


        //val BASE_URL = "https://35c7-14-139-162-2.ngrok-free.app"
        val retrofit = Retrofit.Builder()
                .baseUrl(ngrok)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val api = retrofit.create(FlaskApi::class.java)


        api.getDataFromFlask().enqueue(object : Callback<FlaskData> {
            override fun onResponse(call: Call<FlaskData>, response: Response<FlaskData>) {
                if (response.isSuccessful) {
                    val flaskData = response.body()
                    Log.d("wHY ",flaskData.toString())
                    // Handle the data as needed
                    //val message = flaskData?.name ?: ""
                    //Log.d("SUCCESS",message)
                    Toast.makeText(applicationContext, "Message: htra", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("error",response.errorBody().toString())
                    Log.d("error",response.message().toString())
                    // Handle the response if it's not successful
                    Toast.makeText(applicationContext, "Request failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FlaskData>, t: Throwable) {
                // Handle the failure to make the request

                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()
            }
        })

    }
    */


    fun locationfind() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestpermisson()
            return
        }
        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                    CancellationTokenSource().token

                override fun isCancellationRequested() = false
            }).addOnSuccessListener { location: Location? ->
            if (location == null)
                Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
            else {
                val lat = location.latitude
                val lon = location.longitude
                latlat = lat.toString()
                longlong = lon.toString()
                val la = findViewById<EditText>(R.id.editTextText3)
                val lo  =findViewById<EditText>(R.id.editTextText4)

                la.setText("latitude is $lat")
                lo.setText("longitude is $lon")

            }

        }

    }

    fun requestpermisson() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),100)
    }

    data class RequestData(
        val userid: String,
        val latitude: String,
        val longitude: String
    )

    interface MyApi {
        @POST("https://2d7a-203-129-195-130.ngrok-free.app")
        fun sendData(@Body requestData: RequestData): Call<Void>
    }

    var y: String = " godji"


    fun sendData() {

        val editText = findViewById<EditText>(R.id.editTextText3)
        val textToSend = editText.text.toString()

        val retrofit = Retrofit.Builder()
            .baseUrl(ngrok)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(MyApi::class.java)

        val requestData = RequestData(userid = y, latitude = latlat, longitude = longlong)
        val call = api.sendData(requestData)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Data sent successfully
                    Toast.makeText(applicationContext, "Successfully sent dat", Toast.LENGTH_SHORT).show()
                } else {
                    // Handle error
                    Toast.makeText(applicationContext, "Failure", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle failure
                Toast.makeText(applicationContext, "Big failure", Toast.LENGTH_SHORT).show()
            }
        })
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //intent catching
        val intent = intent

        val user_id: String? = intent.getStringExtra("data")
        if (user_id != null) {
            y = user_id
            val z = findViewById<TextView>(R.id.textView5)
            z.text = y
        }

        val textviewlatitude = findViewById<EditText>(R.id.editTextText3)
        val but = findViewById<Button>(R.id.button)


        val backgroundThread = Thread {
            while (!Thread.currentThread().isInterrupted) {
                // Perform some background task here

                // Post a Runnable to the main thread's message queue
                handler.post {
                    but.performClick()
                }

                // Sleep for 3 seconds
                Thread.sleep(3000)
            }
        }
        backgroundThread.start()


        locationfind()
        but.setOnClickListener {
            locationfind()
            sendData()
            //checkclose()

            //startService(Intent(applicationContext, Audioplayer::class.java))
        }

    }

}




