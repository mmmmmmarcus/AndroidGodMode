package com.marxs.androidgodmode

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.marxs.androidgodmode.ui.GodModeApp
import com.marxs.androidgodmode.ui.theme.AndroidGodModeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            var canWrite by mutableStateOf(Settings.System.canWrite(context))

            AndroidGodModeTheme {
                GodModeApp(
                    canWriteSettings = canWrite,
                    onOpenWriteSettings = {
                        startActivity(
                            Intent(
                                Settings.ACTION_MANAGE_WRITE_SETTINGS,
                                Uri.parse("package:$packageName")
                            )
                        )
                    },
                    refreshWriteSettings = {
                        canWrite = Settings.System.canWrite(context)
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}
