package com.marxs.androidgodmode

import android.content.Intent
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

abstract class BaseGodModeTileService : TileService() {
    abstract val slot: Int

    private val preferences by lazy { GodModePreferences(this) }
    private val executor by lazy { QuickSettingsExecutor(this) }

    override fun onStartListening() {
        super.onStartListening()
        refreshTile()
    }

    override fun onClick() {
        super.onClick()
        val action = preferences.getTileAction(slot)
        if (!action.isToggle) {
            val intent = action.launchIntent(this) ?: Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivityAndCollapse(intent)
            return
        }

        executor.execute(action)
        refreshTile()
    }

    fun refreshTile() {
        val tile = qsTile ?: return
        val action = preferences.getTileAction(slot)
        tile.label = action.title
        tile.subtitle = action.subtitle
        tile.state = when {
            action.isToggle && !executor.canWriteSettings() -> Tile.STATE_UNAVAILABLE
            action.isToggle && executor.isActionEnabled(action) -> Tile.STATE_ACTIVE
            action.isToggle -> Tile.STATE_INACTIVE
            else -> Tile.STATE_INACTIVE
        }
        tile.updateTile()
    }
}
