package com.example.myapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.TimeUnit




class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var viewModel: OrientationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, OrientationViewModelFactory(this)).get(OrientationViewModel::class.java)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        setContent {
            MyApp(viewModel)
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val timestamp = System.currentTimeMillis()
            viewModel.insertOrientationData(x, y, z, timestamp)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

@Composable
fun MyApp(viewModel: OrientationViewModel) {
    val orientationData by viewModel.orientationData.collectAsStateWithLifecycle()
    val predictedData by viewModel.predictedData.collectAsStateWithLifecycle()

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column {
                Text(text = "Current Orientation:")
                Text(text = "X: ${orientationData?.x ?: ""}")
                Text(text = "Y: ${orientationData?.y ?: ""}")
                Text(text = "Z: ${orientationData?.z ?: ""}")

                Text(text = "Predicted Orientation (10s):")
                predictedData?.let {
                    for (data in it) {
                        Text(text = "X: ${data.x}, Y: ${data.y}, Z: ${data.z}")
                    }
                }
            }
        }
    }
}

class OrientationViewModel(private val repository: OrientationRepository) : ViewModel() {
    private val _orientationData = MutableStateFlow<orientationdataentity?>(null)
    val orientationData: StateFlow<orientationdataentity?> = _orientationData

    private val _predictedData = MutableStateFlow<List<orientationdataentity>?>(null)
    val predictedData: StateFlow<List<orientationdataentity>?> = _predictedData

    fun insertOrientationData(x: Float, y: Float, z: Float, timestamp: Long) {
        viewModelScope.launch {
            repository.insertOrientationData(orientationdataentity(x = x, y = y, z = z, timestamp = timestamp))
            _orientationData.value = orientationdataentity(x = x, y = y, z = z, timestamp = timestamp)
        }
    }

    init {
        viewModelScope.launch {
            _predictedData.value = repository.getPredictedData()
        }
    }
}

class OrientationRepository(
    private val dao: orientationdaointerface,
    private val context: Context
) {
    fun getOrientationData(): List<orientationdataentity> {
        return dao.getAllData()
    }

    fun getPredictedData(): List<orientationdataentity> {
        val file = File(context.filesDir, "orientation_data.txt")
        return predictNextValues(file, 10)
    }

    suspend fun insertOrientationData(orientationData: orientationdataentity) {
        dao.insert(orientationData)
        saveDataToFile(orientationData, File(context.filesDir, "orientation_data.txt"))
    }

    private fun predictNextValues(file: File, numValues: Int): List<orientationdataentity> {
        // Implement your time series prediction logic here
        // This is just a dummy implementation that returns the same values
        val lastData = dao.getAllData().lastOrNull() ?: return emptyList()
        return List(numValues) { orientationdataentity(x = lastData.x, y = lastData.y, z = lastData.z, timestamp = lastData.timestamp + TimeUnit.SECONDS.toMillis(it.toLong())) }
    }

    private fun saveDataToFile(data: orientationdataentity, file: File) {
        file.appendText("${data.x},${data.y},${data.z},${data.timestamp}\n")
    }
}

class OrientationViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrientationViewModel::class.java)) {
            val dao = dborientation.getInstance(context).orientationdaointerface()
            val repository = OrientationRepository(dao, context)
            @Suppress("UNCHECKED_CAST")
            return OrientationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
