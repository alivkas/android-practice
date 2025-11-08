package com.example.android_practice.domain.model

data class Profile(
    val id: Int = 1,
    val fullName: String = "",
    val avatarUri: String = "",
    val resumeUrl: String = "",
    val position: String = "",
    val email: String = ""
) {
    val isEmpty: Boolean
        get() = fullName.isEmpty() && avatarUri.isEmpty() && resumeUrl.isEmpty() && position.isEmpty() && email.isEmpty()
}