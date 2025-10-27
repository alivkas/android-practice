package com.example.android_practice.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.android_practice.domain.model.FilterSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.filterDataStore: DataStore<Preferences> by preferencesDataStore(name = "filter_settings")

class FilterPreferences(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val GENRE_KEY = stringPreferencesKey("genre")
        private val MIN_RATING_KEY = doublePreferencesKey("min_rating")
        private val YEAR_FROM_KEY = intPreferencesKey("year_from")
    }

    val filterSettings: Flow<FilterSettings> = dataStore.data.map { preferences ->
        FilterSettings(
            genre = preferences[GENRE_KEY] ?: "",
            minRating = preferences[MIN_RATING_KEY] ?: 0.0,
            yearFrom = preferences[YEAR_FROM_KEY] ?: 0
        )
    }

    suspend fun saveFilterSettings(settings: FilterSettings) {
        dataStore.edit { preferences ->
            preferences[GENRE_KEY] = settings.genre
            preferences[MIN_RATING_KEY] = settings.minRating
            preferences[YEAR_FROM_KEY] = settings.yearFrom
        }
    }

    suspend fun clearFilters() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}