package com.example.catsworkmanagerapi

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startForegroundService
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.catsworkmanagerapi.MainActivity.Companion.INPUT_VALUE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import kotlin.random.Random

class DownloadWorker(private val context: Context, private val workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    object RetrofitHelper {

        val baseUrl = "https://cat-fact.herokuapp.com"

        fun getInstance(): Retrofit {
            return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    companion object{
        const val OUTPUT_DATA = "output_data"
    }
    override suspend fun doWork(): Result {
        try {
            val temp = inputData.getString(INPUT_VALUE)
            Log.d("temp", temp.toString())
            val api = DownloadWorker.RetrofitHelper.getInstance().create(ApiRequests::class.java)
            val result = api.getCatFacts()
            Log.d("MainActivity", result.body().toString())
            val data = result.body()!!
            val sendData = data.text
            val outputData = Data.Builder()
                .putString(OUTPUT_DATA, sendData.toString())
                .build()

            return Result.success(outputData)
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}


