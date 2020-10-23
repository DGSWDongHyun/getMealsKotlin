package com.project.getmeals.data.response.array

import com.project.getmeals.data.response.array.weather.Weather
import com.project.getmeals.data.response.obj.Main

data class WeatherData(var weather : ArrayList<Weather>, var main : Main)