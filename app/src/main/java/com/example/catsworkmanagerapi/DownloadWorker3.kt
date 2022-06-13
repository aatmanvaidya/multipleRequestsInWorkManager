package com.example.catsworkmanagerapi

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DownloadWorker3(private val context: Context, private val workerParams: WorkerParameters) :
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
        const val OUTPUT_DATA_3 = "output_data_3"
    }

    override suspend fun doWork(): Result {
        try {
            val temp3 = inputData.getString(MainActivity.INPUT_VALUE_3)
            Log.d("temp3", temp3.toString())
            val api = DownloadWorker3.RetrofitHelper.getInstance().create(ApiRequests::class.java)
            val result = api.getCatFacts()
            Log.d("MainActivity3", result.body().toString())
            val data = result.body()!!
            val sendData3 = data.text
            Log.d("sendData3", sendData3)
            val outputData = Data.Builder()
                .putString(DownloadWorker3.OUTPUT_DATA_3, sendData3.toString())
                .build()

            return Result.success(outputData)
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}