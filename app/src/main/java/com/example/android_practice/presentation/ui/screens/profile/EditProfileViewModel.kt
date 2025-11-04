package com.example.android_practice.presentation.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.domain.model.Profile
import com.example.android_practice.domain.usecase.GetProfileUseCase
import com.example.android_practice.domain.usecase.SaveProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            val profile = getProfileUseCase()
            _uiState.value = EditProfileUiState(profile = profile)
        }
    }

    fun updateFullName(fullName: String) {
        _uiState.value = _uiState.value.copy(
            profile = _uiState.value.profile.copy(fullName = fullName)
        )
    }

    fun updatePosition(position: String) {
        _uiState.value = _uiState.value.copy(
            profile = _uiState.value.profile.copy(position = position)
        )
    }

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(
            profile = _uiState.value.profile.copy(email = email)
        )
    }

    fun updateResumeUrl(resumeUrl: String) {
        _uiState.value = _uiState.value.copy(
            profile = _uiState.value.profile.copy(resumeUrl = resumeUrl)
        )
    }

    fun selectAvatar() {
        //TODO
    }

    fun saveProfile() {
        viewModelScope.launch {
            saveProfileUseCase(_uiState.value.profile)
        }
    }
}

data class EditProfileUiState(
    val profile: Profile = Profile(),
    val isLoading: Boolean = false,
    val error: String? = null
)