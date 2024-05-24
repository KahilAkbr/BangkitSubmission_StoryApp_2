package com.example.storygram.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storygram.data.Result
import com.example.storygram.data.remote.response.RegisterResponse
import com.example.storygram.data.repository.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> {
        return storyRepository.register(name, email, password)
    }
}