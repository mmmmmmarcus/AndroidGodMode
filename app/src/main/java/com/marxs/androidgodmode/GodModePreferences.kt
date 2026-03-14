package com.marxs.androidgodmode

import android.content.Context

class GodModePreferences(context: Context) {
    private val prefs = context.getSharedPreferences("android_god_mode", Context.MODE_PRIVATE)

    fun getTileAction(slot: Int): QuickSettingsAction {
        return QuickSettingsAction.fromKey(prefs.getString(tileKey(slot), QuickSettingsAction.defaultSlots.getOrNull(slot - 1)?.key))
    }

    fun setTileAction(slot: Int, action: QuickSettingsAction) {
        prefs.edit().putString(tileKey(slot), action.key).apply()
    }

    fun getLastScreenTimeout(): Int = prefs.getInt(KEY_LAST_SCREEN_TIMEOUT, 30_000)

    fun setLastScreenTimeout(timeoutMillis: Int) {
        prefs.edit().putInt(KEY_LAST_SCREEN_TIMEOUT, timeoutMillis).apply()
    }

    private fun tileKey(slot: Int) = "tile_slot_$slot"

    companion object {
        private const val KEY_LAST_SCREEN_TIMEOUT = "last_screen_timeout"
    }
}
