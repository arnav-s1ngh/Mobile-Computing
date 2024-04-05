package com.example.myapplication

import WeatherDatabase
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.util.Calendar


var futureoption=0
var globmint=0.0
var globmax=0.0
var globlat=""
var globlong=""
var tim=""
var checkifplease=0
var readytosearch=0
var minifoff=0.0
var maxifoff=0.0
var emg=0
var url=""
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()

        }

    }

}
var intercheck=3
fun checkConnectivity(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}
// credits:https://stackoverflow.com/questions/51141970/check-internet-connectivity-android-in-kotlin
@Composable
fun WeatherApp(context: Context = LocalContext.current) {
    val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(context.applicationContext) as T
        }
    }

    val viewModel: WeatherViewModel = viewModel(factory = viewModelFactory)
    val uiState = viewModel.uiState.collectAsState()
    if (checkConnectivity(context)){
        checkifplease=0
    }
    else{
        checkifplease==2
        emg=2
    }
    Row(
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Absolute.Left
    ){Text(text = "Weather App  $checkifplease                                        ",Modifier.background(Color.Blue), fontSize = 35.sp, color = Color.White )}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var latitude by remember { mutableStateOf(TextFieldValue("")) }
        var longitude by remember { mutableStateOf(TextFieldValue("")) }
        var date by remember { mutableStateOf(TextFieldValue("")) }
        var col1 by remember {
            mutableStateOf(Color.Red)
        }
        var col2 by remember {
            mutableStateOf(Color.Red)
        }
        var col3 by remember {
            mutableStateOf(Color.Red)
        }

        var makeitmove by remember {
            mutableStateOf(0)
        }
        var latstat by remember {
            mutableStateOf((""))
        }
        var lonstat by remember {
            mutableStateOf((""))
        }
        var datestat by remember {
            mutableStateOf((""))
        }
        val cali = Calendar.getInstance()
        checkifplease= intercheck
        var daytoday by remember {
            mutableStateOf(cali[Calendar.DAY_OF_MONTH])
        }
        var monthtoday by remember {
            mutableStateOf(cali[Calendar.MONTH]+1)
        }
        var yeartoday by remember {
            mutableStateOf(cali[Calendar.YEAR])
        }
        try {
            val latitudeValue = latitude.text.toDouble()
            if (latitudeValue in -90.0..90.0) {
                latstat = "The Latitude Field Value is Valid"
                col1=Color.Green
            } else {
                latstat = "The Latitude Field Value is not within the valid range (-90 to 90)"
                col1=Color.Red
            }
        } catch (e: NumberFormatException) {
            latstat = "Invalid Latitude Field Value: ${latitude.text}"
            col1=Color.Red
        }
        try {
            val longitudevalue = longitude.text.toDouble()
            if (longitudevalue in -180.0..180.0) {
                lonstat = "The Longitude Field Value is Valid"
                col2=Color.Green
            } else {
                lonstat = "The Longitude Field Value is not within the range -90 to 90"
                col2=Color.Red
            }
        } catch (e: NumberFormatException) {
            lonstat = "Invalid Longitude Field Value: ${longitude.text}"
            col2=Color.Red
        }

        fun isValidDate(dateString: String): Boolean {

            val regex = Regex("^\\d{4}-\\d{2}-\\d{2}\$")
            if (!regex.matches(dateString)) {
                col3=Color.Red
                return false
            }
            val parts = dateString.split("-")
            val year = parts[0].toInt()
            val month = parts[1].toInt()
            val day = parts[2].toInt()
            if((year>yeartoday) or ((month>monthtoday) and (year==yeartoday)) or ((day>daytoday) and (month==monthtoday) and (year==yeartoday))){
                futureoption=1
            }
            if (year < 0 || month !in 1..12 || day !in 1..31) {
                col3=Color.Red
                return false
            }
            if (listOf(4, 6, 9, 11).contains(month) && day > 30) {
                col3=Color.Red
                return false
            }
            if (month == 2) {
                val isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
                if (isLeapYear && day > 29) {
                    col3=Color.Red
                    return false
                } else if (!isLeapYear && day > 28) {
                    col3=Color.Red
                    return false
                }
            }
            col3=Color.Green
            return true
        }


        Text(text=latstat,color=col1)
        Text(text=lonstat,color=col2)
        Text(text="Date Validity: "+isValidDate(dateString = date.text), color = col3)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = latitude,
            onValueChange = { latitude = it },
            label = { Text("Latitude") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = longitude,
            onValueChange = { longitude = it },
            label = { Text("Longitude") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(

            onClick = {
                if ((col1==Color.Green) and (col2==Color.Green) and (col3==Color.Green)){
                viewModel.fetchWeatherData(
                    latitude.text,
                    longitude.text,
                    date.text
                )

                }



            }
        ) {
            Text(text = "Get Weather Data")
        }

        Spacer(modifier = Modifier.height(16.dp))


        when (val state = uiState.value) {
            is WeatherUiState.Success -> {
                val maxTemperature = state.weatherData.temperatureMax.sum()/state.weatherData.temperatureMax.size

                val minTemperature = state.weatherData.temperatureMin.sum()/state.weatherData.temperatureMin.size


                if (maxTemperature != null && minTemperature != null) {
                    Text("Maximum Temperature: $maxTemperature°C")
                    Text("Minimum Temperature: $minTemperature°C")




                    }
                else {
                    Text("No temperature data available")
                }
            }
            is WeatherUiState.Error -> Text(state.errorMessage)
            WeatherUiState.Loading -> {
            }
            else -> {}
        }
    }
}

