package com.example.storygram.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storygram.data.Result
import com.example.storygram.data.remote.response.LoginResponse
import com.example.storygram.data.repository.StoryRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        return storyRepository.login(email, password)
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            storyRepository.saveToken(token)
            storyRepository.loginPref()
        }
    }
}