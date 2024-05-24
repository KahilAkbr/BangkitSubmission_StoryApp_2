package com.example.storygram.view.add

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storygram.data.Result
import com.example.storygram.data.remote.response.AddStoryResponse
import com.example.storygram.data.repository.StoryRepository
import java.io.File

class AddViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun uploadStory(
        context: Context,
        file: File?,
        description: String,
        lat: Float? = null,
        lon: Float? = null
    ): LiveData<Result<AddStoryResponse>> {
        return storyRepository.uploadStory(context, file, description, lat, lon)
    }
}