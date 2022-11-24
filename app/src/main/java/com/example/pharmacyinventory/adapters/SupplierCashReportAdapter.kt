package com.example.pharmacyinventory.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmacyinventory.databinding.ItemSupplierCashReportBinding
import com.example.pharmacyinventory.model.Entry

class SupplierCashReportAdapter(
    private val clickListener: (Entry) -> Unit
) : ListAdapter<Entry, SupplierCashReportAdapter.SupplierCashReportViewHolder>(EntryDiffCallback) {


//    interface Observable {
//        fun getTotalRemainder(name: String): String
//        fun lastPurchase(): Entry?
//        val supplierCashReportFragment: SupplierCashReportFragment
//    }

    class SupplierCashReportViewHolder(
        private val binding: ItemSupplierCashReportBinding
    ) : RecyclerView.ViewHolder(binding.root) {


//                private val supplierCashReportFragment = SupplierCashReportFragment()
//        private val viewModel: EntryViewModel by lazy {
//            ViewModelProvider.AndroidViewModelFactory(Application()).create(EntryViewModel::class.java)
//        }
        @SuppressLint("SetTextI18n")
        fun bind(entry: Entry) {
//            val fragment = SupplierCashReportFragment()
//            val paidSum = viewModel.getPaidSum(entry.supplierName).value
//            val receivedSum = viewModel.getReceivedSum(entry.supplierName).value
            binding.supplierName.text = entry.supplierName


            binding.totalRemainder.visibility = View.GONE
            binding.lastPurchase.visibility = View.GONE


//            binding.totalRemainder.text = receivedSum?.minus(paidSum!!).toString()
//            binding.lastPurchase.text = viewModel.getLastPurchase.value.toString()

//            binding.totalRemainder.text =
//                supplierCashReportFragment.getTotalRemainder(entry.supplierName).toString()
//            binding.lastPurchase.text =
//                supplierCashReportFragment.lastPurchase()?.paid.toString() + supplierCashReportFragment.lastPurchase()?.date.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SupplierCashReportViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SupplierCashReportViewHolder(
            ItemSupplierCashReportBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SupplierCashReportViewHolder, position: Int) {
        val entry = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener(entry)
        }
        holder.bind(entry)
    }
}