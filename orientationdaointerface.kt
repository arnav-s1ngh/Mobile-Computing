package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface orientationdaointerface {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertorientdata  (orientationData: orientationdataentity)

    @Query("SELECT * FROM orientation_data")
    fun getAllData(): List<orientationdataentity>
}
