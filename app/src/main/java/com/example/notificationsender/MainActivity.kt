package com.example.notificationsender

import androidx.activity.ComponentActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.work.*
import java.util.concurrent.TimeUnit
import com.example.notificationsender.R

class MainActivity : ComponentActivity() {

    private var workId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSchedule = findViewById<Button>(R.id.buttonSchedule)
        val buttonCancel = findViewById<Button>(R.id.buttonCancel)
        val etText = findViewById<EditText>(R.id.etText)
        val etDelay = findViewById<EditText>(R.id.etDelay)

        buttonSchedule.setOnClickListener {
            val text = etText.text.toString()
            val delay = etDelay.text.toString().toLongOrNull() ?: 1
            scheduleWork(text, delay)
        }

        buttonCancel.setOnClickListener {
            cancelWork()
        }
    }

    private fun scheduleWork(text: String, delay: Long) {
        val input = workDataOf("text" to text)

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(input)
            .setInitialDelay(delay, TimeUnit.MINUTES)
            .build()

        workId = request.id.toString()
        WorkManager.getInstance(this).enqueue(request)
    }

    private fun cancelWork() {
        workId?.let {
            WorkManager.getInstance(this)
                .cancelWorkById(java.util.UUID.fromString(it))
        }
    }
}