package com.marxs.androidgodmode

import android.content.Context
import android.provider.Settings

class QuickSettingsExecutor(private val context: Context) {
    private val vibrationController = VibrationController(context)
    private val preferences = GodModePreferences(context)
    private val screenTimeoutController = ScreenTimeoutController(context, preferences)

    fun canWriteSettings(): Boolean = Settings.System.canWrite(context)

    fun isActionEnabled(action: QuickSettingsAction): Boolean {
        return when (action) {
            QuickSettingsAction.VIBRATION_TOGGLE -> vibrationController.isEnabled()
        }
    }

    fun execute(action: QuickSettingsAction): Result<Unit> {
        if (!canWriteSettings()) {
            return Result.failure(IllegalStateException("WRITE_SETTINGS permission required"))
        }
        return when (action) {
            QuickSettingsAction.VIBRATION_TOGGLE -> vibrationController.setEnabled(!vibrationController.isEnabled())
        }
    }

    fun isNeverSleepEnabled(): Boolean = screenTimeoutController.isNeverSleepEnabled()

    fun toggleNeverSleep(): Result<Unit> {
        if (!canWriteSettings()) {
            return Result.failure(IllegalStateException("WRITE_SETTINGS permission required"))
        }
        return screenTimeoutController.toggleNeverSleep()
    }
}
