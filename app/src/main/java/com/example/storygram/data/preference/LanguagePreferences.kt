package com.example.storygram.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale

class LanguagePreferences private constructor(private val dataStore: DataStore<Preferences>) {
    fun getLanguage(): Flow<Locale> {
        return dataStore.data.map { preferences ->
            val language = preferences[LANGUAGE_PREF] ?: Locale.getDefault().language
            Locale(language)
        }
    }

    suspend fun setLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_PREF] = language
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LanguagePreferences? = null
        private val LANGUAGE_PREF = stringPreferencesKey("language_pref")

        fun getInstance(dataStore: DataStore<Preferences>): LanguagePreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = LanguagePreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}