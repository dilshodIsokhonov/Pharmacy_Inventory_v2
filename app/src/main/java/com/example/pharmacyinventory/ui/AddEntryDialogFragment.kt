package com.example.pharmacyinventory.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.pharmacyinventory.BaseApplication
import com.example.pharmacyinventory.R
import com.example.pharmacyinventory.databinding.FragmentAddEntryDialogBinding
import com.example.pharmacyinventory.model.Entry
import com.example.pharmacyinventory.viewmodel.EntryViewModel
import com.example.pharmacyinventory.viewmodel.EntryViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddEntryDialogFragment : BottomSheetDialogFragment() {

    private val navigationArgs: AddEntryDialogFragmentArgs by navArgs()

    private val viewModel: EntryViewModel by activityViewModels {
        EntryViewModelFactory(
            (activity?.application as BaseApplication).repository
        )
    }
    private lateinit var entry: Entry
    private var _binding: FragmentAddEntryDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddEntryDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val id = navigationArgs.id
        if (id > 0) {
            viewModel.getEntry(id).observe(this.viewLifecycleOwner) {
                entry = it
                bindEntry(entry)
            }
            binding.deleteButton.visibility = View.VISIBLE
            binding.deleteButton.setOnClickListener {
                deleteEntry(entry)
            }
        } else {
//            val calendar = Calendar.getInstance()
//            binding.dateText.text = SimpleDateFormat("d MMM yyyy E", Locale.getDefault()).format(calendar.time)
            binding.saveButton.setOnClickListener {
                dismiss()
                insertEntry()
            }
        }

        viewModel.getSupplierNames.observe(this.viewLifecycleOwner) {
            val autoCompleteTextView = binding.supplierNameInput
            autoCompleteTextView.dropDownVerticalOffset = 0
            autoCompleteTextView.dropDownWidth = 700
            autoCompleteTextView.dropDownHorizontalOffset = 800
            val dropDownAdapter = ArrayAdapter(requireContext(), R.layout.list_item, it)
            dropDownAdapter.filter.filter(autoCompleteTextView.text.toString())
            (autoCompleteTextView as? AutoCompleteTextView)?.setAdapter(dropDownAdapter)
        }
        showDatePickerDialog()
        viewModel.date.observe(this.viewLifecycleOwner) {
            binding.dateText.text = it
        }
    }

    private fun showDatePickerDialog() {
        binding.editDate.setOnClickListener {
            val newFragment = DatePickerDialogFragment()
            newFragment.show(childFragmentManager, "datePicker")
        }
    }

    private fun deleteEntry(entry: Entry) {
        dismiss()
        viewModel.delete(entry)
    }

    private fun insertEntry() {
        if (isValidEntry()) {
            val dateTextView = binding.dateText.text.toString()
            viewModel.insert(
                name = binding.supplierNameInput.text.toString(),
                received = if (binding.receivedInput.text!!.trim()
                        .isNotEmpty()
                ) binding.receivedInput.text.toString().toLong() else 0,
                paid = if (binding.paidInput.text!!.trim()
                        .isNotEmpty()
                ) binding.paidInput.text.toString().toLong() else 0,
                date = convertDateFormat(dateTextView)
            )

        }
    }

    private fun updateEntry() {
        if (isValidEntry()) {
            val dateTextView = binding.dateText.text.toString()
            viewModel.update(
                id = navigationArgs.id,
                name = binding.supplierNameInput.text.toString(),
                received = if (binding.receivedInput.text!!.trim()
                        .isNotEmpty()
                ) binding.receivedInput.text.toString().toLong() else 0,
                paid = if (binding.paidInput.text!!.trim()
                        .isNotEmpty()
                ) binding.paidInput.text.toString().toLong() else 0,
                date = convertDateFormat(dateTextView)
            )
        }
    }

    private fun bindEntry(entry: Entry?) {
        binding.apply {
            supplierNameInput.setText(entry!!.supplierName, TextView.BufferType.SPANNABLE)
            paidInput.setText(entry.paid.toString(), TextView.BufferType.SPANNABLE)
            receivedInput.setText(entry.received.toString(), TextView.BufferType.SPANNABLE)
            dateText.setText(entry.date?.let {
                SimpleDateFormat("d MMM yyyy E", Locale.getDefault()).format(it)
            }, TextView.BufferType.SPANNABLE)
            saveButton.setOnClickListener {
                dismiss()
                updateEntry()
            }
        }
    }

    private fun convertDateFormat(data: String): Date? {
        try {
            val simpleDateFormat = SimpleDateFormat("d MMM yyyy E", Locale.getDefault())
            val date: Date? = simpleDateFormat.parse(data)
            Log.d("TAG", "test==>$date")
            return date
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    private fun isValidEntry() = viewModel.isValidEntry(
        binding.supplierNameInput.text.toString(),
        binding.paidInput.text.toString(),
        binding.receivedInput.text.toString()
    )

    override fun onDestroyView() {
        super.onDestroyView()
        val calendar = Calendar.getInstance()
        val date = SimpleDateFormat("d MMM yyyy E", Locale.getDefault()).format(calendar.time)
        viewModel._date.value = date
        _binding = null
    }
}