package com.marxs.androidgodmode

enum class QuickSettingsAction(
    val key: String,
    val title: String,
    val subtitle: String,
    val isToggle: Boolean
) {
    VIBRATION_TOGGLE(
        key = "vibration_toggle",
        title = "触感反馈",
        subtitle = "切换系统触感反馈",
        isToggle = true
    )
}
