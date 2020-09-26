package com.project.getmeals

import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.util.Log
import android.view.View
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

    private val school = School(School.Type.HIGH, School.Region.DAEGU, "D100000282")

    val YEAR = parseInt(SimpleDateFormat("yyyy").format(Date()))
    val MONTH = parseInt(SimpleDateFormat("MM").format(Date()))
    val DAY = parseInt(SimpleDateFormat("dd").format(Date()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        initializeLayout()
        getTime()

    }
    fun NextPage() : String? {
        val menu = school.getMonthlyMenu(YEAR, MONTH)
        when(INTPAGE){
            0 -> {
                binding.timesetImg.setImageDrawable(getDrawable(R.drawable.breakfast))
                binding.timeset.setText("아침")
                return RemoveNumbrOfMeals(menu.get(DAY - 1).breakfast)
            }
            1 -> {
                binding.timeset.setText("점심")
                binding.timesetImg.setImageDrawable(getDrawable(R.drawable.lunch))
                return RemoveNumbrOfMeals(menu.get(DAY - 1).lunch)
            }
            2 -> {
                binding.timeset.setText("저녁")
                binding.timesetImg.setImageDrawable(getDrawable(R.drawable.dinner))
                return RemoveNumbrOfMeals(menu.get(DAY - 1).dinner)
            }
            else -> {
                if(INTPAGE < 0){
                    INTPAGE = 2;
                    binding.timeset.setText("저녁")
                    binding.timesetImg.setImageDrawable(getDrawable(R.drawable.dinner))
                    return RemoveNumbrOfMeals(menu.get(DAY - 1).dinner)
                }else if(INTPAGE > 2){
                    INTPAGE = 0
                    binding.timeset.setText("아침")
                    binding.timesetImg.setImageDrawable(getDrawable(R.drawable.breakfast))
                    return RemoveNumbrOfMeals(menu.get(DAY - 1).breakfast)
                }
            }
        }
        return null
    }
    fun RemoveNumbrOfMeals(meals : String) : String?{
        for(idx in meals.indices){
            if(meals.get(idx) in '0'..'9' || meals.get(idx) == '.'){
                meals[idx].minus(idx)
            }
        }
        return meals
    }
    fun initializeLayout(){
        calender.time = Date()

        // getPlan

        val plan = school.getMonthlySchedule(YEAR, MONTH)
        if(plan != null)
            binding.notification.setText("오늘의 학사 일정 : " + plan.get(DAY - 1).schedule)
        else
            binding.notification.setText("오늘은 학사 일정이 없네요.")
        // getPlan

        binding.menuName.setText(NextPage());

        binding.next.setOnClickListener {
            binding.menuName.setText(NextPage())
            INTPAGE++
        }

        binding.prev.setOnClickListener {
            INTPAGE --;
            binding.menuName.setText(NextPage())
        }

    }
    fun getTime() {
        var time = SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Date())
        binding.time.setText(time)
        Handler().postDelayed(Runnable {
            binding.time.setText(time)
            getTime()
        },1000)
    }
}