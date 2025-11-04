package com.example.android_practice.data.repository.impl

import com.example.android_practice.data.datastore.FilterPreferences
import com.example.android_practice.domain.model.FilterSettings
import com.example.android_practice.domain.repository.FilterRepository
import kotlinx.coroutines.flow.Flow

class FilterRepositoryImpl(
    private val filterPreferences: FilterPreferences
) : FilterRepository {

    override fun getFilterSettings(): Flow<FilterSettings> {
        return filterPreferences.filterSettings
    }

    override suspend fun saveFilterSettings(settings: FilterSettings) {
        filterPreferences.saveFilterSettings(settings)
    }

    override suspend fun clearFilters() {
        filterPreferences.clearFilters()
    }
}