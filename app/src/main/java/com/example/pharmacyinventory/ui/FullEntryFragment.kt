package com.example.pharmacyinventory.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.pharmacyinventory.BaseApplication
import com.example.pharmacyinventory.R
import com.example.pharmacyinventory.adapters.FullEntryAdapter
import com.example.pharmacyinventory.adapters.ItemSectionDecoration
import com.example.pharmacyinventory.databinding.FragmentFullEntryBinding
import com.example.pharmacyinventory.viewmodel.EntryViewModel
import com.example.pharmacyinventory.viewmodel.EntryViewModelFactory

class FullEntryFragment : Fragment() {

    private lateinit var itemSectionDecoration: ItemSectionDecoration

    private val viewModel: EntryViewModel by activityViewModels {
        EntryViewModelFactory(
            (activity?.application as BaseApplication).repository
        )
    }

    private var _binding: FragmentFullEntryBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFullEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FullEntryAdapter(
            { entry ->
                val action = FullEntryFragmentDirections
                    .actionNavFullEntryToNavSupplierEntry(entry.id, entry.supplierName)
                findNavController().navigate(action)
            },
            { entry ->
                val action = FullEntryFragmentDirections
                    .actionNavFullEntryToNavAddEntry(entry.id)
                findNavController().navigate(action)
            }
        )
        viewModel.getFullEntries.observe(this.viewLifecycleOwner) { entry ->
            entry.let {
                adapter.submitList(it)
            }
        }
        itemSectionDecoration = ItemSectionDecoration(requireContext()) {
            adapter.currentList
        }
        binding.apply {
            recyclerView.addItemDecoration(itemSectionDecoration)
            recyclerView.adapter = adapter
            recyclerView.setOnScrollChangeListener { _, i, i2, _, i4 ->
                when {
                    i > i4 -> extendedFab.shrink()
                    i == i2 -> extendedFab.extend()
                    else -> extendedFab.extend()
                }
            }
            extendedFab.setOnClickListener {
                findNavController().navigate(
                    R.id.action_nav_full_entry_to_nav_add_entry
                )
            }
        }
    }
}