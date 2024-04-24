package com.example.myapplication

import android.content.Context
import android.graphics.PointF
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt
import android.util.Log;




var globcheck="Saved"
var ax= 0.toFloat()
var startime=0
var ay= 0.toFloat()
var az= 0.toFloat()
var pitch=0.toFloat()
var ro=0.toFloat()
var yaw=0.toFloat()
var lastTimestamp=0.toFloat()
val steps=5
var yawhist=listOf(Point(0f, 90f), Point(1f, 90f), Point(2f, 90f), Point(3f, 60f), Point(4f, 10f), Point(5f, 40f), Point(6f, 50f), Point(7f, 60f), Point(8f, 70f), Point(9f, 80f), Point(10f, 90f), Point(11f, 80f), Point(12f, 70f), Point(13f, 60f), Point(14f, 50f), Point(15f, 40f), Point(16f, 50f), Point(17f, 60f), Point(18f, 70f), Point(19f, 80f), Point(20f, 90f))
var rohist=listOf(Point(0f, 0f), Point(1f, 0f), Point(2f, 0f), Point(3f, 20f), Point(4f, 40f), Point(5f, 50f), Point(6f, 21f), Point(7f, 34f), Point(8f, 89f), Point(9f, 76f), Point(10f, 24f), Point(11f, 45f), Point(12f, 32f), Point(13f, 65f), Point(14f, 72f), Point(15f, 23f), Point(16f, 92f), Point(17f, 34f), Point(18f, 45f), Point(19f, 44f), Point(20f, 2f))
var pithhist=listOf(Point(0f, 37f), Point(1f, 22f), Point(2f, 58f), Point(3f, 16f), Point(4f, 71f), Point(5f, 90f), Point(6f, 43f), Point(7f, 65f), Point(8f, 11f), Point(9f, 89f), Point(10f, 35f), Point(11f, 78f), Point(12f, 29f), Point(13f, 50f), Point(14f, 84f), Point(15f, 20f), Point(16f, 62f), Point(17f, 47f), Point(18f, 80f), Point(19f, 10f), Point(20f, 53f))
var pointsData: List<Point> = yawhist
val xAxisData = AxisData.Builder()
    .axisStepSize(20.dp)
    .backgroundColor(Color.Blue)
    .steps(pointsData.size - 1)
    .labelData { i -> i.toString() }
    .labelAndAxisLinePadding(1.dp)
    .build()

val yAxisData = AxisData.Builder()
    .steps(steps)
    .backgroundColor(Color.Red)
    .labelAndAxisLinePadding(1.dp)
    .build()
val lineChartData = LineChartData(
    linePlotData = LinePlotData(
        lines = listOf(
            Line(
                dataPoints = rohist,
                LineStyle(),
                IntersectionPoint(),
                SelectionHighlightPoint(),
                ShadowUnderLine(),
                SelectionHighlightPopUp()
            )
        ),
    ),
    xAxisData = xAxisData,
    yAxisData = yAxisData,
    gridLines = GridLines(),
    backgroundColor = Color.White
)
val lineChartData2 = LineChartData(
    linePlotData = LinePlotData(
        lines = listOf(
            Line(
                dataPoints = yawhist,
                LineStyle(),
                IntersectionPoint(),
                SelectionHighlightPoint(),
                ShadowUnderLine(),
                SelectionHighlightPopUp()
            )
        ),
    ),
    xAxisData = xAxisData,
    yAxisData = yAxisData,
    gridLines = GridLines(),
    backgroundColor = Color.White
)
val lineChartData3 = LineChartData(
    linePlotData = LinePlotData(
        lines = listOf(
            Line(
                dataPoints = pithhist,
                LineStyle(),
                IntersectionPoint(),
                SelectionHighlightPoint(),
                ShadowUnderLine(),
                SelectionHighlightPopUp()
            )
        ),
    ),
    xAxisData = xAxisData,
    yAxisData = yAxisData,
    gridLines = GridLines(),
    backgroundColor = Color.White
)

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null
    private lateinit var viewModel: OrientationViewModel
    private var lastInsertTimeMillis: Long = 0
    private val insertIntervalMillis = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, OrientationViewModelFactory(this)).get(OrientationViewModel::class.java)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        setContent {
            MyApp(viewModel)
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        gyroscope?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTimeMillis = System.currentTimeMillis()

            if (true) {
                lastInsertTimeMillis = currentTimeMillis
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                ax=x
                ay=y
                az=z

                Log.i("data","$ax $ay $az $currentTimeMillis")

                var mag= sqrt((ax*ax)+(ay*ay)+(az*az))
                var normx=ax/mag
                var normy=ay/mag
                var normz=az/mag
                ro = (atan2(normy, normz)* (180 / Math.PI)).toFloat()
                var sq=sqrt(normy.pow(2) + normz.pow(2))
                pitch = (atan2((normx*(-1)).toDouble(),sq.toDouble() )* (180 / Math.PI).toFloat()).toFloat()
                viewModel.insertOrientationData(pitch, ro, az, currentTimeMillis)
            }

        }
        if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
            val currentTimeMillis = System.currentTimeMillis()
            val currentTimestamp = event.timestamp
            val dt = (currentTimestamp - lastTimestamp) / 1_000_000_000f // Calculate the time elapsed in seconds
            lastTimestamp = currentTimestamp.toFloat()
            val gz = event.values[2]
            yaw+=gz*dt
            yaw=yaw*(180/Math.PI).toFloat()
            viewModel.insertOrientationData(pitch, ro, az, currentTimeMillis)
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

