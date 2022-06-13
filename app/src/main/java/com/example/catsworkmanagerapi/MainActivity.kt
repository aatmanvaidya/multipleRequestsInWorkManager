package com.example.catsworkmanagerapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    companion object { //helps make static
        const val INPUT_VALUE = "input_value"
        const val INPUT_VALUE_2 = "input_value_2"
        const val INPUT_VALUE_3 = "input_value_3"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            setOneTimeWorkRequest()
        }
    }

    private fun setOneTimeWorkRequest() {
        val text: TextView = findViewById(R.id.text)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val text2: TextView = findViewById(R.id.text2)
        val progressBar2 = findViewById<ProgressBar>(R.id.progressBar2)
        val text3: TextView = findViewById(R.id.text3)
        val progressBar3 = findViewById<ProgressBar>(R.id.progressBar3)

        val workManager = WorkManager.getInstance(applicationContext)
        val data: Data = Data.Builder()
            .putString(INPUT_VALUE, "Hello")
            .build()
        val data2: Data = Data.Builder()
            .putString(INPUT_VALUE_2, "Hii")
            .build()
        val data3: Data = Data.Builder()
            .putString(INPUT_VALUE_3, "BRUH")
            .build()

        val getData = OneTimeWorkRequest.Builder(DownloadWorker::class.java)
            .setInputData(data)
            .addTag("getData")
            .build()
        val getData2 = OneTimeWorkRequest.Builder(DownloadWorker2::class.java)
            .setInputData(data2)
            .addTag("getData2")
            .build()
        val getData3 = OneTimeWorkRequest.Builder(DownloadWorker3::class.java)
            .setInputData(data3)
            .addTag("getData3")
            .build()

        val parallelWorks = mutableListOf<OneTimeWorkRequest>()
            parallelWorks.add(getData2)
            parallelWorks.add(getData3)

        workManager
            .beginWith(getData)
            .then(parallelWorks)
            .enqueue()

        progressBar.visibility = View.VISIBLE
        text.visibility = View.GONE
//        progressBar2.visibility = View.VISIBLE
//        text2.visibility = View.GONE

        workManager.getWorkInfoByIdLiveData(getData.id)
            .observe(this, Observer {
                if (it.state.isFinished) {
                    val receivedData = it.outputData
                    val message = receivedData.getString(DownloadWorker.OUTPUT_DATA)
//                    val modMessage: JSONObject = JSONObject(message).getJSONObject("text")
                    Log.d("out", message.toString())
                    text.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    text.text = message
                    val runningState = it.state
                    Log.d("state", runningState.toString())
                    if(runningState == WorkInfo.State.SUCCEEDED){
                        progressBar2.visibility = View.VISIBLE
                        text2.visibility = View.GONE
                        progressBar3.visibility = View.VISIBLE
                        text3.visibility = View.GONE
                    }
                }
            })

        workManager.getWorkInfoByIdLiveData(getData2.id)
            .observe(this, Observer {
                if (it.state.isFinished) {
                    val receivedData2 = it.outputData
                    val message2 = receivedData2.getString(DownloadWorker2.OUTPUT_DATA_2)
                    Log.d("out2", message2.toString())
                    text2.visibility = View.VISIBLE
                    progressBar2.visibility = View.GONE
                    text2.text = message2
                    val runningState2 = it.state
//                    if (runningState2 == WorkInfo.State.SUCCEEDED){
//                        progressBar3.visibility = View.VISIBLE
//                        text3.visibility = View.GONE
//                    }
                }
            })

        workManager.getWorkInfoByIdLiveData(getData3.id)
            .observe(this, Observer {
                if(it.state.isFinished){
                    val receivedData3 = it.outputData
                    val message3 = receivedData3.getString(DownloadWorker3.OUTPUT_DATA_3)
                    Log.d("out3", message3.toString())
                    text3.visibility = View.VISIBLE
                    progressBar3.visibility = View.GONE
                    text3.text = message3
                }
            })

    }
}