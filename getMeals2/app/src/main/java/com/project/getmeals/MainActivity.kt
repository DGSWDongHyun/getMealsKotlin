package com.project.getmeals

import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.getmeals.databinding.ActivityMainBinding
import kr.go.neis.api.School
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var INTPAGE : Int = 0
    private val calender : Calendar = Calendar.getInstance()
    val school = School(School.Type.HIGH, School.Region.DAEGU, "D100000282")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        calender.time = Date()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val menu = school.getMonthlyMenu(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH))

        binding.menuName.setText(menu.get(parseInt(SimpleDateFormat("dd").format(Date()))).lunch)




    }
}