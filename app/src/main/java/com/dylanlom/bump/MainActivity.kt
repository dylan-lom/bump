package com.dylanlom.bump

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import java.lang.Exception

const val CHANNEL_ID = "1"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Notifier.createNotificationChannel(applicationContext)
    }

    // TODO: Store + retrieve defaults from resources
    fun buttonOnClick(vw: View) {
        val description = findViewById<TextView>(R.id.editTextDescription)?.text?.toString() ?: "Take out the bins"
        val timeOption = findViewById<Spinner>(R.id.spinnerTime)?.selectedItemId?.toInt() ?: 0
        val timeView = findViewById<TextView>(R.id.editTextTime)
        var time: Long = 15
        // TODO: Handle empty/null input
        time = timeView.text.toString().toLong()

        val secondsUntilTime: Long =
            when (timeOption) {
                0 -> time * 60           // Minutes
                1 -> time * 60 * 60      // Hours
                2 -> time * 60 * 60 * 24 // Days
                else -> 0                // Ruh roh shaggy
            }

        Log.d("MainActivity", "time: $time | secondsUntilTime: $secondsUntilTime")
        Notifier.createNotification(applicationContext, description, secondsUntilTime)
        // TODO: Feedback when created
    }
}