package com.example.pharmacyinventory.viewmodel

import androidx.lifecycle.*
import com.example.pharmacyinventory.database.EntryRepository
import com.example.pharmacyinventory.model.Entry
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EntryViewModel(private val repository: EntryRepository) : ViewModel() {

    fun getEntry(id: Long): LiveData<Entry> = repository.getEntry(id).asLiveData()

    val getFullEntries: LiveData<List<Entry>> = repository.getFullEntries.asLiveData()

    val getSupplierNames: LiveData<List<String>> = repository.getSupplierNames.asLiveData()
    val getSuppliersByName: LiveData<List<Entry>> = repository.getSuppliersByName.asLiveData()

    fun getSupplierEntries(name: String): LiveData<List<Entry>> = repository.getSupplierEntries(name).asLiveData()

    fun getPaidSum(name: String): LiveData<Long> = repository.getPaidSum(name).asLiveData()

    fun getReceivedSum(name: String): LiveData<Long> = repository.getReceivedSum(name).asLiveData()

    val getLastPurchase: LiveData<Entry> = repository.getLastPurchase.asLiveData()

    fun insert(
        name: String, received: Long, paid: Long, date: Date?
    ) {
        val entry = Entry(
            supplierName = name, received = received, paid = paid, date = date
        )
        viewModelScope.launch {
            repository.insertEntry(entry)
        }
    }

    fun update(
        id: Long, name: String, received: Long, paid: Long, date: Date?
    ) {
        val entry = Entry(
            id = id, supplierName = name, received = received, paid = paid, date = date
        )
        viewModelScope.launch {
            repository.updateEntry(entry)
        }
    }

    fun delete(entry: Entry) = viewModelScope.launch {
        repository.delete(entry)
    }

    fun isValidEntry(name: String, paid: String, received: String):Boolean {
        return name.isNotBlank() && paid.isNotBlank()  || received.isNotBlank()
    }

    val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

}

class EntryViewModelFactory(private val repository: EntryRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EntryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EntryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}