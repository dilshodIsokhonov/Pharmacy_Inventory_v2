package com.example.pharmacyinventory.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmacyinventory.databinding.ItemSupplierEntryBinding
import com.example.pharmacyinventory.model.Entry
import com.example.pharmacyinventory.model.getFormattedPaidPrice
import com.example.pharmacyinventory.model.getFormattedReceivedPrice
import java.text.SimpleDateFormat
import java.util.*

class SupplierEntryAdapter(
    private val onLongClickListener: (Entry) -> Unit
) : ListAdapter<Entry, SupplierEntryAdapter.SupplierEntryViewHolder>(EntryDiffCallback) {

    class SupplierEntryViewHolder(
        private val binding: ItemSupplierEntryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: Entry) {
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
                SimpleDateFormat("d.MM E yyyy", Locale.getDefault()).format(
                    it
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierEntryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SupplierEntryViewHolder(
            ItemSupplierEntryBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SupplierEntryViewHolder, position: Int) {
        val entry = getItem(position)
        holder.itemView.setOnLongClickListener {
            onLongClickListener(entry)
            return@setOnLongClickListener true
        }
        holder.bind(entry)
    }
}