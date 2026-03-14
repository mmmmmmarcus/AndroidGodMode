package com.marxs.androidgodmode

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings

class VibrationController(private val context: Context) {
    fun isEnabled(): Boolean {
        return Settings.System.getInt(
            context.contentResolver,
            Settings.System.HAPTIC_FEEDBACK_ENABLED,
            1
        ) == 1
    }

    fun setEnabled(enabled: Boolean): Result<Unit> {
        return runCatching {
            val value = if (enabled) 1 else 0
            val changed = Settings.System.putInt(
                context.contentResolver,
                Settings.System.HAPTIC_FEEDBACK_ENABLED,
                value
            )
            check(changed) { "Unable to update haptic feedback setting" }
            if (enabled) pulse()
        }
    }

    fun pulse() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (!vibrator.hasVibrator()) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(60)
        }
    }
}
