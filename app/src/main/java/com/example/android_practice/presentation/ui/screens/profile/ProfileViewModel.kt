package com.example.android_practice.presentation.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val profile = getProfileUseCase()
                _uiState.value = ProfileUiState(profile = profile)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Ошибка загрузки профиля: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun downloadResume() {
        viewModelScope.launch {
            ///TODO
        }
    }
}

data class ProfileUiState(
    val profile: com.example.android_practice.domain.model.Profile = com.example.android_practice.domain.model.Profile(),
    val isLoading: Boolean = false,
    val error: String? = null
)