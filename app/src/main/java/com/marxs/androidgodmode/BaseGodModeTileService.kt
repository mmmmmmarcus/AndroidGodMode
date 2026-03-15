package com.marxs.androidgodmode

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

abstract class BaseGodModeTileService : TileService() {
    abstract val slot: Int

    private val executor by lazy { QuickSettingsExecutor(this) }

    override fun onStartListening() {
        super.onStartListening()
        refreshTile()
    }

    override fun onClick() {
        super.onClick()
        executor.execute(QuickSettingsAction.VIBRATION_TOGGLE)
        refreshTile()
    }

    fun refreshTile() {
        val tile = qsTile ?: return
        tile.label = QuickSettingsAction.VIBRATION_TOGGLE.title
        tile.subtitle = QuickSettingsAction.VIBRATION_TOGGLE.subtitle
        tile.state = when {
            !executor.canWriteSettings() -> Tile.STATE_UNAVAILABLE
            executor.isActionEnabled(QuickSettingsAction.VIBRATION_TOGGLE) -> Tile.STATE_ACTIVE
            else -> Tile.STATE_INACTIVE
        }
        tile.updateTile()
    }
}
