package com.example.android_practice.domain.repository

import com.example.android_practice.domain.model.FilterSettings
import kotlinx.coroutines.flow.Flow

interface FilterRepository {
    fun getFilterSettings(): Flow<FilterSettings>
    suspend fun saveFilterSettings(settings: FilterSettings)
    suspend fun clearFilters()
}