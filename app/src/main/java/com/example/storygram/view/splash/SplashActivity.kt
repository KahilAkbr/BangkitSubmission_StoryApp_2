package com.example.storygram.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.storygram.R
import com.example.storygram.utils.ObtainViewModelFactory
import com.example.storygram.view.boarding.BoardingActivity
import com.example.storygram.view.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SplashActivity : AppCompatActivity() {
    companion object {
        const val SPLASH_DURATION: Long = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val viewModel = ObtainViewModelFactory.obtain<SplashViewModel>(this)

        Handler(Looper.getMainLooper()).postDelayed({
            val isLogin = runBlocking {
                viewModel.getLoginStatus().first()
            }
            val intent = if (isLogin == true) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, BoardingActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, SPLASH_DURATION)
    }
}