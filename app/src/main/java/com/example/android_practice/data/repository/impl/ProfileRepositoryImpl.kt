package com.example.android_practice.data.repository.impl

import android.content.Context
import android.content.SharedPreferences
import com.example.android_practice.domain.model.Profile
import com.example.android_practice.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val context: Context
) : ProfileRepository {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    }

    override suspend fun saveProfile(profile: Profile) {
        sharedPreferences.edit().apply {
            putString(FULL_NAME_KEY, profile.fullName)
            putString(AVATAR_URI_KEY, profile.avatarUri)
            putString(RESUME_URL_KEY, profile.resumeUrl)
            putString(POSITION_KEY, profile.position)
            putString(EMAIL_KEY, profile.email)
            apply()
        }
    }

    override suspend fun getProfile(): Profile {
        return Profile(
            fullName = sharedPreferences.getString(FULL_NAME_KEY, "") ?: "",
            avatarUri = sharedPreferences.getString(AVATAR_URI_KEY, "") ?: "",
            resumeUrl = sharedPreferences.getString(RESUME_URL_KEY, "") ?: "",
            position = sharedPreferences.getString(POSITION_KEY, "") ?: "",
            email = sharedPreferences.getString(EMAIL_KEY, "") ?: ""
        )
    }

    companion object {
        private const val FULL_NAME_KEY = "profile_full_name"
        private const val AVATAR_URI_KEY = "profile_avatar_uri"
        private const val RESUME_URL_KEY = "profile_resume_url"
        private const val POSITION_KEY = "profile_position"
        private const val EMAIL_KEY = "profile_email"
    }
}