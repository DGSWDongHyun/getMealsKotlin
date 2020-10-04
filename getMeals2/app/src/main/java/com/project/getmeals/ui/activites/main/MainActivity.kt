package com.project.getmeals.ui.activites.main

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.project.getmeals.R
import com.project.getmeals.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getTime()

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


