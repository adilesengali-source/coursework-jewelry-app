package com.example.jewelryshopapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculations")
data class Calculation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemName: String,
    val price: Double,
    val quantity: Int,
    val total: Double,
    val date: Long
)
