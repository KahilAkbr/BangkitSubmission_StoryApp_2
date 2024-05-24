package com.example.storygram.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storygram.data.Result
import com.example.storygram.data.remote.response.ListStoryItem
import com.example.storygram.data.repository.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStoriesWithLocation() : LiveData<Result<List<ListStoryItem>>> {
        return storyRepository.getStoriesWithLocation()
    }
}