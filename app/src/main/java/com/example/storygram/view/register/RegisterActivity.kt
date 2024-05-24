package com.example.storygram.view.register

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.storygram.R
import com.example.storygram.data.Result
import com.example.storygram.databinding.ActivityRegisterBinding
import com.example.storygram.utils.MotionVisibility.Companion.setMotionVisibilities
import com.example.storygram.utils.ObtainViewModelFactory
import com.example.storygram.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_top, 0)

        val viewModel = ObtainViewModelFactory.obtain<RegisterViewModel>(this)

        val alertBuilder = AlertDialog.Builder(this)

        binding.toLogin.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSignup.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            viewModel.register(name, email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.setMotionVisibilities(View.VISIBLE)
                            binding.btnSignup.isClickable = false
                        }

                        is Result.Success -> {
                            binding.progressBar.setMotionVisibilities(View.GONE)
                            binding.btnSignup.isClickable = true
                            alertBuilder.setTitle(getString(R.string.register_sukses))
                            alertBuilder.setMessage(getString(R.string.start_share))
                            alertBuilder.setPositiveButton(getString(R.string.login_now)) { _, _ ->
                                val intent = Intent(this, LoginActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finishAffinity()
                            }.create().show()
                        }

                        is Result.Error -> {
                            binding.progressBar.setMotionVisibilities(View.GONE)
                            binding.btnSignup.isClickable = true
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