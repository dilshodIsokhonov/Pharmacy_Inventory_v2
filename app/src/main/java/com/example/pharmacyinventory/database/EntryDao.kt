package com.example.pharmacyinventory.database

import androidx.room.*
import com.example.pharmacyinventory.model.Entry
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {

    @Query("SELECT * FROM entry_table WHERE id IN (:id)")
    fun getEntry(id: Long): Flow<Entry>

    @Query("SELECT DISTINCT name FROM entry_table")
    fun getSupplierNames(): Flow<List<String>>

    @Query("SELECT * FROM entry_table GROUP BY name ORDER BY name LIMIT 1")
    fun getSuppliersByName(): Flow<List<Entry>>

    @Query("SELECT * FROM entry_table ORDER BY date DESC")
    fun getFullEntries(): Flow<List<Entry>>

    @Query("SELECT * FROM entry_table WHERE name IN (:name) ORDER BY date DESC")
    fun getSupplierEntries(name: String): Flow<List<Entry>>

    @Query("SELECT SUM(paid) FROM entry_table WHERE name IN (:name)")
    fun getPaidSum(name: String): Flow<Long>

    @Query("SELECT SUM(received) FROM entry_table WHERE name IN (:name)")
    fun getReceivedSum(name: String): Flow<Long>

    @Query("SELECT * FROM entry_table ORDER BY date LIMIT 1")
    fun getLastPurchase(): Flow<Entry>

    @Query("DELETE FROM entry_table")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(vararg entry: Entry)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(vararg entry: Entry)

    @Delete
    suspend fun delete(vararg entry: Entry)

}
