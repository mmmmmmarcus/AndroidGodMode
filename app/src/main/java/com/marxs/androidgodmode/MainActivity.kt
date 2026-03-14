package com.marxs.androidgodmode

import android.app.StatusBarManager
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.marxs.androidgodmode.ui.GodModeApp
import com.marxs.androidgodmode.ui.theme.AndroidGodModeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidGodModeTheme {
                GodModeApp(
                    onOpenWriteSettings = {
                        startActivity(
                            Intent(
                                Settings.ACTION_MANAGE_WRITE_SETTINGS,
                                Uri.parse("package:$packageName")
                            )
                        )
                    },
                    onOpenDeveloperOptions = {
                        startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
                    },
                    onOpenPrivateDns = {
                        startActivity(Intent("android.settings.PRIVATE_DNS_SETTINGS"))
                    },
                    onRequestTile = ::requestTile,
                    onOpenQuickSettingsHelp = {
                        Toast.makeText(
                            this,
                            "如果系统没弹添加框，就去快捷设置编辑页手动把 AndroidGodMode 拖进去。",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }
        }
    }

    private fun requestTile(slot: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Toast.makeText(this, "Android 13 以下请手动在快捷设置里添加磁贴", Toast.LENGTH_SHORT).show()
            return
        }

        val targetClass = when (slot) {
            1 -> GodModeTileOneService::class.java
            2 -> GodModeTileTwoService::class.java
            else -> GodModeTileThreeService::class.java
        }

        val statusBarManager = getSystemService(StatusBarManager::class.java)
        statusBarManager.requestAddTileService(
            ComponentName(this, targetClass),
            "GodMode $slot",
            android.graphics.drawable.Icon.createWithResource(this, R.drawable.ic_godmode),
            mainExecutor
        ) { result ->
            val message = when (result) {
                StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ALREADY_ADDED -> "磁贴 $slot 已经加进快捷设置了"
                StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ADDED -> "磁贴 $slot 已加入快捷设置"
                else -> "系统没直接加上，去快捷设置编辑页手动拖进去也行"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
