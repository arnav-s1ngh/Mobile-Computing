package com.example.myapplication

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class weatherrepo(private val weatherdaoo: weatherdao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    fun insertdata(newweather: weatherentity) {
        coroutineScope.launch(Dispatchers.IO) {
            weatherdaoo.insertdata(newweather)
        }
    }
}
