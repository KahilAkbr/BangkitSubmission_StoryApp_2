package com.example.storygram.view.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storygram.data.repository.StoryRepository
import com.example.storygram.di.Injection
import com.example.storygram.view.add.AddViewModel
import com.example.storygram.view.login.LoginViewModel
import com.example.storygram.view.main.MainViewModel
import com.example.storygram.view.register.RegisterViewModel
import com.example.storygram.view.setting.SettingViewModel
import com.example.storygram.view.splash.SplashViewModel

class ViewModelFactory private constructor(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass.getConstructor(storyRepository::class.java)
        return constructor.newInstance(storyRepository)
    }
}