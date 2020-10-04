package com.project.getmeals.ui.fragments.rain

import android.Manifest
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
import com.project.getmeals.databinding.FragmentRainBinding
import com.project.getmeals.network.server.Server
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RainFragment : Fragment() {

    private lateinit var binding : FragmentRainBinding
    private val API : String = "cb86a0c7df74ca1305974f5f78b0cfa6"
    private var latitude : String = "";
    private var longitude : String = "";

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
        var res: Call<JsonObject> = Server
            .getInstance()
            .buildRetrofit()
            .getCurrentWeather(latitude, longitude, API)

        res.enqueue(object : Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("N", "Failure : ${t.message.toString()}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                var jsonObj = JSONObject(response.body().toString())
                Log.d("N", "Success :: $jsonObj")

            }
        })
    }
    fun checkPermission() {
        if (activity?.let { ActivityCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) }
            != PackageManager.PERMISSION_GRANTED
            && activity?.let { ActivityCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) }
            != PackageManager.PERMISSION_GRANTED) {
            //권한이 없을 경우 최초 권한 요청 또는 사용자에 의한 재요청 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                && ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // 권한 재요청
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        100)
                }
                return
            } else {
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        100
                    )
                }
                return
            }
        }else{
            getCurrentWeather()

            val locationManager : LocationManager? = null
            val lastKnownLocation: Location? =
                locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation != null) {
                longitude = lastKnownLocation.longitude.toString()
                latitude = lastKnownLocation.latitude.toString()
            }
        }
    }
}