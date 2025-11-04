package com.example.android_practice.data.cache

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BadgeCache {
    private val _showBadge = MutableStateFlow(false)
    val showBadge: StateFlow<Boolean> = _showBadge.asStateFlow()

    fun setShowBadge(show: Boolean) {
        _showBadge.value = show
    }

    fun shouldShowBadge(): Boolean = _showBadge.value
}