package com.example.storygram.view.detail

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storygram.data.remote.response.ListStoryItem
import com.example.storygram.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyDetail = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(STORY_DATA, ListStoryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(STORY_DATA)
        }

        binding.apply {
            Glide.with(this@DetailActivity)
                .load(storyDetail?.photoUrl)
                .into(binding.ivDetailPhoto)
            binding.tvDetailName.text = storyDetail?.name
            binding.tvDetailDescription.text = storyDetail?.description
        }
    }

    companion object {
        const val STORY_DATA = "story_data"
    }
}