package com.example.myapplication

import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.math.max
import kotlin.math.roundToInt
import android.util.DisplayMetrics
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Top_Blue()
                    JourneyApp()
                }


            }
        }
    }
}
// upper blue portion, displays the app name
@Composable
private fun Top_Blue() {
    Column(
        modifier = Modifier
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(text = "Route Tracker                              ",color=Color.White,fontSize=30.sp
            ,fontWeight = FontWeight.Bold,fontFamily = FontFamily.Monospace, modifier = Modifier.background(color= Color.Blue))
    }
    Spacer(modifier = Modifier.height(100.dp))
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Top_Blue()
    }

}

@Composable
fun JourneyApp() {
    var progress by remember { mutableStateOf(0.0f) }
    val maxprogress = 90.0f
    val increment = 10.0f
    var stop by remember { mutableStateOf(1) }
    var nextstop by remember { mutableStateOf(2) }
    var unit by remember { mutableStateOf("Kilometres") }
    var scal by remember { mutableStateOf(10.0f) }
    var cov=((stop-1)*scal).roundToInt()
    val remaining=(max(0.0f,(10-stop)*scal)).roundToInt()
    var set_size=10
    var u1=((stop-1)*scal).roundToInt()
    var u2=((stop-2)*scal).roundToInt()
    var u3=((stop-3)*scal).roundToInt()
    var u4=((stop-4)*scal).roundToInt()
    var u5=((stop-5)*scal).roundToInt()
    var u6=((stop-6)*scal).roundToInt()
    var u7=((stop-7)*scal).roundToInt()
    var u8=((stop-8)*scal).roundToInt()
    var u9=((stop-9)*scal).roundToInt()
    var u10=((stop-10)*scal).roundToInt()


    Spacer(modifier = Modifier.height(20.dp))

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Button(
            onClick = {
                if(unit=="Kilometres") {
                    unit="Miles"
                    scal=6.20f
                }
                else{
                    unit="Kilometres"
                    scal=10.0f
                }
            }
        ) {
            Text("The current unit is $unit")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = progress / maxprogress,
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .padding(horizontal = 2.dp)

        )
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            //space so as to fit row with lin. prog indicator
            Text(text = "1",fontSize = set_size.sp,color = Color.Black)
            Text(text = "2",fontSize = set_size.sp,color = Color.Black)
            Text(text = "3",fontSize = set_size.sp,color = Color.Black)
            Text(text = "4",fontSize = set_size.sp,color = Color.Black)
            Text(text = "5",fontSize = set_size.sp,color = Color.Black)
            Text(text = "6",fontSize = set_size.sp,color = Color.Black)
            Text(text = "7",fontSize = set_size.sp,color = Color.Black)
            Text(text = "8",fontSize = set_size.sp,color = Color.Black)
            Text(text = "9",fontSize = set_size.sp,color = Color.Black)
            Text(text = "10",fontSize = set_size.sp,color = Color.Black)

        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(text ="$u1",fontSize = 6.sp,color = Color.Black)
            Text(text ="$u2",fontSize = 6.sp,color = Color.Black)
            Text(text ="$u3",fontSize = 6.sp,color = Color.Black)
            Text(text ="$u4",fontSize = 6.sp,color = Color.Black)
            Text(text ="$u5",fontSize = 6.sp,color = Color.Black)
            Text(text ="$u6",fontSize = 6.sp,color = Color.Black)
            Text(text ="$u7",fontSize = 6.sp,color = Color.Black)
            Text(text ="$u8",fontSize = 6.sp,color = Color.Black)
            Text(text ="$u9",fontSize = 6.sp,color = Color.Black)
            Text(text ="$u10",fontSize = 6.sp,color = Color.Black)

        }
        // function to change progress
        Spacer(modifier = Modifier.height(1.dp))
        Button(
            onClick = {
                progress+=increment
                if (progress>=maxprogress) {
                    progress=maxprogress
                }
                stop+=1
                nextstop+=1
            }
        ) {
            Text("Next Stop")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // if stops>10
        LazyRow(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(Color.Gray)
        ) {
            items((1..max(10,stop)).toList()) { stop ->
                StopItem(stop = "Stop $stop")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = " Current: Stop$stop \n", color = Color.Black, fontSize = 30.sp,fontFamily = FontFamily.Monospace)
            Text(text = " Next: Stop$nextstop \n", color = Color.Black, fontSize = 30.sp,fontFamily = FontFamily.Monospace)
            if(remaining!=0) {
                Text(
                    text = " Remaining Dist:$remaining  \n",
                    color = Color.Black,
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            else{
                Text(
                    text = " Remaining Dist: N.A.  \n",
                    color = Color.Black,
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            Text(text = " Dist. Covered:$cov \n", color = Color.Black, fontSize = 30.sp,fontFamily = FontFamily.Monospace)

            //remaining distance till stop 10
            // dist covered till inf
        }
        Button(
            onClick = {
                stop=1
                nextstop=2
                progress=0.0f
                scal=10.0f
                unit="Kilometres"
            }
        ) {
            Text("Reset")
        }

    }
}

@Composable
fun StopItem(stop: String) {
    Text(text = stop, color = Color.Black, fontSize = 20.sp, modifier = Modifier.padding(horizontal = 8.dp))
}
