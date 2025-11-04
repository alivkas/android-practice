package com.example.android_practice.domain.repository

import com.example.android_practice.domain.model.Profile

interface ProfileRepository {
    suspend fun saveProfile(profile: Profile)
    suspend fun getProfile(): Profile
}