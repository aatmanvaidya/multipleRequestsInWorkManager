package com.example.catsworkmanagerapi

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DownloadWorker2(private val context: Context, private val workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams){
    object RetrofitHelper {

        val baseUrl = "https://cat-fact.herokuapp.com"

        fun getInstance(): Retrofit {
            return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    companion object{
        const val OUTPUT_DATA_2 = "output_data_2"
    }
    override suspend fun doWork(): Result {
        try {
            val temp2 = inputData.getString(MainActivity.INPUT_VALUE_2)
            Log.d("temp2", temp2.toString())
            val api = DownloadWorker2.RetrofitHelper.getInstance().create(ApiRequests::class.java)
            val result = api.getCatFacts()
            Log.d("MainActivity2", result.body().toString())
            val data = result.body()!!
            val sendData2 = data.text
            Log.d("sendData2", sendData2)
            val outputData = Data.Builder()
                .putString(OUTPUT_DATA_2, sendData2.toString())
                .build()

            return Result.success(outputData)
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}