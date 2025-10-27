package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.FilterSettings
import com.example.android_practice.domain.repository.FilterRepository
import kotlinx.coroutines.flow.Flow

class GetFilterSettingsUseCase(
    private val repository: FilterRepository
) {
    operator fun invoke(): Flow<FilterSettings> = repository.getFilterSettings()
}