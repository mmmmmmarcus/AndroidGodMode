package com.marxs.androidgodmode

import android.content.Context
import android.provider.Settings

class ScreenTimeoutController(
    private val context: Context,
    private val preferences: GodModePreferences
) {
    private val neverSleepTimeout = 86_400_000

    fun currentTimeoutMillis(): Int {
        return Settings.System.getInt(
            context.contentResolver,
            Settings.System.SCREEN_OFF_TIMEOUT,
            30_000
        )
    }

    fun isNeverSleepEnabled(): Boolean = currentTimeoutMillis() >= neverSleepTimeout

    fun statusLabel(): String {
        val timeout = currentTimeoutMillis()
        return if (timeout >= neverSleepTimeout) {
            "当前为超长亮屏（近似永不锁屏）"
        } else {
            "当前锁屏时长：${formatTimeout(timeout)}"
        }
    }

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

    private fun formatTimeout(timeoutMillis: Int): String {
        val seconds = timeoutMillis / 1000
        return when {
            seconds < 60 -> "${seconds} 秒"
            seconds % 60 == 0 -> "${seconds / 60} 分钟"
            else -> "${seconds / 60} 分 ${seconds % 60} 秒"
        }
    }
}
