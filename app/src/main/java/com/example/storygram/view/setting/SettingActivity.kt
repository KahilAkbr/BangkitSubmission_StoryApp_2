package com.example.storygram.view.setting

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.storygram.R
import com.example.storygram.databinding.ActivitySettingBinding
import com.example.storygram.utils.ObtainViewModelFactory
import com.example.storygram.view.boarding.BoardingActivity
import com.example.storygram.view.main.MainActivity
import java.util.Locale

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var language: Locale
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ObtainViewModelFactory.obtain<SettingViewModel>(this)

        val alertBuilder = AlertDialog.Builder(this)

        binding.btnLogout.setOnClickListener {
            alertBuilder.setTitle(getString(R.string.logout))
            alertBuilder.setMessage(getString(R.string.logout_alert))
            alertBuilder.setPositiveButton("OK") { _, _ ->
                val intent = Intent(this, BoardingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finishAffinity()
                viewModel.logout()
            }.create().show()
        }

        binding.usFlag.setOnClickListener {
            language = Locale("en", "US")
            viewModel.setLanguage("en")
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(language))
            Toast.makeText(this, R.string.switch_language, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.indoFlag.setOnClickListener {
            language = Locale("in", "ID")
            viewModel.setLanguage("in")
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(language))
            Toast.makeText(this, R.string.switch_language, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        viewModel.getLanguage().observe(this) { lang ->
            language = lang
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(language))
        }

    }
}