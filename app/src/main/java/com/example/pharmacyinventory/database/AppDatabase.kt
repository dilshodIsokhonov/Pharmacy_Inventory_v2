package com.example.pharmacyinventory.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.pharmacyinventory.model.Entry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Database(entities = [Entry::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun entryDao(): EntryDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.entryDao())
                }
            }
        }

        private fun convertDateFormat(data: String = "4 Jun 2022 Sat"): Date? {
            try {
                val simpleDateFormat =
                    SimpleDateFormat("d MMM yyyy E", Locale.getDefault())
                val date: Date? = simpleDateFormat.parse(data)
                Log.d("TAG", "test==>$date")
                return date
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

        suspend fun populateDatabase(entryDao: EntryDao) {
            var entry = Entry(0, "Mirena", 1200000, 500000, convertDateFormat())
            entryDao.insert(entry)
            entry = Entry(0, "Mirena", 0, 500000, convertDateFormat())
            entryDao.insert(entry)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "entry_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}