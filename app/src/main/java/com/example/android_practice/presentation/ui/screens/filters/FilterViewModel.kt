package com.example.android_practice.presentation.ui.screens.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.data.cache.BadgeStateManager
import com.example.android_practice.domain.model.FilterSettings
import com.example.android_practice.domain.usecase.GetFilterSettingsUseCase
import com.example.android_practice.domain.usecase.SaveFilterSettingsUseCase
import com.example.android_practice.presentation.state.FilterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FilterViewModel(
    private val getFilterSettingsUseCase: GetFilterSettingsUseCase,
    private val saveFilterSettingsUseCase: SaveFilterSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getFilterSettingsUseCase().collect { settings ->
                _uiState.value = _uiState.value.copy(filterSettings = settings)
                updateBadgeState(settings)
            }
        }
    }

    fun updateFilterSettings(settings: FilterSettings) {
        _uiState.value = _uiState.value.copy(filterSettings = settings)
        updateBadgeState(settings)
    }

    fun saveFilters() {
        viewModelScope.launch {
            saveFilterSettingsUseCase(_uiState.value.filterSettings)
        }
    }

    fun clearFilters() {
        _uiState.value = _uiState.value.copy(filterSettings = FilterSettings())
        viewModelScope.launch {
            saveFilterSettingsUseCase(FilterSettings())
            BadgeStateManager.setHasActiveFilters(false)
        }
    }

    private fun updateBadgeState(settings: FilterSettings) {
        val hasActiveFilters = !settings.isDefault
        BadgeStateManager.setHasActiveFilters(hasActiveFilters)
    }
}