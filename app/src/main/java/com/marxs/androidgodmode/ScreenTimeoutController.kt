package com.marxs.androidgodmode

import android.content.Context
import android.provider.Settings

class ScreenTimeoutController(
    private val context: Context,
    private val preferences: GodModePreferences
) {
    private val longTimeout = 600_000

    fun currentTimeoutMillis(): Int {
        return Settings.System.getInt(
            context.contentResolver,
            Settings.System.SCREEN_OFF_TIMEOUT,
            30_000
        )
    }

    fun isAwakeModeEnabled(): Boolean = currentTimeoutMillis() >= longTimeout

    fun toggle(): Result<Unit> = runCatching {
        val current = currentTimeoutMillis()
        if (current < longTimeout) {
            preferences.setLastScreenTimeout(current)
            setTimeout(longTimeout)
        } else {
            setTimeout(preferences.getLastScreenTimeout().coerceAtLeast(15_000))
        }
    }

    fun setTimeout(timeoutMillis: Int) {
        val changed = Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_OFF_TIMEOUT,
            timeoutMillis
        )
        check(changed) { "Unable to update screen timeout" }
    }
}
