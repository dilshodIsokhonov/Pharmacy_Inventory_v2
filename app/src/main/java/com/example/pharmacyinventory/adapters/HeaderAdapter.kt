package com.example.pharmacyinventory.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmacyinventory.R
import java.text.SimpleDateFormat
import java.util.*

class HeaderAdapter: RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>(){
    private var date: Long = 0

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateTextView: TextView = itemView
            .findViewById(R.id.date)

        fun bind(date: Long) {
            dateTextView.text = date.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.header, parent, false)
        return HeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(date)
    }

    //TODO need to make dynamic
    override fun getItemCount(): Int {
        return 2
    }

    fun applyDate() {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        options.add(formatter.format(calendar.time))
        calendar.add(Calendar.DATE, 1)
    }


}