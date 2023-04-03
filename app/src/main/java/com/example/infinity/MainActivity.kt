package com.example.randompet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import java.util.*

class MainActivity : AppCompatActivity() {
    var petImageURL = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.petButton)
        val imageView = findViewById<ImageView>(R.id.petImage)
        val animalTypeTextView = findViewById<TextView>(R.id.petType)

        getNextImage(button, imageView, animalTypeTextView)
    }

    private fun getNextImage(button: Button, imageView: ImageView, animalTypeTextView: TextView) {
        button.setOnClickListener {
            val choice = Random().nextInt(3)

            when (choice) {
                0 -> getCatImageURL(animalTypeTextView)
                1 -> getNasaAPOD(animalTypeTextView)

            }

            Glide.with(this)
                .load(petImageURL)
                .fitCenter()
                .into(imageView)
        }
    }

    private fun getCatImageURL(animalTypeTextView: TextView) {
        val client = AsyncHttpClient()
        client["https://api.thecatapi.com/v1/images/search", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Cat", "response successful$json")
                val resultsJSON = json.jsonArray.getJSONObject(0)
                petImageURL = resultsJSON.getString("url")
                animalTypeTextView.text = "Cat"
                Log.d("petImageURL", "pet image URL set")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Cat Error", errorResponse)
            }
        }]
    }

    private fun getNasaAPOD(animalTypeTextView: TextView) {
        val client = AsyncHttpClient()
        client["https://api.nasa.gov/planetary/apod?api_key=qivKyztKg9cEI2vZZYua5Tw77dJgFAfAXksiOPqA", object :
            JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JsonHttpResponseHandler.JSON
            ) {
                Log.d("NASA", "response successful$json")
                petImageURL = json.jsonObject.getString("url")
                animalTypeTextView.text = "NASA APOD"
                Log.d("petImageURL", "pet image URL set")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("NASA Error", errorResponse)
            }
        }]
    }


}