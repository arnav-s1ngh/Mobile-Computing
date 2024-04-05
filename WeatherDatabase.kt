import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.weatherdao
import com.example.myapplication.weatherentity
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [weatherentity::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherdao(): weatherdao

    companion object {

        @Volatile
        private var instance: WeatherDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context): WeatherDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context) = Room
            .databaseBuilder(context, WeatherDatabase::class.java, "weatherdata")
            .build()
    }
}