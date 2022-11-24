package com.example.pharmacyinventory.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.pharmacyinventory.model.Entry

object EntryDiffCallback : DiffUtil.ItemCallback<Entry>() {
    override fun areItemsTheSame(oldItem: Entry, newItem: Entry): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Entry, newItem: Entry): Boolean {
        return oldItem == newItem
    }
}