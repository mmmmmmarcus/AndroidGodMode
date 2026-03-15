package com.marxs.androidgodmode

import android.content.Context

class GodModePreferences(context: Context) {
    private val prefs = context.getSharedPreferences("android_god_mode", Context.MODE_PRIVATE)

    fun getLastScreenTimeout(): Int = prefs.getInt(KEY_LAST_SCREEN_TIMEOUT, 30_000)

    fun setLastScreenTimeout(timeoutMillis: Int) {
        prefs.edit().putInt(KEY_LAST_SCREEN_TIMEOUT, timeoutMillis).apply()
    }

    companion object {
        private const val KEY_LAST_SCREEN_TIMEOUT = "last_screen_timeout"
    }
}
