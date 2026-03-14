package com.marxs.androidgodmode

import android.content.Context
import android.provider.Settings

class RotationController(private val context: Context) {
    fun isEnabled(): Boolean {
        return Settings.System.getInt(
            context.contentResolver,
            Settings.System.ACCELEROMETER_ROTATION,
            0
        ) == 1
    }

    fun setEnabled(enabled: Boolean): Result<Unit> = runCatching {
        val changed = Settings.System.putInt(
            context.contentResolver,
            Settings.System.ACCELEROMETER_ROTATION,
            if (enabled) 1 else 0
        )
        check(changed) { "Unable to update auto-rotate setting" }
    }
}
