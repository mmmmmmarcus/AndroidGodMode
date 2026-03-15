# AndroidGodMode

面向 Android 深层系统行为与快捷设置磁贴的控制面板 APK。

## 当前功能

- Material 3 / Jetpack Compose UI
- 触感反馈总开关
- 永不锁屏开关（正常锁屏时长 / 永不锁屏）
- 1 个 Quick Settings Tile：触感反馈
- Android 13+ 支持应用内请求把触感反馈磁贴加入系统快捷设置
- Android 12 及以下可手动在快捷设置编辑页拖入磁贴

## 构建

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew assembleDebug
```

生成物：

- `app/build/outputs/apk/debug/app-debug.apk`

## 权限说明

- `WRITE_SETTINGS`：切换类动作所必需
- `BIND_QUICK_SETTINGS_TILE`：由系统授予给 tile service
