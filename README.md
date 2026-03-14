# AndroidGodMode

面向 Android 深层系统行为与快捷设置磁贴的控制面板 APK。

## v0.4.0

这一版不再试图复制控制中心，而是把 **God Mode 能力** 聚焦到两件事：

1. 可在授权后直接切换的深层系统行为
2. 可挂进 Quick Settings 的工程师向入口与动作

### 已实现

- Material 3 / Jetpack Compose UI
- 触感反馈总开关
- 自动旋转开关
- 延长亮屏模式（在常规熄屏时长与 10 分钟之间切换）
- 3 个 Quick Settings Tile Service
- Tile 动作可配置：
  - 震动开关
  - 自动旋转
  - 延长亮屏
  - 开发者选项
  - Private DNS
  - 打开 AndroidGodMode 面板
- Android 13+ 支持应用内请求把磁贴加入系统快捷设置
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

## 设计方向

AndroidGodMode 不做基础控制中心，而做：

- 深层行为开关
- 难找的系统高级入口
- Quick Settings 里的 God Mode 面板

后续可继续扩：

- 动画倍率三件套
- 无线调试 / USB 行为
- Usage Access / Accessibility / 电池优化白名单入口
- Shizuku 模式
- Root 模式