@Suppress("OPT_IN_USAGE")
@OptIn(ExperimentalStdlibApi::class)
data class WeatherData(
    val time: List<String>,
    @SerializedName("temperature_2m_max")
    val temperatureMax:
    List<Double>,
    @SerializedName("temperature_2m_min")
    val temperatureMin: List<Double>
)

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weatherData: WeatherData) : WeatherUiState()
    data class Error(val errorMessage: String) : WeatherUiState()
}

class WeatherViewModel(private val applicationContext: Context) : ViewModel() {
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState
    private val mutex= Mutex()

    fun fetchWeatherData(latitude: String, longitude: String, date: String) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                    val weatherData =
                        fetchWeatherFromApi(latitude, longitude, date, applicationContext)
                    globmax = weatherData.temperatureMax[0]
                    globmint = weatherData.temperatureMin[0]
                    globlat = latitude
                    globlong = longitude
                    tim = date
                    checkifplease = 1
                    _uiState.value = WeatherUiState.Success(weatherData)
                if(checkifplease==2) {
                        val weatherdaoo=WeatherDatabase.getDatabase(applicationContext).weatherdao()
                        val weatherent=weatherentity(latitude = globlat, longitude = globlong, date = tim, mintemp = globmint, maxtemp = globmax)

                                weatherdaoo.insertdata(weatherent)
                                maxifoff= 1.0
                                minifoff=weatherdaoo.getmintemp(globlat,globlong,tim)[0]
                                maxifoff=weatherdaoo.getmaxtemp(globlat,globlong,tim)[0]

                        checkifplease=0
                        readytosearch+=1
                    }
            }
            catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(e.message ?: "$globmax $globmint $latitude $latitude $date")
            }
        }
    }

    @SuppressLint("Range")
    private suspend fun fetchWeatherFromApi(latitude: String, longitude: String, date: String, context: Context): WeatherData=withContext(Dispatchers.IO) {
        url=
            "https://archive-api.open-meteo.com/v1/archive" +
                    "?latitude=$latitude" +
                    "&longitude=$longitude" +
                    "&start_date=$date" +
                    "&end_date=$date" +
                    "&daily=temperature_2m_max,temperature_2m_min"

        if (futureoption==1){
            url = "https://archive-api.open-meteo.com/v1/archive" +
                    "?latitude=$latitude" +
                    "&longitude=$longitude" +
                    "&start_date=2014-01-01" +
                    "&end_date=2024-01-01" +
                    "&daily=temperature_2m_max,temperature_2m_min"
        }

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Weather Data")
            .setDescription("Downloading weather data")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "weather_data.json")

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)

        val query = DownloadManager.Query().setFilterById(downloadId)
        var downloading = true
        while (downloading) {
            val cursor: Cursor = downloadManager.query(query)
            cursor.moveToFirst()
            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                downloading = false
            }
            cursor.close()
        }

        val downloadedFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "weather_data.json")
        val jsonString = downloadedFile.readText()
        val jsonObject = JSONObject(jsonString)
        val dailyObject = jsonObject.getJSONObject("daily")
        val timeArray = dailyObject.getJSONArray("time").let { jsonArray ->
            val timeList = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                timeList.add(jsonArray.getString(i))
            }
            timeList
        }
        val temperatureMaxArray = dailyObject.getJSONArray("temperature_2m_max").let { jsonArray ->
            val tempList = mutableListOf<Double>()
            for (i in 0 until jsonArray.length()) {
                tempList.add(jsonArray.getDouble(i))
            }
            tempList
        }
        val temperatureMinArray = dailyObject.getJSONArray("temperature_2m_min").let { jsonArray ->
            val tempList = mutableListOf<Double>()
            for (i in 0 until jsonArray.length()) {
                tempList.add(jsonArray.getDouble(i))
            }
            tempList
        }
        WeatherData(timeArray, temperatureMaxArray, temperatureMinArray)
    }




}
