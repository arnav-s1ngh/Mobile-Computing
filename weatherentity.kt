package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weatherdata")
data class weatherentity(
    @PrimaryKey(autoGenerate = true)
    val id:Int =0,
    val latitude:String,
    val longitude: String,
    val date: String,
    val maxtemp: Double,
    val mintemp: Double
)