package com.example.android_practice.presentation.ui.screens.profile

import android.app.Application
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.data.manager.ImageManager
import com.example.android_practice.domain.model.Profile
import com.example.android_practice.domain.usecase.GetProfileUseCase
import com.example.android_practice.domain.usecase.SaveProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.io.FileOutputStream

class EditProfileViewModel(
    application: Application,
    private val getProfileUseCase: GetProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val imageManager: ImageManager
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _showImagePickerDialog = MutableStateFlow(false)
    val showImagePickerDialog: StateFlow<Boolean> = _showImagePickerDialog.asStateFlow()

    private var _cameraImageUri: Uri? = null
    val cameraImageUri: Uri? get() = _cameraImageUri

    fun loadProfile() {
        viewModelScope.launch {
            val profile = getProfileUseCase()
            _uiState.value = EditProfileUiState(profile = profile)
        }
    }

    fun updateFullName(fullName: String) {
        _uiState.value = _uiState.value.copy(profile = _uiState.value.profile.copy(fullName = fullName))
    }

    fun updatePosition(position: String) {
        _uiState.value = _uiState.value.copy(profile = _uiState.value.profile.copy(position = position))
    }

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(profile = _uiState.value.profile.copy(email = email))
    }

    fun updateResumeUrl(resumeUrl: String) {
        _uiState.value = _uiState.value.copy(profile = _uiState.value.profile.copy(resumeUrl = resumeUrl))
    }

    fun selectAvatar() {
        _showImagePickerDialog.value = true
    }

    fun hideImagePickerDialog() {
        _showImagePickerDialog.value = false
    }

    fun prepareCameraImage(): Uri {
        val file = imageManager.createImageFile()
        return imageManager.getUriForFile(file).also { _cameraImageUri = it }
    }

    fun getGalleryIntent(): android.content.Intent {
        return imageManager.getGalleryIntent()
    }

    fun getCameraIntent(): android.content.Intent {
        val uri = prepareCameraImage()
        return imageManager.getCameraIntent(uri)
    }

    fun onImageSelected(imageUri: Uri) {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val savedFile = copyUriToInternalFile(context, imageUri)
                _uiState.value = _uiState.value.copy(
                    profile = _uiState.value.profile.copy(avatarUri = savedFile.toUri().toString())
                )
            } catch (e: Exception) {
            }
            _showImagePickerDialog.value = false
        }
    }

    private fun copyUriToInternalFile(context: android.content.Context, uri: Uri): File {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, "avatar_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        inputStream?.close()
        return file
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