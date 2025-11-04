package com.example.android_practice.domain.usecase

import com.example.android_practice.domain.model.Profile
import com.example.android_practice.domain.repository.ProfileRepository

class GetProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Profile = repository.getProfile()
}