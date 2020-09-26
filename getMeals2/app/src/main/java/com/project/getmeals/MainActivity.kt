package com.project.getmeals

import android.R.id.toggle
import android.app.DatePickerDialog
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.getmeals.databinding.ActivityMainBinding
import com.project.getmeals.viewmodel.ViewModelCheck
import com.project.getmeals.viewmodel.factory.ViewModelCheckFactory
import kr.go.neis.api.School
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var INTPAGE : Int = 0
    private val calender : Calendar = Calendar.getInstance()
    private val school = School(School.Type.HIGH, School.Region.DAEGU, "D100000282")

    var checkAllergy : Boolean = false;
    val YEAR = parseInt(SimpleDateFormat("yyyy").format(Date()))
    var MONTH = parseInt(SimpleDateFormat("MM").format(Date()))
    var DAY = parseInt(SimpleDateFormat("dd").format(Date()))

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
                return if (checkAllergy) menu.get(DAY - 1).breakfast else RemoveNumbrOfMeals(
                    menu.get(
                        DAY - 1
                    ).breakfast
                )
            }
            1 -> {
                binding.timeset.setText("점심")
                binding.timesetImg.setImageDrawable(getDrawable(R.drawable.lunch))
                return if (checkAllergy) menu.get(DAY - 1).lunch else RemoveNumbrOfMeals(
                    menu.get(
                        DAY - 1
                    ).lunch
                )
            }
            2 -> {
                binding.timeset.setText("저녁")
                binding.timesetImg.setImageDrawable(getDrawable(R.drawable.dinner))
                return if (checkAllergy) menu.get(DAY - 1).dinner else RemoveNumbrOfMeals(
                    menu.get(
                        DAY - 1
                    ).dinner
                )
            }
            else -> {
                if(INTPAGE < 0){
                    INTPAGE = 2;
                    binding.timeset.setText("저녁")
                    binding.timesetImg.setImageDrawable(getDrawable(R.drawable.dinner))
                    return if(checkAllergy) menu.get(DAY - 1).dinner else RemoveNumbrOfMeals(
                        menu.get(
                            DAY - 1
                        ).dinner
                    )
                }else if(INTPAGE > 2){
                    INTPAGE = 0
                    binding.timeset.setText("아침")
                    binding.timesetImg.setImageDrawable(getDrawable(R.drawable.breakfast))
                    return if(checkAllergy) menu.get(DAY - 1).breakfast else RemoveNumbrOfMeals(
                        menu.get(
                            DAY - 1
                        ).breakfast
                    )
                }
            }
        }
        return null
    }

    fun SpinnerDate() {
        val datepickerdialog: DatePickerDialog = DatePickerDialog(
            MainActivity@ this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                MONTH = monthOfYear + 1
                DAY = dayOfMonth

                binding.check.setChecked(false)
                initializeLayout();

            }, YEAR, MONTH - 1, DAY
        )

        datepickerdialog.show()

    }

    fun RemoveNumbrOfMeals(meals: String) : String?{
        var result : String ?= ""




            if(meals.isEmpty() || meals == null){
                binding.check.visibility = INVISIBLE;
                return "급식이 없습니다."
            }else{
                binding.check.visibility = VISIBLE;
            }


        for(idx in meals.indices){
            if(!(meals.get(idx) in '0'..'9' || meals.get(idx) == '.'))
                result += meals.get(idx)
        }
        return result
    }
    fun initializeLayout(){
        calender.time = Date()

        // getPlan
        val plan = school.getMonthlySchedule(YEAR, MONTH)
        var plan_str : String ?= plan.get(DAY - 1).schedule

        if (plan_str != null) {
            if(!plan_str.isEmpty())
                binding.notification.setText("오늘의 학사 일정 : " + plan_str)
            else
                binding.notification.setText("오늘은 학사 일정이 없네요.")
        }

        // getPlan

        binding.check.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            checkAllergy = isChecked
            if(isChecked){
                if(!NextPage()?.isEmpty()!! || NextPage() != null) {
                    binding.menuName.setText(NextPage())
                }
            } else {
                if (!NextPage()?.isEmpty()!! || NextPage() != null) {
                    binding.menuName.setText(NextPage())
                }
            }
        })

        checkAllergy = binding.check.isChecked;

        binding.menuName.setText(NextPage());

        binding.next.setOnClickListener {
            INTPAGE++
            binding.menuName.setText(NextPage())
        }

        binding.prev.setOnClickListener {
            INTPAGE --;
            binding.menuName.setText(NextPage())
        }

        binding.datepick.setOnClickListener {
            SpinnerDate()
        }

    }
    fun getTime() {
        var time = SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Date())
        binding.time.setText(time)
        Handler().postDelayed(Runnable {
            binding.time.setText(time)
            getTime()
        }, 1000)
    }
}


