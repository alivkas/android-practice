package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.Profile
import com.example.android_practice.domain.repository.ProfileRepository

class SaveProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(profile: Profile) = repository.saveProfile(profile)
}