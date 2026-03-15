package com.marxs.androidgodmode.ui

import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.SettingsSuggest
import androidx.compose.material.icons.rounded.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.marxs.androidgodmode.QuickSettingsAction
import com.marxs.androidgodmode.QuickSettingsExecutor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GodModeApp(
    onOpenWriteSettings: () -> Unit,
    onRequestTile: () -> Unit,
    onOpenQuickSettingsHelp: () -> Unit
) {
    val context = LocalContext.current
    val executor = remember { QuickSettingsExecutor(context) }
    var canWriteSettings by remember { mutableStateOf(Settings.System.canWrite(context)) }
    var vibrationEnabled by remember { mutableStateOf(executor.isActionEnabled(QuickSettingsAction.VIBRATION_TOGGLE)) }
    var neverSleepEnabled by remember { mutableStateOf(executor.isNeverSleepEnabled()) }
    var statusMessage by remember { mutableStateOf("触感反馈磁贴已收敛为单磁贴模式") }

    fun refreshStates() {
        canWriteSettings = Settings.System.canWrite(context)
        vibrationEnabled = executor.isActionEnabled(QuickSettingsAction.VIBRATION_TOGGLE)
        neverSleepEnabled = executor.isNeverSleepEnabled()
    }

    LaunchedEffect(Unit) { refreshStates() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("AndroidGodMode", fontWeight = FontWeight.Bold)
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(Modifier.height(4.dp)) }
                if (!canWriteSettings) {
                    item { PermissionCard(onOpenWriteSettings) }
                }
                item {
                    QuickActionCard(title = "深层可切开关") {
                        ToggleRow(
                            icon = Icons.Rounded.Vibration,
                            title = "触感反馈",
                            subtitle = if (vibrationEnabled) "已开启" else "已关闭",
                            checked = vibrationEnabled,
                            enabled = canWriteSettings,
                            onCheckedChange = {
                                executor.execute(QuickSettingsAction.VIBRATION_TOGGLE)
                                refreshStates()
                                statusMessage = if (vibrationEnabled) "触感反馈已关闭" else "触感反馈已开启"
                            }
                        )
                        HorizontalDivider(Modifier.padding(vertical = 8.dp))
                        ToggleRow(
                            icon = Icons.Rounded.CheckCircle,
                            title = "永不锁屏",
                            subtitle = if (neverSleepEnabled) "当前为永不自动锁屏" else "当前为正常锁屏时长",
                            checked = neverSleepEnabled,
                            enabled = canWriteSettings,
                            onCheckedChange = {
                                executor.toggleNeverSleep()
                                refreshStates()
                                statusMessage = if (neverSleepEnabled) "已恢复正常锁屏时长" else "已切到永不锁屏"
                            }
                        )
                    }
                }
                item {
                    QuickSettingsCard(onRequestTile = onRequestTile, onOpenQuickSettingsHelp = onOpenQuickSettingsHelp)
                }
                item {
                    StatusCard(statusMessage = statusMessage, canWriteSettings = canWriteSettings)
                }
                item { Spacer(Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
private fun PermissionCard(onOpenWriteSettings: () -> Unit) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Rounded.Lock, contentDescription = null)
                Text("需要修改系统设置权限", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Text("不给这个权限，触感反馈和永不锁屏都不能真正生效。")
            Button(onClick = onOpenWriteSettings) { Text("去授权") }
        }
    }
}

@Composable
private fun QuickActionCard(title: String, content: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            content()
        }
    }
}

@Composable
private fun ToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, enabled = enabled, onCheckedChange = { onCheckedChange() })
    }
}

@Composable
private fun QuickSettingsCard(onRequestTile: () -> Unit, onOpenQuickSettingsHelp: () -> Unit) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Quick Settings", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("当前只保留一个系统快捷磁贴：触感反馈。")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onRequestTile) { Text("添加触感反馈磁贴") }
                OutlinedButton(onClick = onOpenQuickSettingsHelp) { Text("查看说明") }
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                Text("Android 13 以下系统一般不会弹添加请求框，请去快捷设置编辑页手动拖入磁贴。", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun StatusCard(statusMessage: String, canWriteSettings: Boolean) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("当前状态", fontWeight = FontWeight.Bold)
            Text(statusMessage)
            Text(
                if (canWriteSettings) "当前切换类动作可正常工作。"
                else "请先授权 WRITE_SETTINGS。",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
