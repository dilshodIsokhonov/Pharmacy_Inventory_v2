package com.example.pharmacyinventory.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.pharmacyinventory.BaseApplication
import com.example.pharmacyinventory.R
import com.example.pharmacyinventory.adapters.SupplierCashReportAdapter
import com.example.pharmacyinventory.databinding.FragmentSupplierCashReportBinding
import com.example.pharmacyinventory.viewmodel.EntryViewModel
import com.example.pharmacyinventory.viewmodel.EntryViewModelFactory

class SupplierCashReportFragment : Fragment() {

    private val viewModel: EntryViewModel by activityViewModels {
        EntryViewModelFactory(
            (activity?.application as BaseApplication).repository
        )
    }
    private var _binding: FragmentSupplierCashReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSupplierCashReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SupplierCashReportAdapter { entry ->
            val action = SupplierCashReportFragmentDirections
                .actionNavSupplierCashReportToNavSupplierEntry(entry.id, entry.supplierName)
            findNavController().navigate(action)
        }
        viewModel.getSuppliersByName.observe(this.viewLifecycleOwner) { entry ->
            entry.let {
                adapter.submitList(it)
            }
        }
        binding.apply {
            recyclerView.adapter = adapter
            fab.setOnClickListener {
                findNavController().navigate(R.id.action_nav_supplier_cash_report_to_nav_cash_flow_statistics)
            }
        }
    }

//    override fun getTotalRemainder(name: String): String {
//        val receivedSum = viewModel.getReceivedSum(name).value!!
//        val paidSum = viewModel.getPaidSum(name).value!!
//        return receivedSum.minus(paidSum).toString()

//        viewModel.getPaidSum(name).observe(this.viewLifecycleOwner) { paidSum ->
//            viewModel.getReceivedSum(name).observe(this.viewLifecycleOwner) { receivedSum ->
//               receivedSum.minus(paidSum)
//
//            }
//        }
//    }

//    override fun lastPurchase(): Entry? {
//        return viewModel.getLastPurchase.value
//    }
//
//    override val supplierCashReportFragment: SupplierCashReportFragment
//        get() = this

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}