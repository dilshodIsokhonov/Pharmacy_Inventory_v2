package com.example.pharmacyinventory.ui

import android.content.Context
import android.widget.FrameLayout
import android.widget.TextView
import com.example.pharmacyinventory.R

class SectionViewHolder(
    context: Context
): FrameLayout(context) {

    private lateinit var tvDate: TextView

    init {
        inflate(context, R.layout.item_section, this)

        findView()
    }

    private fun findView() {
        tvDate = findViewById(R.id.textViewDate)
    }

    fun setDate(dateString: String) {
        tvDate.text = dateString
    }
}