package com.marxs.androidgodmode

import android.content.Context
import android.provider.Settings

class ScreenTimeoutController(
    private val context: Context,
    private val preferences: GodModePreferences
) {
    private val neverSleepTimeout = Int.MAX_VALUE

    fun currentTimeoutMillis(): Int {
        return Settings.System.getInt(
            context.contentResolver,
            Settings.System.SCREEN_OFF_TIMEOUT,
            30_000
        )
    }

    fun isNeverSleepEnabled(): Boolean = currentTimeoutMillis() >= neverSleepTimeout

    fun toggleNeverSleep(): Result<Unit> = runCatching {
        val current = currentTimeoutMillis()
        if (current < neverSleepTimeout) {
            preferences.setLastScreenTimeout(current)
            setTimeout(neverSleepTimeout)
        } else {
            setTimeout(preferences.getLastScreenTimeout().coerceAtLeast(15_000))
        }
    }

    private fun setTimeout(timeoutMillis: Int) {
        val changed = Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_OFF_TIMEOUT,
            timeoutMillis
        )
        check(changed) { "Unable to update screen timeout" }
    }
}
