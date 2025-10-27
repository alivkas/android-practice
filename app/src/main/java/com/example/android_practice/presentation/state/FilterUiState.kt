package com.example.android_practice.presentation.state

import com.example.android_practice.domain.model.FilterSettings

data class FilterUiState(
    val filterSettings: FilterSettings = FilterSettings()
)