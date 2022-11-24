package com.example.pharmacyinventory.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.pharmacyinventory.BaseApplication
import com.example.pharmacyinventory.viewmodel.EntryViewModel
import com.example.pharmacyinventory.viewmodel.EntryViewModelFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DatePickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val viewModel: EntryViewModel by activityViewModels {
        EntryViewModelFactory(
            (activity?.application as BaseApplication).repository
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        try {
            val calendar = Calendar.getInstance()
            calendar.set(year,month,day)
            val simpleDateFormat = SimpleDateFormat("d MMM yyyy E", Locale.getDefault())
            val date = simpleDateFormat.format(calendar.time)
            viewModel._date.value = date
        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }
}