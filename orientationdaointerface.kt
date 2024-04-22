package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface orientationdaointerface {
    @Insert
    suspend fun insert(orientationData: orientationdataentity)

    @Query("SELECT * FROM orientation_data")
    fun getAllData(): List<orientationdataentity>
}