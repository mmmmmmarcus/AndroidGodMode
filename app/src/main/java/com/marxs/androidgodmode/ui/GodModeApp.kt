package com.marxs.androidgodmode.ui

import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Dns
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.SettingsSuggest
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.Vibration
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.marxs.androidgodmode.GodModePreferences
import com.marxs.androidgodmode.QuickSettingsAction
import com.marxs.androidgodmode.QuickSettingsExecutor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GodModeApp(
    onOpenWriteSettings: () -> Unit,
    onOpenDeveloperOptions: () -> Unit,
    onOpenPrivateDns: () -> Unit,
    onRequestTile: (Int) -> Unit,
    onOpenQuickSettingsHelp: () -> Unit
) {
    val context = LocalContext.current
    val executor = remember { QuickSettingsExecutor(context) }
    val preferences = remember { GodModePreferences(context) }
    var canWriteSettings by remember { mutableStateOf(Settings.System.canWrite(context)) }
    var vibrationEnabled by remember { mutableStateOf(executor.isActionEnabled(QuickSettingsAction.VIBRATION_TOGGLE)) }
    var rotateEnabled by remember { mutableStateOf(executor.isActionEnabled(QuickSettingsAction.AUTO_ROTATE_TOGGLE)) }
    var awakeModeEnabled by remember { mutableStateOf(executor.isActionEnabled(QuickSettingsAction.SCREEN_AWAKE_TOGGLE)) }
    var tile1 by remember { mutableStateOf(preferences.getTileAction(1)) }
    var tile2 by remember { mutableStateOf(preferences.getTileAction(2)) }
    var tile3 by remember { mutableStateOf(preferences.getTileAction(3)) }
    var statusMessage by remember { mutableStateOf("v0.4：多磁贴、可配置动作、深层入口聚合") }
    var versionStage by remember { mutableIntStateOf(4) }

    fun refreshStates() {
        canWriteSettings = Settings.System.canWrite(context)
        vibrationEnabled = executor.isActionEnabled(QuickSettingsAction.VIBRATION_TOGGLE)
        rotateEnabled = executor.isActionEnabled(QuickSettingsAction.AUTO_ROTATE_TOGGLE)
        awakeModeEnabled = executor.isActionEnabled(QuickSettingsAction.SCREEN_AWAKE_TOGGLE)
        tile1 = preferences.getTileAction(1)
        tile2 = preferences.getTileAction(2)
        tile3 = preferences.getTileAction(3)
    }

    LaunchedEffect(Unit) { refreshStates() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("AndroidGodMode", fontWeight = FontWeight.Bold)
                        Text("v0.$versionStage · Quick Settings God Mode", style = MaterialTheme.typography.labelMedium)
                    }
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
                item {
                    HeroCard(versionStage = versionStage)
                }
                item {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(onClick = { versionStage = 2 }, label = { Text("v0.2 单磁贴") })
                        AssistChip(onClick = { versionStage = 3 }, label = { Text("v0.3 多磁贴") })
                        AssistChip(onClick = { versionStage = 4 }, label = { Text("v0.4 可配置") })
                    }
                }
                if (!canWriteSettings) {
                    item {
                        PermissionCard(onOpenWriteSettings)
                    }
                }
                item {
                    QuickActionCard(
                        title = "深层可切开关",
                        subtitle = "这些不是控制中心常规入口，而是系统里埋得更深、但还能由普通 APK 在授权后直接控制的那批。"
                    ) {
                        ToggleRow(
                            icon = Icons.Rounded.Vibration,
                            title = "触感反馈",
                            subtitle = if (vibrationEnabled) "已开启" else "已关闭",
                            checked = vibrationEnabled,
                            enabled = canWriteSettings,
                            onCheckedChange = {
                                executor.execute(QuickSettingsAction.VIBRATION_TOGGLE)
                                refreshStates()
                                statusMessage = if (vibrationEnabled) "震动已关闭" else "震动已开启"
                            }
                        )
                        HorizontalDivider(Modifier.padding(vertical = 8.dp))
                        ToggleRow(
                            icon = Icons.Rounded.AutoAwesome,
                            title = "自动旋转",
                            subtitle = if (rotateEnabled) "传感器旋转已开启" else "已锁定竖屏/当前方向",
                            checked = rotateEnabled,
                            enabled = canWriteSettings,
                            onCheckedChange = {
                                executor.execute(QuickSettingsAction.AUTO_ROTATE_TOGGLE)
                                refreshStates()
                                statusMessage = if (rotateEnabled) "自动旋转已关闭" else "自动旋转已开启"
                            }
                        )
                        HorizontalDivider(Modifier.padding(vertical = 8.dp))
                        ToggleRow(
                            icon = Icons.Rounded.CheckCircle,
                            title = "延长亮屏",
                            subtitle = if (awakeModeEnabled) "当前为 10 分钟熄屏" else "当前为常规熄屏时长",
                            checked = awakeModeEnabled,
                            enabled = canWriteSettings,
                            onCheckedChange = {
                                executor.execute(QuickSettingsAction.SCREEN_AWAKE_TOGGLE)
                                refreshStates()
                                statusMessage = if (awakeModeEnabled) "已恢复常规熄屏时长" else "已切到 10 分钟亮屏"
                            }
                        )
                    }
                }
                item {
                    QuickActionCard(
                        title = "系统深层入口",
                        subtitle = "这些入口适合做成快捷磁贴，点一下就进系统深处。"
                    ) {
                        ShortcutButtonRow(
                            icon = Icons.Rounded.Code,
                            title = "开发者选项",
                            subtitle = "工程师高频页",
                            onClick = onOpenDeveloperOptions
                        )
                        ShortcutButtonRow(
                            icon = Icons.Rounded.Dns,
                            title = "Private DNS",
                            subtitle = "网络高级页",
                            onClick = onOpenPrivateDns
                        )
                        ShortcutButtonRow(
                            icon = Icons.Rounded.SettingsSuggest,
                            title = "Quick Settings 添加帮助",
                            subtitle = "让系统接纳 GodMode 磁贴",
                            onClick = onOpenQuickSettingsHelp
                        )
                    }
                }
                item {
                    QuickTileConfigCard(
                        tile1 = tile1,
                        tile2 = tile2,
                        tile3 = tile3,
                        onChange = { slot, action ->
                            preferences.setTileAction(slot, action)
                            refreshStates()
                            statusMessage = "磁贴 $slot 已改成：${action.title}"
                        },
                        onRequestTile = { slot ->
                            onRequestTile(slot)
                            refreshStates()
                        }
                    )
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
private fun HeroCard(versionStage: Int) {
    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Rounded.Android, contentDescription = null)
                Text("God Mode 控制台", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Text(
                "我们这次不做控制中心重复品，直接走深层路线：把可切的系统行为 + 可挂进快捷设置的深层入口，压成一个工程师面板。当前 UI 已一步推进到 v0.$versionStage。",
                style = MaterialTheme.typography.bodyMedium
            )
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
            Text("不给这个权限，磁贴里的切换类动作只能显示但不能真正生效。入口类磁贴仍然可以正常打开系统页面。")
            Button(onClick = onOpenWriteSettings) { Text("去授权") }
        }
    }
}

