package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.FilterSettings
import com.example.android_practice.domain.repository.FilterRepository

class SaveFilterSettingsUseCase(
    private val repository: FilterRepository
) {
    suspend operator fun invoke(settings: FilterSettings) = repository.saveFilterSettings(settings)
}