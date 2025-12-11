package com.example.jewelryshopapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CalculationDao {

    @Query("SELECT * FROM calculations ORDER BY date DESC")
    suspend fun getAllCalculations(): List<Calculation>

    @Insert
    suspend fun insertCalculation(calculation: Calculation)
}
