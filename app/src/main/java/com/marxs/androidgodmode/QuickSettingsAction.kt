package com.marxs.androidgodmode

import android.content.Context
import android.content.Intent
import android.provider.Settings

enum class QuickSettingsAction(
    val key: String,
    val title: String,
    val subtitle: String,
    val isToggle: Boolean
) {
    VIBRATION_TOGGLE(
        key = "vibration_toggle",
        title = "震动开关",
        subtitle = "切换系统触感反馈",
        isToggle = true
    ),
    AUTO_ROTATE_TOGGLE(
        key = "auto_rotate_toggle",
        title = "自动旋转",
        subtitle = "切换屏幕自动旋转",
        isToggle = true
    ),
    SCREEN_AWAKE_TOGGLE(
        key = "screen_awake_toggle",
        title = "延长亮屏",
        subtitle = "在 30 秒与 10 分钟熄屏间切换",
        isToggle = true
    ),
    OPEN_DEVELOPER_OPTIONS(
        key = "open_developer_options",
        title = "开发者选项",
        subtitle = "直接跳进系统开发者选项",
        isToggle = false
    ),
    OPEN_PRIVATE_DNS(
        key = "open_private_dns",
        title = "Private DNS",
        subtitle = "打开 Android Private DNS 页面",
        isToggle = false
    ),
    OPEN_GOD_MODE(
        key = "open_god_mode",
        title = "GodMode 面板",
        subtitle = "打开 AndroidGodMode 主页",
        isToggle = false
    );

    fun launchIntent(context: Context): Intent? {
        return when (this) {
            OPEN_DEVELOPER_OPTIONS -> Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
            OPEN_PRIVATE_DNS -> Intent(Settings.ACTION_WIRELESS_SETTINGS)
            OPEN_GOD_MODE -> context.packageManager.getLaunchIntentForPackage(context.packageName)
            else -> null
        }?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    companion object {
        val defaultSlots = listOf(VIBRATION_TOGGLE, AUTO_ROTATE_TOGGLE, OPEN_GOD_MODE)

        fun fromKey(key: String?): QuickSettingsAction {
            return entries.firstOrNull { it.key == key } ?: VIBRATION_TOGGLE
        }
    }
}
