package com.example.storygram.view.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storygram.data.repository.StoryRepository
import kotlinx.coroutines.launch
import java.util.Locale

class SettingViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }

    fun getLanguage(): LiveData<Locale> {
        return storyRepository.getLanguange()
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            storyRepository.setLanguage(language)
        }
    }
}