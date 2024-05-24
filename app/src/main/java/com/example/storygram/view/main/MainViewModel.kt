package com.example.storygram.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storygram.data.Result
import com.example.storygram.data.remote.response.StoryResponse
import com.example.storygram.data.repository.StoryRepository

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getAllStory(): LiveData<Result<StoryResponse>> {
        return storyRepository.getAllStory()
    }
}