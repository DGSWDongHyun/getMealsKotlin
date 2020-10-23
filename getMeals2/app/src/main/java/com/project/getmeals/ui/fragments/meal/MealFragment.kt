package com.project.getmeals.ui.fragments.meal

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.os.StrictMode
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.project.getmeals.R
import com.project.getmeals.databinding.FragmentMealBinding
import kr.go.neis.api.School
import java.text.SimpleDateFormat
import java.util.*


class MealFragment : Fragment() {


    private lateinit var binding: FragmentMealBinding
    private var INTPAGE : Int = 0
    private val calender : Calendar = Calendar.getInstance()
    private val school = School(School.Type.HIGH, School.Region.DAEGU, "D100000282")

    var checkAllergy : Boolean = false;
    val YEAR = Integer.parseInt(SimpleDateFormat("yyyy").format(Date()))
    var MONTH = Integer.parseInt(SimpleDateFormat("MM").format(Date()))
    var DAY = Integer.parseInt(SimpleDateFormat("dd").format(Date()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealBinding.inflate(inflater, container, false)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentMealBinding.inflate(layoutInflater)
        val view = binding.root

        initializeLayout()

        super.onViewCreated(view, savedInstanceState)
    }
    fun showDialog() {
        val allergyBuilder : AlertDialog.Builder =
            AlertDialog.Builder(
                ContextThemeWrapper(
                    context,
                    R.style.Theme_AppCompat_Light_Dialog
                )
            )
                .setTitle("알레르기 정보 안내")
                .setMessage(R.string.allergyInfo)
                .setPositiveButton("확인") { dialogInterface, i ->
                }

        val allergyDialog = allergyBuilder.create()

        allergyDialog.show()
    }
    fun NextPage() : String? {
        val menu = school.getMonthlyMenu(YEAR, MONTH)
        when(INTPAGE){
            0 -> {
                binding.timesetImg.setImageDrawable(context?.getDrawable(R.drawable.breakfast))
                binding.timeset.setText("아침")
                return if (checkAllergy) menu.get(DAY - 1).breakfast else RemoveNumbrOfMeals(
                    menu.get(
                        DAY - 1
                    ).breakfast
                )
            }
            1 -> {
                binding.timeset.setText("점심")
                binding.timesetImg.setImageDrawable(context?.getDrawable(R.drawable.lunch))
                return if (checkAllergy) menu.get(DAY - 1).lunch else RemoveNumbrOfMeals(
                    menu.get(
                        DAY - 1
                    ).lunch
                )
            }
            2 -> {
                binding.timeset.setText("저녁")
                binding.timesetImg.setImageDrawable(context?.getDrawable(R.drawable.dinner))
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
                    binding.timesetImg.setImageDrawable(context?.getDrawable(R.drawable.dinner))
                    return if(checkAllergy) menu.get(DAY - 1).dinner else RemoveNumbrOfMeals(
                        menu.get(
                            DAY - 1
                        ).dinner
                    )
                }else if(INTPAGE > 2){
                    INTPAGE = 0
                    binding.timeset.setText("아침")
                    binding.timesetImg.setImageDrawable(context?.getDrawable(R.drawable.breakfast))
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
        val datepickerdialog: DatePickerDialog? = context?.let {
            DatePickerDialog(
                it,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    MONTH = monthOfYear + 1
                    DAY = dayOfMonth

                    binding.check.setChecked(false)
                    initializeLayout();

                }, YEAR, MONTH - 1, DAY
            )
        }

        datepickerdialog?.show()

    }

    fun RemoveNumbrOfMeals(meals: String) : String?{
        var result : String ?= ""

        if(meals.isEmpty() || meals == null){
            binding.allergyInfoLayout.visibility = View.INVISIBLE;
            return "급식이 없습니다."
        }else{
            binding.allergyInfoLayout.visibility = View.VISIBLE;
        }


        for(idx in meals.indices){
            if(idx < meals.length){
                if(!(meals.get(idx) in '0'..'9' || meals.get(idx) == '.'))
                    if (meals.get(idx-1) in '0'..'9' && (meals.get(idx) == '인' || meals.get(idx) == '개') && idx != 0)
                        result += meals.get(idx)
            }
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

        binding.informatioAllergy.setOnClickListener {
            showDialog()
        }

        binding.check.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            checkAllergy = isChecked
            if (isChecked) {
                if (!NextPage()?.isEmpty()!! || NextPage() != null) {
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
}