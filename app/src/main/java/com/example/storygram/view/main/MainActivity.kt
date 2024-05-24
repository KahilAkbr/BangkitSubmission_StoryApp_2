package com.example.storygram.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storygram.R
import com.example.storygram.adapter.StoryAdapter
import com.example.storygram.data.Result
import com.example.storygram.data.remote.response.ListStoryItem
import com.example.storygram.databinding.ActivityMainBinding
import com.example.storygram.utils.ObtainViewModelFactory
import com.example.storygram.view.add.AddActivity
import com.example.storygram.view.maps.MapsActivity
import com.example.storygram.view.setting.SettingActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ObtainViewModelFactory.obtain<MainViewModel>(this)

        binding.setting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        binding.addStory.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        binding.maps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        getStory()
    }

    private fun getStory() {
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter
        binding.rvStory.layoutManager = LinearLayoutManager(this)

        viewModel.getAllStory().observe(this){result->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    adapter.submitData(lifecycle, result.data)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}