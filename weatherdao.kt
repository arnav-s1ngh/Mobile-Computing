package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface weatherdao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertdata(weatherentity: weatherentity)
    @Query("SELECT mintemp FROM weatherdata WHERE latitude = :latitude AND longitude = :longitude AND date= :date")
    fun getmintemp(latitude: String, longitude: String, date: String): List<Double>
    @Query("SELECT maxtemp FROM weatherdata WHERE latitude = :latitude AND longitude = :longitude AND date= :date")
    fun getmaxtemp(latitude: String, longitude: String, date: String): List<Double>
    @Query("SELECT AVG(mintemp) FROM weatherdata WHERE latitude = :latitude AND longitude = :longitude")
    fun avgmintemp(latitude: String, longitude: String): List<Double>
    @Query("SELECT AVG(maxtemp) FROM weatherdata WHERE latitude = :latitude AND longitude = :longitude")
    fun avgmaxtemp(latitude: String, longitude: String): List<Double>



}