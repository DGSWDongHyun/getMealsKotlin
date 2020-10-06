package com.project.getmeals.ui.activites.main

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.project.getmeals.R
import com.project.getmeals.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var isMealFragment : Boolean? = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getTime()
        setInitializeLayout()

    }

    fun setInitializeLayout(){
        val controller = Navigation.findNavController(this, R.id.fragment_host)

        binding.test.setOnClickListener {
            if(isMealFragment!!){
                isMealFragment = false
                controller.navigate(R.id.action_mealFragment_to_rainFragment)
            }else{
                isMealFragment = true
                controller.navigate(R.id.action_rainFragment_to_mealFragment)
            }
        }
    }
    fun getTime(){
        var time = SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Date())
        binding.time.setText(time)
        Handler().postDelayed(Runnable {
            binding.time.setText(time)
            getTime()
        }, 1000)
    }

    override fun onBackPressed() {
        val endDialogBuilder = AlertDialog.Builder(this@MainActivity)
            .setIcon(R.drawable.ic_baseline_info_24)
            .setTitle("종료")
            .setMessage("정말 종료하시겠어요?")
            .setPositiveButton("확인"){ dialogInterface, i ->
                super.onBackPressed()
            }
            .setNegativeButton("취소"){ dialogInterface, i->

            }
        val endDialog = endDialogBuilder.create()

        endDialog.show()
    }
}


