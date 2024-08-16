package com.importpark.office

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loadButton = findViewById<Button>(R.id.load_button)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadButton.setOnClickListener {
            lifecycleScope.launch {
                val callLogs = getCallLogs(contentResolver)
                Log.d("TAG", callLogs.toString())
                recyclerView.adapter = CallLogAdapter(callLogs)

                val apiService = getApiService()
                try {
                    withContext(Dispatchers.IO) {
                        apiService.sendCallLogs(CallLogData(callLogs))
                    }
                    Toast.makeText(this@MainActivity, "Call logs sent!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.d("TAG", e.toString())
                    Toast.makeText(this@MainActivity, "Failed to send call logs", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://import-park.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}

data class CallLogData(val logs: List<String>)

interface ApiService {
    @POST("sync_call_logs")
    suspend fun sendCallLogs(@Body callLogs: CallLogData)
}