@Composable
fun MyApp(viewModel: OrientationViewModel) {
    val orientationData by viewModel.orientationData.collectAsStateWithLifecycle()
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Absolute.Left
            ){Text(text = "Orientation Detection                                                       ",Modifier.background(
                Color.Blue), fontSize = 35.sp, color = Color.White )}
            Spacer(modifier = Modifier.height(30.dp))
            Column {
                Spacer(modifier = Modifier.height(30.dp))
                Spacer(modifier = Modifier.height(30.dp))
                Text(text = "Current Orientation:", fontSize = 20.sp, color = Color.Black )
                Text(text = "X: ${orientationData?.x ?: ""}", fontSize = 20.sp, color = Color.Black )
                Text(text = "Y: ${orientationData?.y ?: ""}", fontSize = 20.sp, color = Color.Black )
                Text(text = "Z: ${orientationData?.z ?: ""}", fontSize = 20.sp, color = Color.Black )
                Spacer(modifier = Modifier.height(150.dp))
                Text("Graphs:-", fontSize = 20.sp, color = Color.Black)
                LineChart(
                    modifier = Modifier
                        .height(100.dp),
                    lineChartData = lineChartData
                )
                LineChart(
                    modifier = Modifier
                        .height(100.dp),
                    lineChartData = lineChartData2
                )
                LineChart(
                    modifier = Modifier
                        .height(100.dp),
                    lineChartData = lineChartData3
                )


                }

            }
        }
    }


class OrientationViewModel(private val repository: OrientationRepository) : ViewModel() {
    private val _orientationData = MutableStateFlow<orientationdataentity?>(null)
    val orientationData: StateFlow<orientationdataentity?> = _orientationData
    fun insertOrientationData(x: Float, y: Float, z: Float, timestamp: Long) {
        viewModelScope.launch {
            repository.insertOrientationData(orientationdataentity(x = x, y = y, z = z, timestamp = timestamp))
            _orientationData.value = orientationdataentity(x = x, y = y, z = z, timestamp = timestamp)
        }
    }

//    init {
//        viewModelScope.launch {
//            _predictedData.value = repository.getPredictedData()
//        }
//    }
}

class OrientationRepository(
    private val dao: orientationdaointerface,
    private val context: Context
) {
    fun getOrientationData(): List<orientationdataentity> {
        return dao.getAllData()
    }

    suspend fun insertOrientationData(orientationData: orientationdataentity) {
        dao.insertorientdata(orientationData)
        saveDataToFile(orientationData, "orientation_data_mac.txt")
    }

    private fun saveDataToFile(data: orientationdataentity, file: String) {
    }
}


class OrientationViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrientationViewModel::class.java)) {
            val dao = dborientation.getInstance(context).Orientationdaointerface()
            val repository = OrientationRepository(dao, context)
            @Suppress("UNCHECKED_CAST")
            return OrientationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

