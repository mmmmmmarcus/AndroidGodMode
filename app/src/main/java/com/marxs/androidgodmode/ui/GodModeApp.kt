package com.marxs.androidgodmode.ui

import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MotionPhotosAuto
import androidx.compose.material.icons.rounded.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.marxs.androidgodmode.AnimationScaleState
import com.marxs.androidgodmode.QuickSettingsAction
import com.marxs.androidgodmode.QuickSettingsExecutor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GodModeApp(
    onOpenWriteSettings: () -> Unit,
    onOpenDeveloperOptions: () -> Unit,
    onRequestTile: () -> Unit,
    onOpenQuickSettingsHelp: () -> Unit
) {
    val context = LocalContext.current
    val executor = remember { QuickSettingsExecutor(context) }
    val allowedScales = remember { executor.animationScaleValues() }
    var canWriteSettings by remember { mutableStateOf(Settings.System.canWrite(context)) }
    var vibrationEnabled by remember { mutableStateOf(executor.isActionEnabled(QuickSettingsAction.VIBRATION_TOGGLE)) }
    var neverSleepEnabled by remember { mutableStateOf(executor.isNeverSleepEnabled()) }
    var neverSleepStatus by remember { mutableStateOf(executor.neverSleepStatusLabel()) }
    var animationState by remember { mutableStateOf(executor.animationScaleState()) }
    var windowScale by remember { mutableFloatStateOf(animationState.windowScale) }
    var transitionScale by remember { mutableFloatStateOf(animationState.transitionScale) }
    var animatorScale by remember { mutableFloatStateOf(animationState.animatorScale) }
    var statusMessage by remember { mutableStateOf("单磁贴模式已启用") }

    fun refreshStates() {
        canWriteSettings = Settings.System.canWrite(context)
        vibrationEnabled = executor.isActionEnabled(QuickSettingsAction.VIBRATION_TOGGLE)
        neverSleepEnabled = executor.isNeverSleepEnabled()
        neverSleepStatus = executor.neverSleepStatusLabel()
        animationState = executor.animationScaleState()
        windowScale = animationState.windowScale
        transitionScale = animationState.transitionScale
        animatorScale = animationState.animatorScale
    }

    LaunchedEffect(Unit) { refreshStates() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AndroidGodMode", fontWeight = FontWeight.SemiBold) }
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
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(Modifier.height(2.dp)) }
                if (!canWriteSettings) {
                    item { PermissionCard(onOpenWriteSettings) }
                }
                item {
                    SectionCard(title = "System") {
                        ToggleRow(
                            icon = Icons.Rounded.Vibration,
                            title = "触感反馈",
                            subtitle = if (vibrationEnabled) "开启" else "关闭",
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
                            subtitle = neverSleepStatus,
                            checked = neverSleepEnabled,
                            enabled = canWriteSettings,
                            onCheckedChange = {
                                executor.toggleNeverSleep()
                                refreshStates()
                                statusMessage = if (neverSleepEnabled) "已恢复正常锁屏时长" else "已切到近似永不锁屏"
                            }
                        )
                    }
                }
                item {
                    SectionCard(title = "Animation Scale") {
                        ToggleRow(
                            icon = Icons.Rounded.MotionPhotosAuto,
                            title = "动画倍率总开关",
                            subtitle = if (animationState.supported) "已接入系统动画倍率控制" else animationState.reason,
                            checked = animationState.enabled,
                            enabled = false,
                            onCheckedChange = {
                                executor.setAnimationScaleEnabled(!animationState.enabled)
                                refreshStates()
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                        ScaleEditorRow(
                            label = "Window",
                            value = windowScale,
                            values = allowedScales,
                            enabled = false,
                            onCycle = {
                                windowScale = nextScale(windowScale, allowedScales)
                                statusMessage = "动画倍率需要 ADB / Shizuku / Root 才能真正写入"
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                        ScaleEditorRow(
                            label = "Transition",
                            value = transitionScale,
                            values = allowedScales,
                            enabled = false,
                            onCycle = {
                                transitionScale = nextScale(transitionScale, allowedScales)
                                statusMessage = "动画倍率需要 ADB / Shizuku / Root 才能真正写入"
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                        ScaleEditorRow(
                            label = "Animator",
                            value = animatorScale,
                            values = allowedScales,
                            enabled = false,
                            onCycle = {
                                animatorScale = nextScale(animatorScale, allowedScales)
                                statusMessage = "动画倍率需要 ADB / Shizuku / Root 才能真正写入"
                            }
                        )
                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = onOpenDeveloperOptions) { Text("开发者选项") }
                            OutlinedButton(onClick = {
                                statusMessage = "等后面接 Shizuku / ADB，这组就能真的生效"
                            }) { Text("稍后接入") }
                        }
                    }
                }
                item {
                    SectionCard(title = "Quick Settings") {
                        Text(
                            "当前只保留一个磁贴：触感反馈。",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = onRequestTile) { Text("添加磁贴") }
                            OutlinedButton(onClick = onOpenQuickSettingsHelp) { Text("说明") }
                        }
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                            Spacer(Modifier.height(10.dp))
                            Text(
                                "Android 13 以下需在快捷设置编辑页手动拖入磁贴。",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                item {
                    StatusCard(statusMessage = statusMessage, canWriteSettings = canWriteSettings)
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

private fun nextScale(current: Float, values: List<Float>): Float {
    val index = values.indexOfFirst { it == current }.takeIf { it >= 0 } ?: 0
    return values[(index + 1) % values.size]
}

@Composable
private fun PermissionCard(onOpenWriteSettings: () -> Unit) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Rounded.Lock, contentDescription = null)
                Text("需要修改系统设置权限", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            Text("不给这个权限，触感反馈和永不锁屏都无法真正写入系统。", style = MaterialTheme.typography.bodyMedium)
            Button(onClick = onOpenWriteSettings) { Text("去授权") }
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
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
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, enabled = enabled, onCheckedChange = { onCheckedChange() })
    }
}

@Composable
private fun ScaleEditorRow(
    label: String,
    value: Float,
    values: List<Float>,
    enabled: Boolean,
    onCycle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(label, modifier = Modifier.width(84.dp), fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            value = "${value}x",
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            modifier = Modifier.weight(1f)
        )
        OutlinedButton(onClick = onCycle, enabled = enabled) {
            Text("切换")
        }
    }
    Text(
        text = "可选：${values.joinToString(" / ") { "${it}x" }}",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun StatusCard(statusMessage: String, canWriteSettings: Boolean) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Status", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
            Text(statusMessage, style = MaterialTheme.typography.bodyMedium)
            Text(
                if (canWriteSettings) "系统写入权限已就绪。"
                else "请先授权 WRITE_SETTINGS。",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
