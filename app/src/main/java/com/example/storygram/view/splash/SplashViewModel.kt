package com.example.storygram.view.splash

import androidx.lifecycle.ViewModel
import com.example.storygram.data.repository.StoryRepository

class SplashViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getLoginStatus() = storyRepository.getLoginStatus()
}