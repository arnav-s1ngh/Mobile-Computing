package com.example.myapplication
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [orientationdataentity::class], version = 1)
public abstract class dborientation : RoomDatabase() {
    abstract fun orientationdaointerface(): orientationdaointerface
    companion object {
        @Volatile
        private var INSTANCE: dborientation? = null

        fun getInstance(context: Context): dborientation {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    dborientation::class.java,
                    "orientation_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}