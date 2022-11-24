package com.example.pharmacyinventory.database

import androidx.annotation.WorkerThread
import com.example.pharmacyinventory.model.Entry
import kotlinx.coroutines.flow.Flow

class EntryRepository(private val entryDao: EntryDao) {

    val getFullEntries: Flow<List<Entry>> = entryDao.getFullEntries()

    val getSupplierNames: Flow<List<String>> = entryDao.getSupplierNames()
    val getSuppliersByName: Flow<List<Entry>> = entryDao.getSuppliersByName()

    fun getEntry(id: Long): Flow<Entry> = entryDao.getEntry(id)

    fun getSupplierEntries(name: String): Flow<List<Entry>> = entryDao.getSupplierEntries(name)

    fun getPaidSum(name: String): Flow<Long> = entryDao.getPaidSum(name)

    fun getReceivedSum(name: String): Flow<Long> = entryDao.getReceivedSum(name)

    val getLastPurchase: Flow<Entry> = entryDao.getLastPurchase()

    @WorkerThread
    suspend fun delete(entry: Entry) {
        entryDao.delete(entry)
    }

    @WorkerThread
    suspend fun insertEntry(entry: Entry) {
        entryDao.insert(entry)
    }

    @WorkerThread
    suspend fun updateEntry(entry: Entry) {
        entryDao.update(entry)
    }
}