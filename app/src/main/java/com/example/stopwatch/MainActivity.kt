package com.example.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stopwatch.adapter.LapAdapter
import com.example.stopwatch.data.LapTime
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var isRunning = false
    private var startTime = 0L
    private var pauseTime = 0L
    private lateinit var textView : TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var lapAdapter: LapAdapter
    private var lastLapTime = 0L
    private var lapCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        recyclerView = findViewById(R.id.recyclerView)
        lapAdapter = LapAdapter()
        recyclerView.adapter = lapAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val buttonStart = findViewById<Button>(R.id.buttonStart)
        val buttonReset = findViewById<Button>(R.id.buttonReset)
        val buttonLap = findViewById<Button>(R.id.buttonLap)



        buttonStart.setOnClickListener {
            if (!isRunning) {
                isRunning = true
                startTime = SystemClock.elapsedRealtime() - pauseTime
                handler.post(updateTime)

                buttonStart.text = "Pause"
            } else {
                isRunning = false
                pauseTime = SystemClock.elapsedRealtime() - startTime
                handler.removeCallbacks(updateTime)

                buttonStart.text = "Start"
            }
        }

        buttonReset.setOnClickListener {
            isRunning = false
            handler.removeCallbacks(updateTime)
            textView.text = "0.00"
            startTime = 0L
            pauseTime = 0L
            lastLapTime = 0L
            lapCount = 0
            lapAdapter = LapAdapter()
            recyclerView.adapter = lapAdapter
            buttonStart.text = "Start"
        }

        buttonLap.setOnClickListener {
            if (isRunning) {
                val currentTime = SystemClock.elapsedRealtime() - startTime
                val lapTime = currentTime - lastLapTime
                lastLapTime = currentTime
                lapCount++

                val lap = LapTime(
                    lapNumber = lapCount,
                    lapTime = formatTime(currentTime)
                )
                lapAdapter.addLap(lap)

                recyclerView.smoothScrollToPosition(0)
            }
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private val updateTime = object : Runnable {
        override fun run() {
            if (isRunning) {
                val currentTime = SystemClock.elapsedRealtime() - startTime
                textView.text = String.format(Locale.ROOT, "%.2f", currentTime / 1000.0)
                handler.postDelayed(this, 10) // 0.01초마다 업데이트
            }
        }
    }

    private fun formatTime(timeInMillis: Long): String {
        val minutes = timeInMillis / 60000
        val seconds = (timeInMillis % 60000) / 1000
        val millis = timeInMillis % 1000
        return String.format(Locale.ROOT, "%02d:%02d.%02d", minutes, seconds, millis / 10)
    }
}