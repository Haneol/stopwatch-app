package com.example.stopwatch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stopwatch.R
import com.example.stopwatch.data.LapTime

class LapAdapter : RecyclerView.Adapter<LapAdapter.LapViewHolder>() {
    private val lapTimes = mutableListOf<LapTime>()

    class LapViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val lapNumber: TextView = view.findViewById(R.id.lapNumber)
        private val totalTime: TextView = view.findViewById(R.id.lapTime)

        fun bind(lap: LapTime) {
            lapNumber.text = "Lap ${lap.lapNumber}"
            totalTime.text = lap.lapTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lap_item, parent, false)
        return LapViewHolder(view)
    }

    override fun onBindViewHolder(holder: LapViewHolder, position: Int) {
        holder.bind(lapTimes[position])
    }

    override fun getItemCount() = lapTimes.size

    fun addLap(lap: LapTime) {
        lapTimes.add(0, lap)  // 최신 기록을 위에 추가
        notifyItemInserted(0)
    }
}