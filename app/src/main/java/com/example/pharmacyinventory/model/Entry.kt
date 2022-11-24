package com.example.pharmacyinventory.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DecimalFormat
import java.util.*

@Entity(tableName = "entry_table")
data class Entry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") val supplierName: String,
    @ColumnInfo(name = "received") val received: Long,
    @ColumnInfo(name = "paid") val paid: Long,
    @ColumnInfo(name = "date") val date: Date?
)

fun Entry.getFormattedReceivedPrice(): String =
    DecimalFormat("#,###.##").format(received)

fun Entry.getFormattedPaidPrice(): String =
    DecimalFormat("#,###.##").format(paid)
