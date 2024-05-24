package com.example.storygram.view.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.storygram.R
import com.example.storygram.data.Result
import com.example.storygram.databinding.ActivityLoginBinding
import com.example.storygram.utils.ObtainViewModelFactory
import com.example.storygram.utils.setMotionVisibilities
import com.example.storygram.view.main.MainActivity
import com.example.storygram.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_top, 0)

        val viewModel = ObtainViewModelFactory.obtain<LoginViewModel>(this)

        val alertBuilder = AlertDialog.Builder(this)

        binding.toSignUp.setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.login(email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.setMotionVisibilities(View.VISIBLE)
                            binding.btnLogin.isClickable = false
                        }

                        is Result.Success -> {
                            binding.progressBar.setMotionVisibilities(View.GONE)
                            binding.btnLogin.isClickable = true
                            viewModel.saveToken(result.data.loginResult.token)
                            alertBuilder.setTitle(getString(R.string.login_sukses))
                            alertBuilder.setMessage(getString(R.string.start_share))
                            alertBuilder.setPositiveButton("OK") { _, _ ->
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finishAffinity()
                            }.setOnCancelListener {
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finishAffinity()
                            }.create().show()
                        }

                        is Result.Error -> {
                            binding.progressBar.setMotionVisibilities(View.GONE)
                            binding.btnLogin.isClickable = true
                            alertBuilder.setTitle(getString(R.string.login_error))
                            alertBuilder.setMessage(result.error)
                            alertBuilder.setPositiveButton("OK") { _, _ -> }.create().show()
                        }
                    }
                }
            }
        }

    }
}