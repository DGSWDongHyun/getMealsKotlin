package com.project.getmeals.ui.fragments.rain

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.project.getmeals.data.response.array.WeatherData
import com.project.getmeals.data.response.array.weather.Weather
import com.project.getmeals.databinding.FragmentRainBinding
import com.project.getmeals.network.server.Server
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RainFragment : Fragment() {
    private var weather : WeatherData ?= null
    private lateinit var binding: FragmentRainBinding
    private val API: String = "cb86a0c7df74ca1305974f5f78b0cfa6"
    private var latitude: String = "";
    private var longitude: String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkPermission()
        super.onViewCreated(view, savedInstanceState)
    }

    fun getCurrentWeather() {
        var res: Call<WeatherData> = Server
            .getInstance()
            .buildRetrofit()
            .getCurrentWeather(latitude, longitude, API)

        res.enqueue(object : Callback<WeatherData> {

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.d("Failed", "Failure : ${t.message.toString()}")
            }

            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                response.body()?.let { setWeather(it) }

            }
        })
    }

    fun checkPermission() {
        if (activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            != PackageManager.PERMISSION_GRANTED
            && activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }
            != PackageManager.PERMISSION_GRANTED) {
            //권한이 없을 경우 최초 권한 요청 또는 사용자에 의한 재요청 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                && ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                // 권한 재요청
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it, arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        100
                    )
                }
                return
            } else {
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it, arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        100
                    )
                }
                return
            }
        } else {
            val locationManager: LocationManager =
                context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var location: Location? =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (location != null) {
                getLocation(location)
            }else{
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    getLocation(location)
                }
            }
        }
    }

    fun setWeather(weather : WeatherData){
        val convertTemp = weather.main.temp?.minus(273.15)
        binding.todayWeather.setText(String.format("%.2f",convertTemp))
    }

    fun getLocation(location: Location) {
        longitude = location.longitude.toString()
        latitude = location.latitude.toString()

        Log.d("Success", "$longitude,$latitude")

        getCurrentWeather()
    }
}