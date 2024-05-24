package com.example.storygram.di

import android.content.Context
import com.example.storygram.data.local.StoryDatabase
import com.example.storygram.data.preference.LanguagePreferences
import com.example.storygram.data.preference.LoginPreferences
import com.example.storygram.data.preference.dataStore
import com.example.storygram.data.remote.retrofit.ApiConfig
import com.example.storygram.data.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getToken().first() }
        val apiService = ApiConfig.getApiSevice(user.toString())
        val pref2 = LanguagePreferences.getInstance(context.dataStore)
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(apiService, pref, pref2, database)
    }
}