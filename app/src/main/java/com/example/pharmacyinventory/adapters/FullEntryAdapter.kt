package com.example.pharmacyinventory.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmacyinventory.databinding.ItemFullEntryBinding
import com.example.pharmacyinventory.model.Entry
import com.example.pharmacyinventory.model.getFormattedPaidPrice
import com.example.pharmacyinventory.model.getFormattedReceivedPrice
import java.text.SimpleDateFormat
import java.util.*

class FullEntryAdapter(
    private val clickListener: (Entry) -> Unit, private val onLongClickListener: (Entry) -> Unit
) : ListAdapter<Entry, FullEntryAdapter.FullEntryViewHolder>(EntryDiffCallback) {

    class FullEntryViewHolder(
        private val binding: ItemFullEntryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: Entry) {
            binding.supplierName.text = entry.supplierName
            if (entry.paid > 0) {
                binding.paid.text = entry.getFormattedPaidPrice()
            } else {
                binding.paid.text = ""
            }
            if (entry.received > 0) {
                binding.received.text = entry.getFormattedReceivedPrice()
            } else {
                binding.received.text = ""
            }
            binding.date.text = entry.date?.let {
                SimpleDateFormat("d MMM yyyy E", Locale.getDefault()).format(
                    it
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullEntryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FullEntryViewHolder(
            ItemFullEntryBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FullEntryViewHolder, position: Int) {
        val entry = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener(entry)
        }
        holder.itemView.setOnLongClickListener {
            onLongClickListener(entry)
            return@setOnLongClickListener true
        }
        holder.bind(entry)
    }
}