package com.example.myapplication
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf( orientationdataentity::class), version = 1)
public abstract class dborientation  : RoomDatabase() {
    abstract fun Orientationdaointerface(): orientationdaointerface
    companion object {
        @Volatile
        private var INSTANCE: dborientation? = null

        fun getInstance(context: Context): dborientation {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        dborientation::class.java,
                        "orientation_data"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
