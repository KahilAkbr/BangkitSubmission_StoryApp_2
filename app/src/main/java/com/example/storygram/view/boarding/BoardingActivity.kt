package com.example.storygram.view.boarding

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.storygram.databinding.ActivityBoardingBinding
import com.example.storygram.view.login.LoginActivity
import com.example.storygram.view.register.RegisterActivity

class BoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playImageViewAnimation()
        binding.loginButton.setOnClickListener {
            val intentToLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentToLogin)
        }
        binding.signupButton.setOnClickListener {
            val intentToRegister = Intent(this, RegisterActivity::class.java)
            startActivity(intentToRegister)
        }
    }

    private fun playImageViewAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -40f, 20f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}