@Composable
private fun QuickActionCard(title: String, subtitle: String, content: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
private fun ShortcutButtonRow(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        OutlinedButton(onClick = onClick) { Text("打开") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickTileConfigCard(
    tile1: QuickSettingsAction,
    tile2: QuickSettingsAction,
    tile3: QuickSettingsAction,
    onChange: (Int, QuickSettingsAction) -> Unit,
    onRequestTile: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Quick Settings 磁贴配置", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("v0.2 跑通第一颗磁贴，v0.3 扩到多磁贴，v0.4 开始允许你给每个磁贴重新分配动作。")
            TileSlotEditor(slot = 1, selected = tile1, onChange = onChange, onRequestTile = onRequestTile)
            TileSlotEditor(slot = 2, selected = tile2, onChange = onChange, onRequestTile = onRequestTile)
            TileSlotEditor(slot = 3, selected = tile3, onChange = onChange, onRequestTile = onRequestTile)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                Text("Android 13 以下系统一般不会弹“添加磁贴”请求框，请去快捷设置编辑页手动拖进去。", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TileSlotEditor(
    slot: Int,
    selected: QuickSettingsAction,
    onChange: (Int, QuickSettingsAction) -> Unit,
    onRequestTile: (Int) -> Unit
) {
    var expanded by remember(selected) { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("磁贴 $slot", fontWeight = FontWeight.SemiBold)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = selected.title,
                onValueChange = {},
                readOnly = true,
                label = { Text("当前动作") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                QuickSettingsAction.entries.forEach { action ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(action.title)
                                Text(action.subtitle, style = MaterialTheme.typography.bodySmall)
                            }
                        },
                        onClick = {
                            expanded = false
                            onChange(slot, action)
                        }
                    )
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onRequestTile(slot) }) { Text("请求添加") }
            OutlinedButton(onClick = { onChange(slot, QuickSettingsAction.OPEN_GOD_MODE) }) { Text("设为打开面板") }
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
                if (canWriteSettings) "切换类动作可正常工作。"
                else "当前只有入口类动作可完全工作；切换类动作需要先授权 WRITE_SETTINGS。",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
