package com.example.lab5_view

import android.animation.Animator
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.CycleInterpolator
import android.view.animation.LinearInterpolator
import com.example.lab5_view.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        val textAnim = ObjectAnimator.ofFloat(findViewById(R.id.loading_text), "alpha", 1F, 0F)
        textAnim.duration = 1000L
        textAnim.repeatCount = ObjectAnimator.INFINITE
        textAnim.repeatMode = ObjectAnimator.REVERSE
        textAnim.start()
    }
}