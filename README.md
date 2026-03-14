# AndroidGodMode

一个面向 Android 设备深层设置的控制面板 APK，当前首版实现了**设备震动/触感反馈总开关**。

## v0.1.0

- Material 3 风格 UI（Jetpack Compose）
- 首个 God Mode 开关：设备震动 / 触感反馈
- 自动检测并引导开启“修改系统设置”权限
- 开启震动时会触发一次短脉冲作为反馈

## 构建

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew assembleDebug
```

生成物：

- `app/build/outputs/apk/debug/app-debug.apk`

## 说明

Android 普通应用无法像 root 工具那样无条件修改所有底层开关，所以当前版本采用系统允许的 `WRITE_SETTINGS` 能力来控制触感反馈；首次使用需要手动授权。

后续可以继续往里扩：

- 动画倍率
- 开发者选项快捷入口
- 屏幕常亮
- 自动旋转
- Wi‑Fi / 蓝牙 / NFC 相关设置页
- USB 默认模式
- 亮度策略 / 休眠策略
