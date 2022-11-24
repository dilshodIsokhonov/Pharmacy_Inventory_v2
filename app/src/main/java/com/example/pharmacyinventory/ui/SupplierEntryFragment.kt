package com.example.pharmacyinventory.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pharmacyinventory.BaseApplication
import com.example.pharmacyinventory.R
import com.example.pharmacyinventory.adapters.SupplierEntryAdapter
import com.example.pharmacyinventory.databinding.FragmentSupplierEntryBinding
import com.example.pharmacyinventory.viewmodel.EntryViewModel
import com.example.pharmacyinventory.viewmodel.EntryViewModelFactory
import java.text.DecimalFormat

class SupplierEntryFragment : Fragment() {

    private val navigationArgs: SupplierEntryFragmentArgs by navArgs()

    private val viewModel: EntryViewModel by activityViewModels {
        EntryViewModelFactory(
            (activity?.application as BaseApplication).repository
        )
    }
    private var _binding: FragmentSupplierEntryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSupplierEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = navigationArgs.name
        val adapter = SupplierEntryAdapter { entry ->
            val action = SupplierEntryFragmentDirections
                .actionNavSupplierEntryToNavAddEntry(entry.id)
            findNavController().navigate(action)
        }
        viewModel.getSupplierEntries(name).observe(this.viewLifecycleOwner) { entry ->
            entry.let {
                adapter.submitList(it)
            }
        }
        binding.apply {
            supplierName.text = name
            totalRemainder.text = getString(R.string.total_remainder, getTotalRemainder(name))
            recyclerView.adapter = adapter
        }
    }

    private fun getTotalRemainder(name: String) {
        viewModel.getPaidSum(name).observe(this.viewLifecycleOwner) { paidSum ->
            viewModel.getReceivedSum(name).observe(this.viewLifecycleOwner) { receivedSum ->
                val price = receivedSum.minus(paidSum)
                binding.totalRemainder.text =
                    getString(R.string.total_remainder, DecimalFormat("#,###.##").format(price))
            }
        }
    }
}
