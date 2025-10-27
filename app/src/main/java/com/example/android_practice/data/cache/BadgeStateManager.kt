package com.example.android_practice.data.cache

object BadgeStateManager {
    private var hasActiveFilters: Boolean = false

    fun setHasActiveFilters(hasFilters: Boolean) {
        hasActiveFilters = hasFilters
    }

    fun shouldShowBadge(): Boolean = hasActiveFilters
}