package com.example.storygram.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storygram.R
import com.example.storygram.data.preference.LoginPreferences
import com.example.storygram.data.remote.response.RegisterResponse
import com.example.storygram.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException
import com.example.storygram.data.Result
import com.example.storygram.data.local.StoryDatabase
import com.example.storygram.data.local.StoryRemoteMediator
import com.example.storygram.data.preference.LanguagePreferences
import com.example.storygram.data.remote.response.AddStoryResponse
import com.example.storygram.data.remote.response.ListStoryItem
import com.example.storygram.data.remote.response.LoginResponse
import com.example.storygram.data.remote.response.StoryResponse
import com.example.storygram.data.remote.retrofit.ApiConfig
import com.example.storygram.utils.reduceFileImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.Locale

class StoryRepository(
    private var apiService: ApiService,
    private val loginPreferences: LoginPreferences,
    private val languagePreferences: LanguagePreferences,
    private val storyDatabase: StoryDatabase
) {
    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(response, RegisterResponse::class.java)
            emit(Result.Error(error.message))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(response, LoginResponse::class.java)
            emit(Result.Error(error.message))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun saveToken(token: String) = loginPreferences.saveToken(token)

    suspend fun loginPref() = loginPreferences.loginPref()

    fun getLoginStatus() = loginPreferences.getLoginStatus()
    suspend fun logout() = loginPreferences.logout()

//    fun getAllStory(): LiveData<Result<StoryResponse>> = liveData {
//        emit(Result.Loading)
//        try {
//            val token = runBlocking {
//                loginPreferences.getToken().first()
//            }
//            apiService = ApiConfig.getApiSevice(token.toString())
//            val response = apiService.getAllStories()
//            emit(Result.Success(response))
//        } catch (e: HttpException) {
//            val response = e.response()?.errorBody()?.string()
//            val error = Gson().fromJson(response, StoryResponse::class.java)
//            emit(Result.Error(error.message))
//        } catch (e: Exception) {
//            emit(Result.Error(e.message.toString()))
//        }
//    }

    @OptIn(ExperimentalPagingApi::class)
    fun getAllStory(coroutineScope: CoroutineScope): LiveData<Result<PagingData<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking {
                loginPreferences.getToken().first()
            }
            apiService = ApiConfig.getApiSevice(token.toString())
            val response = Pager(
                config = PagingConfig(pageSize = 5 ),
                remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
                pagingSourceFactory = { storyDatabase.storyDao().getAllStories() }
            )
            val couroutineFlow = response.flow.cachedIn(coroutineScope)
            couroutineFlow.collect {pagingData->
                emit(Result.Success(pagingData))
            }
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(response, StoryResponse::class.java)
            emit(Result.Error(error.message))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadStory(
        context: Context,
        file: File?,
        description: String,
        lat: Float? = null,
        lon: Float? = null
    ): LiveData<Result<AddStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking {
                loginPreferences.getToken().first()
            }
            apiService = ApiConfig.getApiSevice(token.toString())
            if (file != null) {
                val files = file.reduceFileImage()
                val desc = description.toRequestBody("text/plain".toMediaType())
                val imageFile = files.asRequestBody("image/jpeg".toMediaType())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    files.name,
                    imageFile
                )
                val response = apiService.uploadStory(imageMultipart, desc, lat, lon)
                emit(Result.Success(response))
            } else {
                emit(Result.Error(context.getString(R.string.empty_image)))
            }

        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(response, AddStoryResponse::class.java)
            emit(Result.Error(error.message))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getLanguange(): LiveData<Locale> {
        return languagePreferences.getLanguage().asLiveData()
    }

    suspend fun setLanguage(language: String) {
        languagePreferences.setLanguage(language)
    }

    fun getStoriesWithLocation(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking {
                loginPreferences.getToken().first()
            }
            apiService = ApiConfig.getApiSevice(token.toString())
            val response = apiService.getStoriesWithLocation()
            val stories = response.listStory
            emit(Result.Success(stories))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
            emit(Result.Error(errorBody.message.toString()))
        } catch (e: Exception) {
            Log.d("StoryRepository", "getAllStoriesWithLocation: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            apiService: ApiService,
            preferences: LoginPreferences,
            preferences2: LanguagePreferences,
            storyDatabase : StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, preferences, preferences2, storyDatabase).also {
                    instance = it
                }
            }
    }
}