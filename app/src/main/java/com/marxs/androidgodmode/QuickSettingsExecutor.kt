package com.marxs.androidgodmode

import android.content.Context
import android.provider.Settings
class QuickSettingsExecutor(private val context: Context) {
    private val vibrationController = VibrationController(context)
    private val rotationController = RotationController(context)
    private val preferences = GodModePreferences(context)
    private val screenTimeoutController = ScreenTimeoutController(context, preferences)

    fun canWriteSettings(): Boolean = Settings.System.canWrite(context)

    fun isActionEnabled(action: QuickSettingsAction): Boolean {
        return when (action) {
            QuickSettingsAction.VIBRATION_TOGGLE -> vibrationController.isEnabled()
            QuickSettingsAction.AUTO_ROTATE_TOGGLE -> rotationController.isEnabled()
            QuickSettingsAction.SCREEN_AWAKE_TOGGLE -> screenTimeoutController.isAwakeModeEnabled()
            else -> false
        }
    }

    fun execute(action: QuickSettingsAction): Result<Unit> {
        if (action.isToggle && !canWriteSettings()) {
            return Result.failure(IllegalStateException("WRITE_SETTINGS permission required"))
        }
        return when (action) {
            QuickSettingsAction.VIBRATION_TOGGLE -> vibrationController.setEnabled(!vibrationController.isEnabled())
            QuickSettingsAction.AUTO_ROTATE_TOGGLE -> rotationController.setEnabled(!rotationController.isEnabled())
            QuickSettingsAction.SCREEN_AWAKE_TOGGLE -> screenTimeoutController.toggle()
            QuickSettingsAction.OPEN_DEVELOPER_OPTIONS,
            QuickSettingsAction.OPEN_PRIVATE_DNS,
            QuickSettingsAction.OPEN_GOD_MODE -> runCatching {
                val intent = action.launchIntent(context) ?: error("Intent unavailable")
                context.startActivity(intent)
            }
        }
    }
}
